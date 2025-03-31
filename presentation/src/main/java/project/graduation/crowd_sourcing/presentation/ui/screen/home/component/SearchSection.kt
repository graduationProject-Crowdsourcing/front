package project.graduation.crowd_sourcing.presentation.ui.screen.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import project.graduation.crowd_sourcing.presentation.ui.screen.home.Request

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(
    searchQuery: String, 
    onSearchQueryChange: (String) -> Unit,
    requests: List<Request>
) {
    var showDialog by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Text(
            text = "장소 검색",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("여기에 장소를 입력하세요") },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1785E4),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "검색", fontSize = 16.sp)
        }
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