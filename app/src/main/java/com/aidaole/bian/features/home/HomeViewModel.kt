package com.aidaole.bian.features.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aidaole.bian.data.entity.FeedPost
import com.aidaole.bian.data.entity.FeedTab
import com.aidaole.bian.features.home.data.FeedTabInfo
import com.aidaole.bian.features.home.data.StockItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application
) : AndroidViewModel(
    application = application
) {

    val stockItems = MutableStateFlow(
        listOf(
            StockItem("BNB", true, 604.28F, 4411.24F, -1.78F),
            StockItem("BTC", true, 93196.73F, 680336.24F, 0.38F),
            StockItem("BTC", true, 93196.73F, 680336.24F, 0.38F),
            StockItem("BTC", true, 93196.73F, 680336.24F, 0.38F),
            StockItem("BTC", true, 93196.73F, 680336.24F, 0.38F),
            StockItem("BTC", true, 93196.73F, 680336.24F, 0.38F),
        )
    )

    val homeFeedTabs = MutableStateFlow<List<FeedTab>>(listOf())
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = false
    }

    init {
        loadFeedTabContents()
    }

    private fun loadFeedTabContents() {
        viewModelScope.launch {
            val jsonString = application.assets.open("crypto_news_mock_tabs.json").bufferedReader()
                .use { it.readText() }
            val feeds = json.decodeFromString<List<FeedTab>>(jsonString)
            homeFeedTabs.value = feeds

            homeFeedTabs.value.forEach {
                Log.d(TAG, "loadFeedTabContents: ${it.tabName}")
                Log.d(TAG, "loadFeedTabContents: ${it.contents.size}")
            }
        }
    }
}