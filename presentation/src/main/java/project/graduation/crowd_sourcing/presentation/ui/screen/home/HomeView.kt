package project.graduation.crowd_sourcing.presentation.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.maps.model.LatLng
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.CurrentRequestsList
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.MapSection
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.RequestsSection
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.SearchSection
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.RadiusButton
import project.graduation.crowd_sourcing.presentation.ui.screen.home.component.RadiusSettingDialog
import project.graduation.crowd_sourcing.presentation.ui.theme.CrowdSourcingTheme
import project.graduation.crowd_sourcing.presentation.utils.spaceMedium

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

@Composable
fun HomeView() {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // TODO: Data Layer 구현 후 UseCase로 이동 필요
    val isGoogleMapsAvailable = remember {
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(spaceMedium())
                ) {
                    item { 
                        Box {
                            MapSection(isGoogleMapsAvailable, state)

                            // 구글 맵스 사용 가능하고 현재 위치정보가 있으면 반경 버튼 표시
                            // 구글 맵스 사용 불가능하면 반경 버튼 표시 안함 => 테스트 하고 싶으면 if문 없애고 반경 버튼 표시
                            if(isGoogleMapsAvailable && state.currentLocation != null){
                                Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = spaceMedium()),
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
                        SearchSection(
                            searchQuery = state.searchQuery, 
                            onSearchQueryChange = viewModel::updateSearchQuery,
                            requests = state.requests
                        ) 
                    }
                    item {
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
            }
        }
    }
}

// TODO: Domain Layer로 이동 필요
// - Location 데이터 모델 변환 로직을 Domain Layer의 mapper로 분리
// - 현재는 Presentation Layer에서 직접 처리하고 있음
fun Location.toLatLng(): LatLng {
    return LatLng(this.latitude, this.longitude)
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview(){
    CrowdSourcingTheme{
        HomeView()
    }
}
