package com.aidaole.bian.data.entity

data class StockItem(
    val name: String,
    val withFire: Boolean,
    val price: Float,
    val convertPrice: Float,
    val percent: Float
)