package ai.nextstop.opennews.data.remote

class ApiConfig {
    companion object {
        const val API_KEY = "d5eb43247b784855a462c28e4202da23"
        const val HOST = "newsapi.org"
        const val API_VERSION = "v2"
        const val BASE_API_URL = "https://$HOST/$API_VERSION/"

        const val LIMIT = 20
    }
}