package ai.nextstop.opennews.compose.articles

import Dimens.PaddingNormal
import Dimens.SpaceNormal
import ai.nextstop.opennews.R
import ai.nextstop.opennews.models.local.Article
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.fresco.FrescoImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleCard(article: Article, onClick: () -> Unit, modifier: Modifier) {
    Card(onClick = onClick, modifier = modifier) {
        Column(modifier = Modifier.fillMaxWidth()) {
            FrescoImage(
                modifier = Modifier.fillMaxWidth().height(240.dp),
                imageUrl = article.coverUrl, // loading a network image using an URL.
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                previewPlaceholder = R.drawable.ic_photo
            )
            Column(modifier = Modifier.padding(PaddingNormal)) {
                Spacer(modifier = Modifier.height(SpaceNormal))
                article.author?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(modifier = Modifier.height(SpaceNormal))
                Text(text = article.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(SpaceNormal))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ArticleCardPreview() {
    val itemModifier = Modifier.fillMaxWidth()
    ArticleCard(
        article = Article().apply {
            this.title = "This is title"
            this.description = "article description"
            this.coverUrl =
                "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1721&q=80"
        },
        onClick = {},
        modifier = itemModifier
    )
}