package ai.nextstop.opennews.compose.articles

import Dimens.PaddingNormal
import ai.nextstop.opennews.models.local.Article
import ai.nextstop.opennews.viewmodels.ArticleViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ArticleList(viewModel: ArticleViewModel, onItemClick: (article: Article) -> Unit) {
    val articles by viewModel.findAll().observeAsState(initial = emptyList())
    val itemModifier = Modifier.padding(PaddingNormal).fillMaxWidth().wrapContentHeight()
    viewModel.fetchTopHeadline()
    LazyColumn {
        item {
            Box(modifier = itemModifier) {
                Text(
                    "Open News",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    )
            }

        }
        items(articles) { article ->
            article?.let {
                ArticleCard(article = article, onClick = { }, modifier = itemModifier)
            }

        }
    }
}