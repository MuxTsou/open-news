package ai.nextstop.opennews.compose.articles

import Dimens.PaddingNormal
import ai.nextstop.opennews.models.local.Article
import ai.nextstop.opennews.viewmodels.ArticleViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items

@Composable
fun ArticlePaging(viewModel: ArticleViewModel, onItemClick: (article: Article) -> Unit) {
    val articles = viewModel.getTopHeadLinePagingData().collectAsLazyPagingItems()
    val itemModifier = Modifier.padding(PaddingNormal).fillMaxWidth().wrapContentHeight()
    LazyColumn {
        item {
            Box(
                modifier = Modifier.padding(
                    top = PaddingNormal,
                    start = PaddingNormal,
                    end = PaddingNormal,
                )
            ) {
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
        item {
            Box(
                modifier = Modifier.padding(PaddingNormal).fillMaxWidth().wrapContentHeight()
            ) {
                when {
                    articles.loadState.refresh is LoadState.Loading -> Unit
                    articles.loadState.append is LoadState.Loading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                    articles.loadState.append is LoadState.Error -> {
                        Text(
                            text = "Loading error",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    articles.loadState.append is LoadState.NotLoading && articles.loadState.append.endOfPaginationReached -> {
                        Text(
                            text = "End of list",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
        }

    }
}