package com.aidaole.bian.data.repo

import com.aidaole.bian.data.entity.FeedTab

interface FeedRepository {

    suspend fun getFeedPosts(): List<FeedTab>
}