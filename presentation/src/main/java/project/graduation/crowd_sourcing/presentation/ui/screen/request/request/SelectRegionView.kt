package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.domain.model.entity.martsearch.MartEntity
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton

@Composable
fun SelectRegionView(
    navController: NavController,
    viewModel: SelectRegionViewModel = hiltViewModel()
) {
    val regionList = listOf(
        "강남구", "강동구", "강북구", "강서구", "관악구",
        "광진구", "구로구", "금천구", "노원구", "도봉구",
        "동대문구", "동작구", "마포구", "서대문구", "서초구",
        "성동구", "성북구", "송파구", "양천구", "영등포구",
        "용산구", "은평구", "종로구", "중구", "중랑구"
    )

    val selectedRegions by viewModel.selectedRegions.collectAsState()
    val selectedMarts by viewModel.selectedMarts.collectAsState()
    val martList by viewModel.martList.collectAsState()

    val prefillRegions = navController.previousBackStackEntry
        ?.savedStateHandle?.get<List<String>>("selectedRegions")
    val prefillMarts = navController.previousBackStackEntry
        ?.savedStateHandle?.get<List<MartEntity>>("selectedMarts")

    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (!prefillRegions.isNullOrEmpty() || !prefillMarts.isNullOrEmpty()) {
            viewModel.prefillSelections(
                regions = prefillRegions ?: emptyList(),
                marts = prefillMarts?.map { it.martName } ?: emptyList()
            )
        }
    }

    Scaffold(
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // 바텀 여백 유지
                contentAlignment = Alignment.Center
            ) {
                ConfirmButton(
                    text = "선택 완료",
                    enabled = selectedRegions.isNotEmpty() && selectedMarts.isNotEmpty(),
                    onConfirm = {
                        val selectedRegion = selectedRegions.firstOrNull()
                        if (selectedRegion != null) {
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selectedRegion", selectedRegion) // ✅ 단일 값으로 저장!
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("selectedMarts_prefill", selectedMarts) // ✅ 수정된 키로 저장!
                        }

                        navController.popBackStack()

                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {

            // 지역 선택 영역 (상단 1/2)
            Text("지역 선택", style = MaterialTheme.typography.titleMedium)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(regionList) { region ->
                    val isSelected = region in selectedRegions
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF1785E4) else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(60.dp)
                            .clickable { viewModel.onRegionToggle(region) }
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = region,
                                color = if (isSelected) Color.White else Color.Black,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 마트 선택 영역 (하단 1/2, 일단 빈 박스만)
            Text("마트 선택", style = MaterialTheme.typography.titleMedium)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(martList) { mart ->
                    val isSelected = selectedMarts.contains(mart.martName)
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF1785E4) else Color(0xFFE0E0E0)
                        ),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .height(60.dp)
                            .clickable { viewModel.onMartToggle(mart.martName) }
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Text(
                                text = mart.martName,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
