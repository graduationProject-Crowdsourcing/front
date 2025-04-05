package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.ui.screen.search.SearchUiState
import project.graduation.crowd_sourcing.presentation.ui.screen.search.SearchViewModel

/**
 * 검색 컨텐츠 섹션 (검색창, 위치, 카테고리, 검색 버튼)
 *
 * @param state 현재 UI 상태
 * @param viewModel 검색 화면 ViewModel
 * @param keyboardController 키보드 제어를 위한 컨트롤러
 * @param focusManager 포커스 제어를 위한 매니저
 * @param modifier 컴포넌트에 적용할 추가 수정자
 */
@Composable
fun SearchContentSection(
    state: SearchUiState.Success,
    viewModel: SearchViewModel,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        
        // 검색 안내 텍스트
        Text(
            text = "검색어",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // 검색창
        SearchInputField(
            placeholder = "검색어를 입력해주세요",
            searchQuery = state.searchQuery,
            onSearchQueryChange = { viewModel.updateSearchQuery(it) },
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
                viewModel.performSearch()
            },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 지역 선택 섹션
        RegionSelectionSection(
            regions = state.regions,
            selectedRegion = state.selectedRegion,
            onRegionSelected = { viewModel.selectRegion(it) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 카테고리 선택 섹션
        CategorySelectionSection(
            categories = state.categories,
            selectedCategory = state.selectedCategory,
            onCategorySelected = { viewModel.selectCategory(it) }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 검색하기 버튼
        OutlinedButton(
            onClick = { 
                keyboardController?.hide()
                focusManager.clearFocus()
                viewModel.performSearch()
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color(0xFF1785E4)),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF1785E4)
            )
        ) {
            Text(
                text = "검색하기",
                fontSize = 16.sp
            )
        }
    }
} 