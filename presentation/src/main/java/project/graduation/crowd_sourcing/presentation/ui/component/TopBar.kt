package project.graduation.crowd_sourcing.presentation.ui.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(){
    TopAppBar(
        title = {
            Text(text = "example")
        },
        navigationIcon = {},
        actions = {}
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPrev(){
    TopBar()
}
