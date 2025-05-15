package project.graduation.crowd_sourcing.app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.graduation.crowd_sourcing.data.response.worker.WorkCountEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHistoryEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkHourEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkMostEntity
import project.graduation.crowd_sourcing.data.response.worker.WorkPointEntity
import project.graduation.crowd_sourcing.domain.model.Region
import project.graduation.crowd_sourcing.domain.model.WorkStatus
import project.graduation.crowd_sourcing.domain.repository.MyRepository
import project.graduation.crowd_sourcing.domain.repository.StatisticsRepository
import project.graduation.crowd_sourcing.domain.repository.UserPointRepository
import project.graduation.crowd_sourcing.domain.repository.WorkerRepository
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(
    private val myRepository: MyRepository,
    private val statisticsRepository: StatisticsRepository,
    private val userPointHistoryRepository: UserPointRepository,
    private val workerRepository: WorkerRepository
): ViewModel() {
    private val userId = 4
    private val username = "didehddnjs89"

    fun testMy() = viewModelScope.launch { // 403 error
        myRepository.getRecentWork(userId)
        myRepository.getRecentCommission(userId)
    }

    fun testStatistics() = viewModelScope.launch { // region, category 입력 api 403 error
        statisticsRepository.run{
//            getCommissionDetail(userId) // ok

            getItemList(region = "동대문구", category = "라면")
            getMartList(region = "동대문구", category = "라면")

            getMaxPriceItem(region = "동대문구", category = "라면")
            getMinPriceItem(region = "동대문구", category = "라면")

            getMaxPriceMart(region = "동대문구", category = "라면")
            getMinPriceMart(region = "동대문구", category = "라면")
        }
    }

    fun testUserPointHistory() = viewModelScope.launch { // ok
//        userPointHistoryRepository.getUserPointHistory(userId)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun testWorker() = viewModelScope.launch {
        workerRepository.run {
            postWork(
                work = "test",
                workCount = 5,
                workPoint = 10,
                region = Region.DONGDAEMUN,
                item = "라면",
                itemPrice = 1000,
                workDate = LocalDateTime.now(),
                memberId = 4
            )

//            getWorkerCounts(username)

//            getWorkerPoint(username)

//            getWorking(username)

//            getWorkerHour(username)

//            getWorkHistory(username, WorkStatus.COMPLETED)

            getWorkMost(username)
        }
    }
}