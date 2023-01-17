package ai.nextstop.opennews.data.paging.pagingsource

import ai.nextstop.opennews.data.local.ArticleDao
import ai.nextstop.opennews.data.remote.ApiConfig.Companion.INITIAL_PAGE
import ai.nextstop.opennews.models.local.Article
import androidx.annotation.MainThread
import androidx.paging.PagingSource
import androidx.paging.PagingState
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmResults
import io.realm.internal.IOException
import timber.log.Timber

class ArticlePageSource(private val articleDao: ArticleDao) : PagingSource<Int, Article>() {

    private val realmChangeListener =
        OrderedRealmCollectionChangeListener<RealmResults<Article>> @MainThread { _, changeSet ->
            Timber.d("change state: ${changeSet.state}")
            if (changeSet.state == OrderedCollectionChangeSet.State.UPDATE) {
                invalidate()
            }
        }

    init {
        val query = articleDao.findAllAsync()
        query.addChangeListener(realmChangeListener)
        registerInvalidatedCallback {
            if (query.isValid) {
                query.removeChangeListener(realmChangeListener)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        val refreshKey = state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
        return refreshKey
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val pageNumber = params.key ?: INITIAL_PAGE
            val data = articleDao.findPage(page = pageNumber)
            LoadResult.Page(
                data = data,
                prevKey = if (pageNumber > 1) pageNumber - 1 else null,
                nextKey = if (data.isNotEmpty()) pageNumber + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}