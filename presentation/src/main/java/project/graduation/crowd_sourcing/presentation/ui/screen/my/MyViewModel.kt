package project.graduation.crowd_sourcing.presentation.ui.screen.my

import android.opengl.Visibility
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.MyUseCase
import project.graduation.crowd_sourcing.presentation.utils.getTimeAgo
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myUseCase: MyUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState.init())
    val uiState = _uiState.asStateFlow()

    fun setDialogVisibility(visibility: Boolean) {
        _uiState.update { prev ->
            prev.copy(isDialogVisible = visibility)
        }
    }

    fun getRecentHistory(userId: Int) = viewModelScope.launch {
        myUseCase.getRecentHistory(userId).let {
            val recentWork = it.first.let {
                MyUiState.RecentListItem(
                    name = it.item,
                    date = it.workDate
                )
            }
            val recentCommission = it.second.let {
                MyUiState.RecentListItem(
                    name = it.commission,
                    date = it.commissionDate
                )
            }

            _uiState.update { prev->
                prev.copy(
                    recentWork = recentWork,
                    recentRequest = recentCommission
                )
            }
        }
    }
}