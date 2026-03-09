package com.ledsun.ainewstinder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.ledsun.ainewstinder.ui.AppScaffold
import com.ledsun.ainewstinder.viewmodel.FeedViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val vm = viewModel
        if (vm.autoRefreshOnLaunch.value) {
            vm.refresh()
        }

        setContent {
            MaterialTheme {
                Surface {
                    AppScaffold(viewModel = vm)
                }
            }
        }
    }
}
