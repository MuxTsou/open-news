package ai.nextstop.opennews.data.paging.mediator

import ai.nextstop.opennews.data.local.ArticleDao
import ai.nextstop.opennews.data.remote.ApiConfig
import ai.nextstop.opennews.data.remote.ArticleApi
import ai.nextstop.opennews.models.local.Article
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.realm.exceptions.RealmException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class ArticleRemoteMediator(private val dao: ArticleDao, private val api: ArticleApi) : RemoteMediator<Int, Article>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Article>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> null
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()

                    // You must explicitly check if the last item is null when
                    // appending, since passing null to networkService is only
                    // valid for initial load. If lastItem is null it means no
                    // items were loaded after the initial REFRESH and there are
                    // no more items to load.
                    if (lastItem == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    lastItem.page + 1
                }
            } ?: ApiConfig.INITIAL_PAGE
            Timber.d("page: $loadKey")
            val topHeadline = api.getTopHeadline(apiKey = ApiConfig.API_KEY, page = loadKey, pageSize = ApiConfig.LIMIT)
            topHeadline.articles.forEach{ article ->
                article.page = loadKey
            }
            coroutineScope {
                withContext(Dispatchers.IO) {
                    dao.copyOrUpdate(topHeadline.articles)
                }
            }

            MediatorResult.Success(
                endOfPaginationReached = topHeadline.articles.size < ApiConfig.LIMIT
            )
        } catch (e: IOException) {
            Timber.d("exception: ${e.message}")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Timber.d("exception: ${e.message}")
            MediatorResult.Error(e)
        } catch (e: RealmException) {
            Timber.d("exception: ${e.message}")
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction {
        coroutineScope {
            withContext(Dispatchers.IO) {
                try{
                    dao.deleteAll()
                } catch (e: Exception) {
                    Timber.d("init exception: ${e.message}")
                }
            }
        }
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
}