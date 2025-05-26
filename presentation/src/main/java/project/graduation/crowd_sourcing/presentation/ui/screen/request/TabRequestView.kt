package project.graduation.crowd_sourcing.presentation.ui.screen.request

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import project.graduation.crowd_sourcing.presentation.R
import project.graduation.crowd_sourcing.presentation.ui.navigation.Screen
import project.graduation.crowd_sourcing.presentation.ui.screen.request.component.TabRequestButton

@Composable
fun TabRequestView(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "의뢰",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TabRequestButton(
            title = "의뢰 작성",
            iconResId = R.drawable.ic_star,
            onClick = { navController.navigate(Screen.RequestFormScreen.route) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        TabRequestButton(
            title = "작업 제출",
            iconResId = R.drawable.ic_star,
            onClick = { navController.navigate(Screen.WorkListScreen.route) }
        )
    }
}

/*
@Composable
@Preview(showBackground = true)
fun RequestViewPreview() {
    val navController = rememberNavController()

    TabRequestView(navController = navController)
}
*/

