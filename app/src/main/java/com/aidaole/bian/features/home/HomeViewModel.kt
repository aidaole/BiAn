package com.aidaole.bian.features.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.aidaole.bian.features.home.data.StockItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

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
}