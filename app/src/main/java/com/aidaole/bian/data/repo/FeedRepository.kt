package com.aidaole.bian.data.repo

import com.aidaole.bian.data.entity.FeedTab
import com.aidaole.bian.data.entity.StockItem

interface FeedRepository {

    suspend fun getHomeStocks(): List<StockItem>

    suspend fun getFeedPosts(): List<FeedTab>
}