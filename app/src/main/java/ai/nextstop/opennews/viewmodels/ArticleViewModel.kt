package ai.nextstop.opennews.viewmodels

import ai.nextstop.opennews.data.local.asLiveData
import ai.nextstop.opennews.data.remote.ApiConfig
import ai.nextstop.opennews.models.local.Article
import ai.nextstop.opennews.repositories.ArticleRepository
import ai.nextstop.opennews.workers.TopHeadlineWorker
import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class ArticleViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: ArticleRepository
    ) : ViewModel() {

    fun findAll() : LiveData<List<Article>> {
        return repository.findAllAsync().asLiveData()
    }

    fun fetchTopHeadline(page: Int = 0, pageSize: Int = ApiConfig.LIMIT) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData = workDataOf(
            TopHeadlineWorker.page to page,
            TopHeadlineWorker.pageSize to pageSize,
        )
        val workRequest =
            OneTimeWorkRequestBuilder<TopHeadlineWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .addTag(TopHeadlineWorker.tag)
                .build()
        WorkManager.getInstance(context).beginUniqueWork(TopHeadlineWorker.tag, ExistingWorkPolicy.REPLACE, workRequest).enqueue()
    }

    fun getTopHeadLinePagingData(): Flow<PagingData<Article>> {
        return repository.getTopHeadlinePagingData().cachedIn(viewModelScope)
    }
}