package ai.nextstop.opennews.models.remote

import ai.nextstop.opennews.models.local.Article

class TopHeadlineApiModel {
    var status: String = ""
    var totalResults: Int = 0
    var articles: List<Article> = ArrayList()
}