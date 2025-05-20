package com.aidaole.bian.data.repo.fake

import android.app.Application
import com.aidaole.bian.data.entity.FeedTab
import com.aidaole.bian.data.repo.FeedRepository
import kotlinx.serialization.json.Json
import javax.inject.Inject

class FakeFeedRepository @Inject constructor(
    private val application: Application
) : FeedRepository {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    override suspend fun getFeedPosts(): List<FeedTab> {
        val jsonString = application.assets.open("crypto_news_mock_tabs.json").bufferedReader()
            .use { it.readText() }
        val feeds = json.decodeFromString<List<FeedTab>>(jsonString)
        return feeds
    }
}