package project.graduation.crowd_sourcing.presentation.ui.screen.withdraw

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.usecase.MemberUseCase
import javax.inject.Inject

@HiltViewModel
class WithdrawConfirmViewModel @Inject constructor(
    private val memberUseCase: MemberUseCase
) : ViewModel() {

    var withdrawSuccess by mutableStateOf(false)
        private set

    fun withdraw() {
        viewModelScope.launch {
            memberUseCase.withdraw()
                .onSuccess {
                    withdrawSuccess = true
                }
                .onFailure {
                    Log.e("Withdraw", "❌ 회원 탈퇴 실패: ${it.message}")
                }
        }
    }
}
