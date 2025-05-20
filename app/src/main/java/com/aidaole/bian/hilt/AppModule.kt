package com.aidaole.bian.hilt

import com.aidaole.bian.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideApp(): App {
        return App.instance
    }
}