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
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.domain.usecase.MartSearchUseCase
import project.graduation.crowd_sourcing.domain.usecase.HistoryUseCase
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
    private val martSearchUseCase: MartSearchUseCase,
    private val historyUseCase: HistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _nearbyMarts = MutableStateFlow<List<MartEntity>>(emptyList())
    val nearbyMarts: StateFlow<List<MartEntity>> = _nearbyMarts.asStateFlow()
    
    // 위치 정보 캐싱을 위한 변수
    private var lastLat: Double? = null
    private var lastLng: Double? = null
    private var lastRadius: Int? = null
    
    // 마트 데이터 캐싱을 위한 변수 추가
    private var cachedMartEntities: List<MartEntity> = emptyList()
    
    companion object {
        private const val TAG = "HomeViewModel"
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
        Log.d(TAG, "=== 현재 위치 정보 가져오기 시작 ===")
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        
        try {
            val hasFineLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            val hasCoarseLocation = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            
            Log.d(TAG, "위치 권한 확인 - FINE_LOCATION: $hasFineLocation, COARSE_LOCATION: $hasCoarseLocation")
            
            if (hasFineLocation || hasCoarseLocation) {
                Log.d(TAG, "위치 권한 있음 - 위치 정보 요청")
                
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        
                        Log.d(TAG, "✅ 위치 정보 획득 성공: $latitude, $longitude")
                        
                        // 위치가 변경된 경우에만 업데이트 및 마트 검색
                        if (lastLat != latitude || lastLng != longitude) {
                            lastLat = latitude
                            lastLng = longitude
                            
                            Log.d(TAG, "🔄 위치 변경됨: 새로운 위치로 업데이트")
                            updateCurrentLocation(latitude, longitude)
                            
                            // 현재 반경 값으로 주변 마트 검색
                            val currentState = _uiState.value as? HomeUiState.Success
                            val radius = (currentState?.searchRadius ?: 0.1f).coerceIn(0.1f, 0.5f)
                            Log.d(TAG, "마트 검색 시작 - 반경: ${radius}km")
                            searchNearbyMarts(latitude, longitude, radius)
                            
                            // 위치 기반 추천의뢰 로드
                            Log.d(TAG, "🎯 위치 기반 추천의뢰 로드 시작")
                            loadLocationBasedRecommendedRequests(latitude, longitude)
                        } else {
                            Log.d(TAG, "위치 변경 없음: 마트 검색 스킵")
                            
                            // 위치가 변경되지 않았더라도 추천의뢰가 비어있다면 로드
                            val currentState = _uiState.value as? HomeUiState.Success
                            val recommendedCount = currentState?.recommendedRequests?.size ?: -1
                            Log.d(TAG, "현재 추천의뢰 개수: $recommendedCount")
                            
                            if (currentState?.recommendedRequests?.isEmpty() == true) {
                                Log.d(TAG, "🎯 추천의뢰가 비어있음: 위치 기반 추천의뢰 로드")
                                loadLocationBasedRecommendedRequests(latitude, longitude)
                            } else {
                                Log.d(TAG, "추천의뢰가 이미 존재함: 로드 스킵")
                            }
                        }
                    } else {
                        Log.w(TAG, "⚠️ 위치 정보가 null임")
                    }
                }.addOnFailureListener { exception ->
                    Log.e(TAG, "❌ 위치 정보 획득 실패: ${exception.message}", exception)
                }
            } else {
                Log.w(TAG, "⚠️ 위치 권한이 없음")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ 위치 정보 오류: ${e.message}", e)
            _uiState.update { 
                HomeUiState.Error("위치 정보를 가져오는데 실패했습니다: ${e.message}")
            }
        }
    }

    /**
     * 초기 데이터를 로드하여 상태를 설정
     * 위치 정보를 얻은 후 위치 기반 추천의뢰를 로드
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "=== 초기 데이터 로드 시작 ===")
                _uiState.update { 
                    HomeUiState.Success(
                        currentRequests = emptyList(),
                        recommendedRequests = emptyList() // 위치 정보를 얻은 후에 로드
                    )
                }
                Log.d(TAG, "초기 상태 설정 완료")
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
                val shouldRefreshData = lastLat != lat || lastLng != lng || cachedMartEntities.isEmpty()
                
                if (shouldRefreshData) {
                    Log.d(TAG, "위치 변경 또는 최초 로드: 새로운 마트 데이터 요청 ($lat, $lng)")
                    
                    // 위치 정보 캐싱
                    lastLat = lat
                    lastLng = lng
                    
                    // 서버에 고정 반경(500m)으로 API 요청
                    val apiRadius = 500.0
                    try {
                        val marts = martSearchUseCase.searchMartByLocation(lat, lng, apiRadius)
                        Log.d(TAG, "API 응답: ${marts.size}개 마트 로드 성공")
                        
                        // 마트 데이터 캐싱
                        cachedMartEntities = marts
                    } catch (e: Exception) {
                        Log.e(TAG, "API 호출 실패: ${e.message}", e)
                        // API 호출 실패 시 캐시된 데이터 유지
                    }
                } else {
                    Log.d(TAG, "캐시된 마트 데이터 사용 (마트 ${cachedMartEntities.size}개)")
                }
                
                // 반경은 변경되었을 수 있으므로 매번 필터링
                val filteredMarts = cachedMartEntities.filter { mart ->
                    val distance = distanceInMeters(lat, lng, mart.latitude, mart.longitude)
                    val inRadius = distance <= radiusInMeters
                    Log.d(TAG, "마트: ${mart.martName}, 거리: ${distance.toInt()}m, " +
                          "설정반경: ${radiusInMeters}m, 포함여부: $inRadius")
                    inRadius
                }
                
                Log.d(TAG, "필터링 결과: ${cachedMartEntities.size}개 중 ${filteredMarts.size}개 " +
                         "(설정 반경: ${radiusInMeters}m 이내만 표시)")
                
                // 필터링 결과 상태 업데이트
                _uiState.update { currentState ->
                    when (currentState) {
                        is HomeUiState.Success -> currentState.copy(nearbyMartEntities = filteredMarts)
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
     * 현재 작업중인 의뢰 목록을 업데이트
     * 
     * @param requests 새로운 현재 작업중인 의뢰 목록
     */
    fun updateCurrentRequests(requests: List<Request>) {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(currentRequests = requests)
                else -> currentState
            }
        }
    }

    /**
     * 추천 의뢰 목록을 업데이트
     * 
     * @param requests 새로운 추천 의뢰 목록
     */
    fun updateRecommendedRequests(requests: List<Request>) {
        Log.d(TAG, "📝 추천의뢰 상태 업데이트 - 개수: ${requests.size}")
        requests.forEachIndexed { index, request ->
            Log.d(TAG, "   ${index + 1}. ${request.title} (${request.reward}원)")
        }
        
        val oldState = _uiState.value
        Log.d(TAG, "이전 상태: ${oldState::class.simpleName}")
        
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> {
                    val newState = currentState.copy(recommendedRequests = requests)
                    Log.d(TAG, "✅ Success 상태 업데이트 완료")
                    newState
                }
                else -> {
                    Log.w(TAG, "⚠️ Success 상태가 아니므로 업데이트 불가: ${currentState::class.simpleName}")
                    currentState
                }
            }
        }
        
        // 업데이트 후 검증
        val newState = _uiState.value
        when (newState) {
            is HomeUiState.Success -> {
                Log.d(TAG, "🔍 업데이트 검증 - 현재 추천의뢰 개수: ${newState.recommendedRequests.size}")
            }
            else -> {
                Log.e(TAG, "❌ 업데이트 검증 실패 - 현재 상태: ${newState::class.simpleName}")
            }
        }
    }

    /**
     * 현재 작업중인 의뢰 목록을 가져오는 함수
     * HistoryUseCase의 getRequest를 사용하여 현재 의뢰 목록을 가져옴
     */
    fun loadCurrentRequests() {
        viewModelScope.launch {
            try {
                Log.d(TAG, "현재 의뢰 목록 로드 시작")
                
                val result = historyUseCase.getRequest()
                result.onSuccess { historyStats ->
                    // currentList를 Request 형태로 변환
                    val currentRequests = historyStats.currentList.map { workHistory ->
                        Request(
                            id = workHistory.id.toString(),
                            title = workHistory.commission,
                            location = Location(0.0, 0.0), // 위치 정보가 없어서 기본값 사용
                            place = workHistory.commissionRegion,
                            reward = workHistory.commissionPoint
                        )
                    }
                    
                    Log.d(TAG, "현재 의뢰 목록 로드 성공: ${currentRequests.size}개")
                    updateCurrentRequests(currentRequests)
                }.onFailure { exception ->
                    Log.e(TAG, "현재 의뢰 목록 로드 실패: ${exception.message}", exception)
                    updateCurrentRequests(emptyList())
                }
            } catch (e: Exception) {
                Log.e(TAG, "현재 의뢰 목록 로드 중 오류 발생: ${e.message}", e)
                updateCurrentRequests(emptyList())
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
                        // 위치 정보가 있으면 새 반경으로 마트 검색 및 추천의뢰 다시 로드
                        searchNearbyMarts(location.latitude, location.longitude, limitedRadius)
                        loadLocationBasedRecommendedRequests(location.latitude, location.longitude)
                    }
                    
                    newState
                }
                else -> currentState
            }
        }
    }

    /**
     * 키워드로 마트 검색
     * 
     * @param keyword 검색할 키워드
     */
    fun searchMartsByKeyword(keyword: String) {
        if (keyword.isBlank()) return
        
        viewModelScope.launch {
            try {
                Log.d(TAG, "마트 키워드 검색 시작: $keyword")
                
                // 키워드로 검색 (radius 0.0으로 전달하여 서버 디폴트 값 사용)
                val searchResults = martSearchUseCase.searchMartByKeyword(keyword, 600.0)
                
                // 현재 위치 정보 가져오기
                val currentState = _uiState.value as? HomeUiState.Success
                val currentLocation = currentState?.currentLocation
                
                // 검색 키워드와의 유사도 순으로 정렬, 점수가 같으면 거리 순으로 정렬
                val sortedResults = searchResults.sortedWith(compareByDescending<MartEntity> { mart ->
                    calculateRelevanceScore(mart.martName, keyword)
                }.thenBy { mart ->
                    // 점수가 같으면 현재 위치에서 가까운 순으로 정렬
                    currentLocation?.let { location ->
                        distanceInMeters(location.latitude, location.longitude, mart.latitude, mart.longitude)
                    } ?: Double.MAX_VALUE // 현재 위치가 없으면 가장 뒤로
                })
                
                Log.d(TAG, "마트 검색 결과: ${searchResults.size}개 (유사도 순 + 거리 순 정렬 완료)")
                
                _uiState.update { currentState ->
                    when (currentState) {
                        is HomeUiState.Success -> currentState.copy(
                            searchedMarts = sortedResults,
                            isSearchResultDialogVisible = true
                        )
                        else -> currentState
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "마트 검색 오류: ${e.message}", e)
                _uiState.update { currentState ->
                    when (currentState) {
                        is HomeUiState.Success -> currentState.copy(
                            searchedMarts = emptyList(),
                            isSearchResultDialogVisible = true
                        )
                        else -> currentState
                    }
                }
            }
        }
    }

    /**
     * 검색 키워드와 마트 이름의 유사도 점수 계산
     * 점수가 높을수록 더 관련성이 높음
     */
    private fun calculateRelevanceScore(martName: String, keyword: String): Int {
        val lowerMartName = martName.lowercase()
        val lowerKeyword = keyword.lowercase()
        
        var score = 0
        
        // 1. 완전 일치 (가장 높은 점수)
        if (lowerMartName == lowerKeyword) {
            score += 100
        }
        
        // 2. 시작 부분 일치
        if (lowerMartName.startsWith(lowerKeyword)) {
            score += 50
        }
        
        // 3. 포함 여부
        if (lowerMartName.contains(lowerKeyword)) {
            score += 30
        }
        
        // 4. 키워드가 마트 이름에 포함된 비율
        val containsRatio = lowerKeyword.length.toFloat() / lowerMartName.length.toFloat()
        score += (containsRatio * 20).toInt()
        
        // 5. 각 글자별 일치도 (부분 일치)
        var charMatches = 0
        for (char in lowerKeyword) {
            if (lowerMartName.contains(char)) {
                charMatches++
            }
        }
        score += (charMatches.toFloat() / lowerKeyword.length * 10).toInt()
        
        return score
    }

    /**
     * 검색 결과 다이얼로그를 표시
     */
    fun showSearchResultDialog() {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(isSearchResultDialogVisible = true)
                else -> currentState
            }
        }
    }

    /**
     * 검색 결과 다이얼로그를 숨김
     */
    fun hideSearchResultDialog() {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(
                    isSearchResultDialogVisible = false,
                    searchedMarts = emptyList()
                )
                else -> currentState
            }
        }
    }

    /**
     * 마트 클릭 시 해당 마트의 의뢰 다이어로그를 표시
     * 
     * @param mart 클릭된 마트 정보
     */
    fun onMartClicked(mart: MartEntity) {
        viewModelScope.launch {
            try {
                // 실제 usecase를 사용하여 마트 의뢰 정보 조회
                val martWorkEntities = martSearchUseCase.searchWorkByMartName(mart.martName)
                
                // MartWorkEntity를 Request로 변환
                val martRequests = martWorkEntities.map { workEntity ->
                    Request(
                        id = workEntity.id.toString(),
                        title = "${mart.martName} - ${workEntity.work}",
                        location = Location(mart.latitude, mart.longitude),
                        place = mart.martName,
                        reward = workEntity.workpoint
                    )
                }
                
                _uiState.update { currentState ->
                    when (currentState) {
                        is HomeUiState.Success -> currentState.copy(
                            selectedMart = mart,
                            selectedMartRequests = martRequests,
                            isMartRequestDialogVisible = true
                        )
                        else -> currentState
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "마트 의뢰 조회 실패: ${e.message}", e)
                // 에러 발생 시 빈 목록으로 처리
                _uiState.update { currentState ->
                    when (currentState) {
                        is HomeUiState.Success -> currentState.copy(
                            selectedMart = mart,
                            selectedMartRequests = emptyList(),
                            isMartRequestDialogVisible = true
                        )
                        else -> currentState
                    }
                }
            }
        }
    }

    /**
     * 마트 의뢰 다이어로그를 숨김
     */
    fun hideMartRequestDialog() {
        _uiState.update { currentState ->
            when (currentState) {
                is HomeUiState.Success -> currentState.copy(
                    selectedMart = null,
                    selectedMartRequests = emptyList(),
                    isMartRequestDialogVisible = false
                )
                else -> currentState
            }
        }
    }

    /**
     * 사용자 위치 기반으로 추천의뢰를 로드
     * 
     * @param lat 사용자 위도
     * @param lng 사용자 경도
     */
    private fun loadLocationBasedRecommendedRequests(lat: Double, lng: Double) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "=== 위치 기반 추천의뢰 로드 시작 ===")
                Log.d(TAG, "위치: ($lat, $lng)")
                
                // 1. 현재 설정된 반경으로 주변 마트 검색
                val currentState = _uiState.value as? HomeUiState.Success
                val radiusInKm = currentState?.searchRadius ?: 0.5f
                val radiusInMeters = (radiusInKm * 1000).toDouble().coerceIn(100.0, 500.0) // 100m~500m로 제한
                
                Log.d(TAG, "1단계: 주변 마트 검색 - 반경: ${radiusInKm}km (${radiusInMeters}m)")
                
                val nearbyMarts = martSearchUseCase.searchMartByLocation(lat, lng, radiusInMeters)
                Log.d(TAG, "주변 마트 검색 결과: ${nearbyMarts.size}개")
                
                if (nearbyMarts.isEmpty()) {
                    Log.w(TAG, "주변에 마트가 없습니다")
                    updateRecommendedRequests(emptyList())
                    return@launch
                }
                
                // 마트 목록 상세 로그
                nearbyMarts.forEachIndexed { index, mart ->
                    val distance = distanceInMeters(lat, lng, mart.latitude, mart.longitude)
                    Log.d(TAG, "   마트 ${index + 1}: ${mart.martName} (의뢰: ${mart.existCommission}개, 거리: ${distance.toInt()}m)")
                }
                
                // 2. 의뢰가 있는 마트만 필터링
                Log.d(TAG, "2단계: 의뢰가 있는 마트 필터링")
                val martsWithCommission = nearbyMarts.filter { mart ->
                    val hasCommission = mart.existCommission > 0
                    Log.d(TAG, "   ${mart.martName}: 의뢰 ${mart.existCommission}개 - ${if (hasCommission) "포함" else "제외"}")
                    hasCommission
                }
                Log.d(TAG, "의뢰가 있는 마트: ${martsWithCommission.size}개")
                
                if (martsWithCommission.isEmpty()) {
                    Log.w(TAG, "주변에 의뢰가 있는 마트가 없습니다")
                    updateRecommendedRequests(emptyList())
                    return@launch
                }
                
                // 3. 사용자와 가까운 순으로 정렬
                Log.d(TAG, "🔍 3단계: 거리순 정렬")
                val sortedMarts = martsWithCommission.sortedBy { mart ->
                    distanceInMeters(lat, lng, mart.latitude, mart.longitude)
                }
                
                sortedMarts.forEachIndexed { index, mart ->
                    val distance = distanceInMeters(lat, lng, mart.latitude, mart.longitude)
                    Log.d(TAG, "   ${index + 1}번째: ${mart.martName} (거리: ${distance.toInt()}m)")
                }
                
                // 4. 각 마트에서 의뢰를 가져와서 추천의뢰 생성
                Log.d(TAG, "🔍 4단계: 각 마트별 의뢰 가져오기")
                val recommendedRequests = mutableListOf<Request>()
                val usedWorkIds = mutableSetOf<Int>() // 중복 의뢰 방지용
                
                for ((index, mart) in sortedMarts.withIndex()) {
                    if (recommendedRequests.size >= 2) {
                        Log.d(TAG, "   최대 개수 (2개) 도달로 중단")
                        break
                    }
                    
                    Log.d(TAG, "   마트 ${index + 1} - ${mart.martName} 의뢰 조회 중...")
                    
                    try {
                        // 해당 마트의 의뢰 목록 가져오기
                        val martWorks = martSearchUseCase.searchWorkByMartName(mart.martName)
                        Log.d(TAG, "     API 응답: ${martWorks.size}개 의뢰")
                        
                        if (martWorks.isEmpty()) {
                            Log.w(TAG, "     ${mart.martName}에 의뢰가 없음")
                            continue
                        }
                        
                        // 의뢰 목록 상세 로그
                        martWorks.forEachIndexed { workIndex, work ->
                            val isUsed = usedWorkIds.contains(work.id)
                            Log.d(TAG, "       의뢰 ${workIndex + 1}: ID=${work.id}, ${work.work} (${work.workpoint}원) - ${if (isUsed) "이미 사용됨" else "사용 가능"}")
                        }
                        
                        // 아직 사용하지 않은 의뢰 중에서 첫 번째 의뢰만 선택
                        val availableWork = martWorks.firstOrNull { work ->
                            !usedWorkIds.contains(work.id)
                        }
                        
                        if (availableWork != null) {
                            val request = Request(
                                id = availableWork.id.toString(),
                                title = "${mart.martName} - ${availableWork.work}",
                                location = Location(mart.latitude, mart.longitude),
                                place = mart.martName,
                                reward = availableWork.workpoint
                            )
                            
                            recommendedRequests.add(request)
                            usedWorkIds.add(availableWork.id)
                            
                            Log.d(TAG, "     추천의뢰 추가: ${request.title}, 보상: ${request.reward}원")
                        } else {
                            Log.w(TAG, "     사용 가능한 의뢰가 없음")
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "     마트 ${mart.martName}의 의뢰 조회 실패: ${e.message}", e)
                        continue
                    }
                }
                
                Log.d(TAG, "=== 추천의뢰 로드 완료: ${recommendedRequests.size}개 ===")
                if (recommendedRequests.isNotEmpty()) {
                    recommendedRequests.forEachIndexed { index, request ->
                        Log.d(TAG, "   추천${index + 1}: ${request.title} (${request.reward}원)")
                    }
                } else {
                    Log.w(TAG, "최종 추천의뢰가 0개입니다")
                }
                
                // 5. 상태 업데이트
                Log.d(TAG, "상태 업데이트 중...")
                updateRecommendedRequests(recommendedRequests)
                
                // 업데이트 후 상태 확인
                val updatedState = _uiState.value as? HomeUiState.Success
                val updatedCount = updatedState?.recommendedRequests?.size ?: -1
                Log.d(TAG, "상태 업데이트 완료 - 최종 추천의뢰 개수: $updatedCount")
                
            } catch (e: Exception) {
                Log.e(TAG, "위치 기반 추천의뢰 로드 실패: ${e.message}", e)
                e.printStackTrace()
                // 실패 시 빈 목록으로 설정
                updateRecommendedRequests(emptyList())
            }
        }
    }


} 