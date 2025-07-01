package project.graduation.crowd_sourcing.presentation.ui.screen.my

import android.net.Uri
import android.opengl.Visibility
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.local.TokenManager
import project.graduation.crowd_sourcing.domain.usecase.HistoryUseCase
import project.graduation.crowd_sourcing.domain.usecase.MemberUseCase
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

    fun putNickname(nickname: String) = viewModelScope.launch {
        myUseCase.putNickname(nickname)
            .onSuccess {
                _uiState.update { prev ->
                    prev.copy(
                        nickname = nickname,
                        isDialogVisible = false
                    )
                }
            }.onFailure {

            }
    }

    fun getRecentHistory() = viewModelScope.launch {
        myUseCase.getRecentHistory()
            .onSuccess { (work, commission) ->
                val recentWork = MyUiState.RecentListItem(
                    name = work.item,
                    id = work.id
                )

                val recentCommission = MyUiState.RecentListItem(
                    name = commission.commission,
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
                e.printStackTrace()
            }
    }

    fun changeImg(uri: Uri) = viewModelScope.launch {
        myUseCase.changeMyProfileImage(uri, uiState.value.profileImage).onSuccess {
            _uiState.update { prev ->
                prev.copy(
                    profileImage = it
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun loadProfile() = viewModelScope.launch {
        myUseCase.loadProfile().onSuccess {
            _uiState.update { prev ->
                prev.copy(
                    profileImage = it.second,
                    nickname = it.first.nickname,
                    point = it.first.point
                )
            }
        }.onFailure {

        }
    }
}