package project.graduation.crowd_sourcing.presentation.ui.screen.request.request

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton

@Composable
fun SelectRegionView(
    navController: NavController,
    onConfirmSelection: (List<String>) -> Unit
) {
    val regionList = listOf(
        "강남구", "강동구", "강북구", "강서구", "관악구",
        "광진구", "구로구", "금천구", "노원구", "도봉구",
        "동대문구", "동작구", "마포구", "서대문구", "서초구",
        "성동구", "성북구", "송파구", "양천구", "영등포구",
        "용산구", "은평구", "종로구", "중구", "중랑구"
    )

    val selectedRegions = remember { mutableStateListOf<String>() }

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
                    enabled = selectedRegions.isNotEmpty(),
                    onConfirm = {
                        onConfirmSelection(selectedRegions.toList())
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
                            .clickable {
                                if (isSelected) {
                                    selectedRegions.remove(region)
                                } else {
                                    selectedRegions.add(region)
                                }
                            }
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray)
            ) {
                // 마트 리스트 추가는 다음 단계에서
                Text(
                    text = "해당 지역 마트를 불러오는 공간입니다.",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
