package project.graduation.crowd_sourcing.presentation.ui.screen.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.entity.Mart
import project.graduation.crowd_sourcing.domain.usecase.MartSearchUseCase
import javax.inject.Inject
import kotlin.math.*

// TODO: Domain Layer 구현 필요
// 1. UseCase 주입 및 사용
//    - ViewModel에 UseCase 주입
//    - 더미 데이터 대신 실제 UseCase 사용
// 2. Repository 구현
//    - GoogleMapsRepository: Google Maps API 연동
//    - PlacesRepository: 장소 검색 API 연동
// 3. 에러 처리
//    - UseCase에서 발생하는 예외 처리
//    - 사용자에게 적절한 에러 메시지 표시

// 변경 내역:
// 1. 상태 관리 개선
//    - sealed class를 사용한 상태 관리 도입 (Loading, Success, Error)
//    - 상태 업데이트 시 when 표현식을 사용하여 타입 안전성 확보
// 2. 에러 처리 추가
//    - try-catch 블록을 사용한 예외 처리
//    - 에러 상태에 대한 UI 처리
// 3. 초기화 로직 개선
//    - loadInitialData() 함수를 통한 초기화
//    - viewModelScope를 사용한 코루틴 스코프 관리
// 4. 더미 데이터 관리
//    - companion object를 사용한 더미 데이터 관리
//    - 일관된 네이밍 컨벤션 적용

/**
 * 홈 화면의 비즈니스 로직을 처리하는 ViewModel
 * 위치 정보, 검색 결과, 의뢰 목록 등의 상태 관리
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val martSearchUseCase: MartSearchUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _nearbyMarts = MutableStateFlow<List<Mart>>(emptyList())
    val nearbyMarts: StateFlow<List<Mart>> = _nearbyMarts.asStateFlow()
    
    // 위치 정보 캐싱을 위한 변수
    private var lastLat: Double? = null
    private var lastLng: Double? = null
    private var lastRadius: Int? = null
    
    // 마트 데이터 캐싱을 위한 변수 추가
    private var cachedMarts: List<Mart> = emptyList()
    
    companion object {
        private const val TAG = "HomeViewModel"
        
        /**
         * 테스트용 더미 의뢰 데이터
         * Domain 계층 구현 시 실제 데이터로 교체 필요
         */
        private val DUMMY_REQUESTS = listOf(
            Request(
                id = "1",
                title = "수색 아파트 - 벌기 편백",
                location = Location(37.5820, 126.8895),
                place = "수색 아파트",
                reward = 15000
            ),
            Request(
                id = "2",
                title = "상암 물품 - 벌기 가격",
                location = Location(37.5784, 126.8967),
                place = "상암동",
                reward = 10000
            )
        )
    }

    init {
        loadInitialData()
        getCurrentLocation()
    }

    /**
     * 현재 위치 정보를 가져와 상태를 업데이트
     * 위치 권한이 있는 경우에만 동작
     */
    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val latitude = it.latitude
                        val longitude = it.longitude
                        
                        Log.d(TAG, "현재 위치: $latitude, $longitude")
                        
                        // 위치가 변경된 경우에만 업데이트 및 마트 검색
                        if (lastLat != latitude || lastLng != longitude) {
                            lastLat = latitude
                            lastLng = longitude
                            
                            Log.d(TAG, "위치 변경됨: 새로운 위치로 업데이트")
                            updateCurrentLocation(latitude, longitude)
                            
                            // 현재 반경 값으로 주변 마트 검색
                            val currentState = _uiState.value as? HomeUiState.Success
                            val radius = (currentState?.searchRadius ?: 0.1f).coerceIn(0.1f, 0.5f)
                            searchNearbyMarts(latitude, longitude, radius)
                        } else {
                            Log.d(TAG, "위치 변경 없음: 마트 검색 스킵")
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "위치 정보 오류: ${e.message}", e)
            _uiState.update { 
                HomeUiState.Error("위치 정보를 가져오는데 실패했습니다: ${e.message}")
            }
        }
    }

    /**
     * 초기 데이터를 로드하여 상태를 설정
     * 현재는 더미 데이터를 사용하지만 향후 실제 데이터로 교체 필요
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                _uiState.update { 
                    HomeUiState.Success(
                        requests = emptyList()
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "초기 데이터 로드 오류: ${e.message}", e)
                _uiState.update { 
                    HomeUiState.Error("데이터를 불러오는데 실패했습니다: ${e.message}")
                }
            }
        }
    }

    private fun searchNearbyMarts(lat: Double, lng: Double, searchRadius: Float? = null) {
        viewModelScope.launch {
            try {
                val currentState = _uiState.value as? HomeUiState.Success ?: return@launch
                val radiusInKm = (searchRadius ?: currentState.searchRadius).coerceIn(0.1f, 0.5f)
                val radiusInMeters = (radiusInKm * 1000).toInt()
                
                // 위치가 변경된 경우에만 새로운 API 호출
                val shouldRefreshData = lastLat != lat || lastLng != lng || cachedMarts.isEmpty()
                
                if (shouldRefreshData) {
                    Log.d(TAG, "위치 변경 또는 최초 로드: 새로운 마트 데이터 요청 ($lat, $lng)")
                    
                    // 위치 정보 캐싱
                    lastLat = lat
                    lastLng = lng
                    
                    // 서버에 고정 반경(500m)으로 API 요청
                    val apiRadius = 500
                    try {
                        val marts = martSearchUseCase.searchMartByLocation(lat, lng, apiRadius)
                        Log.d(TAG, "API 응답: ${marts.size}개 마트 로드 성공")
                        
                        // 마트 데이터 캐싱
                        cachedMarts = marts
                    } catch (e: Exception) {
                        Log.e(TAG, "API 호출 실패: ${e.message}", e)
                        // API 호출 실패 시 캐시된 데이터 유지
                    }
                } else {
                    Log.d(TAG, "캐시된 마트 데이터 사용 (마트 ${cachedMarts.size}개)")
                }
                
                // 반경은 변경되었을 수 있으므로 매번 필터링
                val filteredMarts = cachedMarts.filter { mart ->
                    val distance = distanceInMeters(lat, lng, mart.lat, mart.lng)
                    val inRadius = distance <= radiusInMeters
                    Log.d(TAG, "마트: ${mart.martName}, 거리: ${distance.toInt()}m, " +
                          "설정반경: ${radiusInMeters}m, 포함여부: $inRadius")
                    inRadius
                }
                
                Log.d(TAG, "필터링 결과: ${cachedMarts.size}개 중 ${filteredMarts.size}개 " +
                         "(설정 반경: ${radiusInMeters}m 이내만 표시)")
                
                // 필터링 결과 상태 업데이트
                _uiState.update { currentState ->
                    when (currentState) {
                        is HomeUiState.Success -> currentState.copy(nearbyMarts = filteredMarts)
                        else -> currentState
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "마트 검색 오류: ${e.message}", e)
                _uiState.update { 
                    HomeUiState.Error("주변 마트 검색에 실패했습니다: ${e.message}")
                }
            }
        }
    }

    fun distanceInMeters(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val R = 6371000.0 // 지구 반지름(m)
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLng / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    /**
     * 검색어를 업데이트
     * 
     * @param query 새로운 검색어
     */
    fun updateSearchQuery(query: String) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(searchQuery = query)
                else -> currentState
            }
        }
    }

    /**
     * 현재 위치 정보를 업데이트
     * 
     * @param latitude 위도
     * @param longitude 경도
     */
    fun updateCurrentLocation(latitude: Double, longitude: Double) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(
                    currentLocation = Location(latitude, longitude)
                )
                else -> currentState
            }
        }
    }

    /**
     * 의뢰 목록을 업데이트
     * 
     * @param requests 새로운 의뢰 목록
     */
    fun updateRequests(requests: List<Request>) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(requests = requests)
                else -> currentState
            }
        }
    }

    /**
     * 반경 설정 다이얼로그를 표시
     */
    fun showRadiusDialog() {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(isRadiusDialogVisible = true)
                else -> currentState
            }
        }
    }

    /**
     * 반경 설정 다이얼로그를 숨김
     */
    fun hideRadiusDialog() {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(isRadiusDialogVisible = false)
                else -> currentState
            }
        }
    }

    /**
     * 검색 반경을 업데이트
     * 
     * @param radius 새로운 검색 반경 (km 단위)
     */
    fun updateSearchRadius(radius: Float) {
        val limitedRadius = radius.coerceIn(0.1f, 0.5f) // 0.1~0.5km(100~500m)로 제한
        Log.d(TAG, "반경 업데이트: ${limitedRadius}km (${(limitedRadius * 1000).toInt()}m)")
        
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> {
                    val newState = currentState.copy(searchRadius = limitedRadius)
                    
                    currentState.currentLocation?.let { location ->
                        // 위치 정보가 있으면 새 반경으로 마트 검색
                        searchNearbyMarts(location.latitude, location.longitude, limitedRadius)
                    }
                    
                    newState
                }
                else -> currentState
            }
        }
    }
} 