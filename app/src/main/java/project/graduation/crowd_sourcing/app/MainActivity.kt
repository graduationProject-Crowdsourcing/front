package project.graduation.crowd_sourcing.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import project.graduation.crowd_sourcing.presentation.ui.screen.base.BaseView
import project.graduation.crowd_sourcing.presentation.ui.theme.CrowdSourcingTheme
import androidx.hilt.navigation.compose.hiltViewModel
import project.graduation.crowd_sourcing.domain.local.TokenManager

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CrowdSourcingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BaseView()
                    TestServer()
                }
            }
        }
    }
}

@Composable
@RequiresApi(Build.VERSION_CODES.O)
fun TestServer() {
    val testViewModel: TestViewModel = hiltViewModel()

    fun testMy(){
        testViewModel.testMy()
    }

    fun testUserPointHistory(){
        testViewModel.testUserPointHistory()
    }

    fun testStatistics(){
        testViewModel.testStatistics()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun testWorker(){
        testViewModel.testWorker()
    }

    testViewModel.getFcmToken()
    testMy()
//    testUserPointHistory()
//    testMy()
//    testStatistics()
//    testWorker()

}