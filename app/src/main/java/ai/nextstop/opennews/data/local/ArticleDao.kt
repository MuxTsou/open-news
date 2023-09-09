package ai.nextstop.opennews.data.local

import ai.nextstop.opennews.data.remote.ApiConfig
import ai.nextstop.opennews.models.local.Article
import io.realm.Realm
import io.realm.RealmResults
import timber.log.Timber

class ArticleDao : Dao<Article>(Article::class.java){

    fun findPage(page: Int, limit: Long = ApiConfig.LIMIT.toLong()) : RealmResults<Article>{
        try {
            val realm = Realm.getDefaultInstance()
            val results = realm.where(Article::class.java)
                .equalTo("page", page)
                .findAll()
            Timber.d("result ${results.size}")
            return results
        } catch (e: Exception) {
            Timber.d(e.message)
            throw e
        }
    }
}