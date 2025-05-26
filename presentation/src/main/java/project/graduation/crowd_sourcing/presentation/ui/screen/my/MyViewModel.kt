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
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.usecase.HistoryUseCase
import project.graduation.crowd_sourcing.domain.usecase.MyUseCase
import project.graduation.crowd_sourcing.presentation.utils.getTimeAgo
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myUseCase: MyUseCase,
    private val historyUseCase: HistoryUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyUiState.init())
    val uiState = _uiState.asStateFlow()

    fun setDialogVisibility(visibility: Boolean) {
        _uiState.update { prev ->
            prev.copy(isDialogVisible = visibility)
        }
    }

    fun putNickname(nickname:String) = viewModelScope.launch {
        myUseCase.putNickname(nickname)
            .onSuccess {
                //성공 시 회원 정보 다시 가져와서 uiState에 업데이트. 아직 회원 정보 조회 기능 없어서 주석처리
            }.onFailure {

            }
    }

    fun getRecentHistory() = viewModelScope.launch {
        myUseCase.getRecentHistory()
            .onSuccess { (work, commission) ->
                val recentWork = MyUiState.RecentListItem(
                    name = work.item,
                    date = work.workDate,
                    id = work.id
                )

                val recentCommission = MyUiState.RecentListItem(
                    name = commission.commission,
                    date = commission.commissionDate,
                    id = commission.id
                )

                _uiState.update { prev ->
                    prev.copy(
                        recentWork = recentWork,
                        recentRequest = recentCommission
                    )
                }
            }
            .onFailure { e ->

            }
    }

}