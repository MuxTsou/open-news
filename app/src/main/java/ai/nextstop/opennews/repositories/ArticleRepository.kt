package ai.nextstop.opennews.repositories

import ai.nextstop.opennews.data.local.ArticleDao
import ai.nextstop.opennews.data.remote.ArticleApi
import ai.nextstop.opennews.models.local.Article
import io.realm.RealmResults


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
}