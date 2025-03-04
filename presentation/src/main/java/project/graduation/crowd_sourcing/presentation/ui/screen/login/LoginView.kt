package project.graduation.crowd_sourcing.presentation.ui.screen.login

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginView(){
    val viewModel: LoginViewModel = hiltViewModel()

    Text(text = "login screen")
}