package project.graduation.crowd_sourcing.presentation.ui.screen.home

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.common.GoogleApiAvailability
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.MapSection
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.RadiusButton
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.RadiusSettingDialog
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.RequestsSection
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.SearchSection
import project.graduation.crowd_sourcing.presentation.ui.theme.CrowdSourcingTheme

// TODO: Domain Layer 구현 필요
// 1. Google Maps 관련 UseCase 구현
//    - CheckGoogleMapsAvailabilityUseCase: Google Maps API 사용 가능 여부 확인
//    - GetCurrentLocationUseCase: 현재 위치 정보 가져오기
//    - GetNearbyPlacesUseCase: 주변 장소 검색
// 2. Location 관련 도메인 모델 및 매퍼 구현
//    - Location 도메인 모델 정의
//    - LocationMapper: Location <-> LatLng 변환 로직
// 3. Repository 인터페이스 정의
//    - LocationRepository: 위치 관련 데이터 처리
//    - PlacesRepository: 장소 검색 관련 데이터 처리

// 변경 내역:
// 1. 상태 관리 개선
//    - sealed class를 사용한 상태 관리에 맞춰 UI 로직 수정
//    - when 표현식을 사용한 상태별 UI 처리
// 2. 에러 처리 추가
//    - 에러 상태에 대한 UI 처리 추가
//    - 에러 메시지 표시
// 3. 로딩 상태 처리
//    - 로딩 상태에 대한 UI 처리 추가
//    - CircularProgressIndicator 표시
// 4. 컴포넌트 구조 개선
//    - 상태에 따른 조건부 렌더링
//    - 컴포넌트 간 일관된 스타일 적용
// 5. Kakao Map 지원 추가
//    - 구글맵 또는 카카오맵 중 선택하여 사용 가능

/**
 * 홈 화면 UI 구성
 * 
 * 주요 기능:
 * 1. 네이버 맵을 사용한 지도 표시
 * 2. 사용자 현재 위치 표시
 * 3. 주변 의뢰 목록 표시
 * 4. 검색 반경 설정
 * 5. 검색 기능
 */
@SuppressLint("RememberReturnType")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeView() {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    
    // 화면 콘텐츠 표시 여부를 제어하는 상태
    val showContent = remember { mutableStateOf(true) }
    
    // 화면 생명주기에 따라 콘텐츠 관리
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> {
                    // 화면이 백그라운드로 이동할 때 모든 콘텐츠를 숨김
                    showContent.value = false
                }
                Lifecycle.Event.ON_RESUME -> {
                    // 화면이 포그라운드로 돌아올 때 모든 콘텐츠를 다시 표시
                    showContent.value = true
                }
                else -> {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)
        
        onDispose {
            // 컴포지션이 해제될 때 옵저버 제거
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(Unit) {
        locationPermissionState.launchPermissionRequest()
    }
    
    // TODO: Data Layer 구현 후 UseCase로 이동 필요
    // Google Maps는 사용하지 않지만 참조용으로 코드 남겨둠
    val isMapServiceAvailable = remember {
        val availability = GoogleApiAvailability.getInstance()
        val resultCode = availability.isGooglePlayServicesAvailable(context)
        resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val state = uiState.value) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message)
                }
            }
            is HomeUiState.Success -> {
                // showContent 상태에 따라 모든 콘텐츠 조건부 렌더링
                if (showContent.value) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        item { 
                            Box {
                                // 맵 표시
                                MapSection(isMapServiceAvailable = isMapServiceAvailable, state = state)

                                // 현재 위치정보가 있으면 반경 버튼 표시
                                if (state.currentLocation != null) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp),
                                        contentAlignment = Alignment.TopCenter
                                    ) {
                                        RadiusButton(
                                            radius = state.searchRadius,
                                            onClick = viewModel::showRadiusDialog,
                                            modifier = Modifier.zIndex(1f)
                                        )
                                    }
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))
                            SearchSection(
                                searchQuery = state.searchQuery, 
                                onSearchQueryChange = viewModel::updateSearchQuery,
                                requests = state.requests
                            ) 
                        }
                        item {
                            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.space_medium)))
                            RequestsSection(viewModel = viewModel, state = state)
                        }
                    }

                    if (state.isRadiusDialogVisible) {
                        RadiusSettingDialog(
                            currentRadius = state.searchRadius,
                            onRadiusChange = viewModel::updateSearchRadius,
                            onDismiss = viewModel::hideRadiusDialog
                        )
                    }
                } else {
                    // 화면 전환 중일 때는 빈 화면 표시
                    Box(modifier = Modifier.fillMaxSize())
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview(){
    CrowdSourcingTheme{
        HomeView()
    }
}
