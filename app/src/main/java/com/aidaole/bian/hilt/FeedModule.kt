package com.aidaole.bian.hilt

import android.app.Application
import com.aidaole.bian.data.repo.FeedRepository
import com.aidaole.bian.data.repo.fake.FakeFeedRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FeedModule {

    @Provides
    @Singleton
    fun provideFeedRepository(
        application: Application
    ): FeedRepository {
        return FakeFeedRepository(application)
    }
}