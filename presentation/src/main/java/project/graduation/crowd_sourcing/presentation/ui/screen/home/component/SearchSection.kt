package project.graduation.crowd_sourcing.presentation.ui.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.component.ConfirmButton
import project.graduation.crowd_sourcing.presentation.ui.component.EditTextBox
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Request
import project.graduation.crowd_sourcing.presentation.ui.screen.home.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    requests: List<Request>,
    viewModel: HomeViewModel? = null
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "마트 검색",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        EditTextBox(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeHolder = "마트 이름을 입력하세요"
        )

        Spacer(Modifier.height(dimensionResource(R.dimen.space_small)))
        ConfirmButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = "검색",
            onConfirm = { 
                if (searchQuery.isNotBlank()) {
                    viewModel?.searchMartsByKeyword(searchQuery)
                } else {
                    showDialog = true
                }
            }
        )
    }

    if (showDialog) {
        SearchResultDialog(
            requests = requests.filter {
                it.place.contains(searchQuery, ignoreCase = true) ||
                        it.title.contains(searchQuery, ignoreCase = true)
            },
            onDismiss = { showDialog = false },
            searchQuery = searchQuery
        )
    }
}

@Preview
@Composable
fun SearchSectionPrev() {
    SearchSection(
        onSearchQueryChange = { },
        searchQuery = "",
        requests = emptyList()
    )
}