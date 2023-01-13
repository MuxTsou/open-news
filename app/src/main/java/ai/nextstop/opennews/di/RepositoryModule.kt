package ai.nextstop.opennews.di

import ai.nextstop.opennews.data.local.ArticleDao
import ai.nextstop.opennews.data.remote.ArticleApi
import ai.nextstop.opennews.repositories.ArticleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideArticleRepository(dao: ArticleDao, api: ArticleApi): ArticleRepository = ArticleRepository(dao, api)
}