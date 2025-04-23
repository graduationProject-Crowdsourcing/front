package project.graduation.crowd_sourcing.presentation.ui.screen.alarm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class AlarmViewModel: ViewModel() {
    var isChecked by mutableStateOf(false)

    fun toggleChecked() {
        isChecked = !isChecked
    }
}