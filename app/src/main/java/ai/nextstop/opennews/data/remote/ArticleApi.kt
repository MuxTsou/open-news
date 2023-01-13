package ai.nextstop.opennews.data.remote

import ai.nextstop.opennews.models.remote.TopHeadlineApiModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticleApi {

    companion object {
        const val ENDPOINT = "top-headlines"
    }

    @GET(ENDPOINT)
    suspend fun getTopHeadline(
        @Query("apiKey") apiKey: String = ApiConfig.API_KEY,
        @Query("country") country: String = "us",
        @Query("category") category: String = "business",
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int,
    ) : TopHeadlineApiModel
}