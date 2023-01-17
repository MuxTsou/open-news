package ai.nextstop.opennews.repositories

import ai.nextstop.opennews.data.local.ArticleDao
import ai.nextstop.opennews.data.paging.mediator.ArticleRemoteMediator
import ai.nextstop.opennews.data.paging.pagingsource.ArticlePageSource
import ai.nextstop.opennews.data.remote.ApiConfig
import ai.nextstop.opennews.data.remote.ArticleApi
import ai.nextstop.opennews.models.local.Article
import androidx.paging.*
import io.realm.RealmResults
import kotlinx.coroutines.flow.Flow


class ArticleRepository(private val dao: ArticleDao, private val  api: ArticleApi) {

    fun findAllAsync() : RealmResults<Article> {
        return dao.findAllAsync()
    }

    suspend fun getTopHeadline(page: Int, pageSize: Int) {
        val topHeadline = api.getTopHeadline(page = page, pageSize = pageSize)
        if(page == 0) {
            dao.deleteAll()
        }
        dao.copyOrUpdate(topHeadline.articles)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getTopHeadlinePagingData(): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(pageSize = ApiConfig.LIMIT),
            remoteMediator = ArticleRemoteMediator(dao, api),
        ) {
            ArticlePageSource(dao)
        }.flow
    }
}