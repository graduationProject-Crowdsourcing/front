package project.graduation.crowd_sourcing.presentation.ui.screen.search.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox

/**
 * 검색어 입력 섹션
 *
 * @param searchQuery 검색어
 * @param onSearchQueryChange 검색어 변경 콜백
 * @param keyboardController 키보드 컨트롤러
 * @param focusManager 포커스 매니저
 * @param modifier 컴포넌트에 적용할 추가 수정자
 */
@Composable
fun SearchInputSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
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
        EditTextBox(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeHolder = "검색어를 입력해주세요",
            modifier = Modifier.fillMaxWidth()
        )
    }
} 