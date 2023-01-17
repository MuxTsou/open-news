package ai.nextstop.opennews

import ai.nextstop.opennews.compose.articles.ArticlePaging
import ai.nextstop.opennews.ui.theme.OpenNewsTheme
import ai.nextstop.opennews.viewmodels.ArticleViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: ArticleViewModel by viewModels()

    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OpenNewsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val refreshScope = rememberCoroutineScope()
                    var refreshing by remember { mutableStateOf(false) }
                    fun refresh() = refreshScope.launch {
                        refreshing = true
                        delay(1500)
                        refreshing = false
                    }

                    val pullRefreshState = rememberPullRefreshState(refreshing, ::refresh)
                    Box(Modifier.pullRefresh(pullRefreshState)) {
                        ArticlePaging(viewModel, onItemClick = { article ->

                        })
                        PullRefreshIndicator(
                            refreshing,
                            pullRefreshState,
                            Modifier.align(Alignment.TopCenter)
                        )
                    }
                }
            }
        }
    }
}

