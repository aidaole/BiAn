package com.aidaole.bian.features.home.data

data class StockItem(
    val name: String,
    val withFire: Boolean,
    val price: Float,
    val convertPrice: Float,
    val percent: Float
)