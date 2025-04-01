package project.graduation.crowd_sourcing.presentation.ui.screen.my

import android.opengl.Visibility
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MyViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState.init())
    val uiState = _uiState.asStateFlow()

    fun setDialogVisibility(visibility: Boolean){
        _uiState.update { prev->
            prev.copy(isDialogVisible = visibility)
        }
    }
}