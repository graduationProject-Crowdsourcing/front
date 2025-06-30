package project.graduation.crowd_sourcing.presentation.ui.screen.request.work

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.domain.model.Category
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.usecase.WorkerUseCase
import javax.inject.Inject

// 작업 제출 버튼 클릭 시 등장 - 작업 리스트 페이지 뷰모델
data class Work(
    val id: Int,
    val title: String,
    val place: Region,
    val reward: Int,
    val martName:String,
    val category: Category
){
//    companion object{
//        fun test(): List<Work> = listOf(
//            Work(97, "a", Region.DONGDAEMUN, 7),
//            Work(97, "a", Region.DONGDAEMUN, 7),
//            Work(97, "a", Region.DONGDAEMUN, 7),
//            Work(97, "a", Region.DONGDAEMUN, 7),
//        )
//    }
}

@HiltViewModel
class WorkListViewModel @Inject constructor(
   private val workerUseCase: WorkerUseCase
) : ViewModel() {

    var workList by mutableStateOf(emptyList<Work>())

    fun getWorkList() = viewModelScope.launch {
        workerUseCase.getWorkOngoingList().onSuccess {
            workList = it.map{
                Work(
                    id = it.id,
                    title = it.commission,
                    place = it.commissionRegion,
                    reward = it.commissionPoint,
                    martName = it.martName,
                    category = it.category
                )
            }
        }.onFailure {
            it.printStackTrace()
        }
    }


}