package com.aidaole.bian.features.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aidaole.bian.data.entity.FeedTab
import com.aidaole.bian.data.entity.StockItem
import com.aidaole.bian.data.repo.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application, private val feedRepository: FeedRepository
) : AndroidViewModel(
    application = application
) {

    val stockItems = MutableStateFlow<List<StockItem>>(emptyList())
    val homeFeedTabs = MutableStateFlow<List<FeedTab>>(emptyList())

    init {
        loadStockItems()
        loadFeedTabContents()
    }

    private fun loadStockItems() {
        viewModelScope.launch {
            stockItems.value = withContext(Dispatchers.IO) {
                feedRepository.getHomeStocks()
            }
        }
    }

    private fun loadFeedTabContents() {
        viewModelScope.launch {
            homeFeedTabs.value = withContext(Dispatchers.IO) {
                feedRepository.getFeedPosts()
            }.onEach {
                Log.d(TAG, "loadFeedTabContents: ${it.tabName}")
                Log.d(TAG, "loadFeedTabContents: ${it.contents.size}")
            }
        }
    }
}