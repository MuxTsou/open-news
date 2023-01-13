package ai.nextstop.opennews.workers

import ai.nextstop.opennews.data.remote.ApiConfig
import ai.nextstop.opennews.repositories.ArticleRepository
import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class TopHeadlineWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: ArticleRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val tag: String = "TopHeadlineWorker"
        const val page: String = "page"
        const val pageSize: String = "pageSize"
    }

    override suspend fun doWork(): Result {
        return try{
            val page = inputData.getInt(page, 0)
            val pageSize = inputData.getInt(pageSize, ApiConfig.LIMIT)
            repository.getTopHeadline(page = page, pageSize = pageSize)
            Result.success()
        } catch (e: Exception) {
            Log.d("Worker", e.message.toString())
            Result.failure()
        }
    }

}