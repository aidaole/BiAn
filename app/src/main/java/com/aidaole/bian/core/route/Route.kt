package com.aidaole.bian.core.route

import kotlinx.serialization.Serializable

class Route {

    @Serializable
    data class MainPageData(val ext: String)

    @Serializable
    data class LoginPageData(val ext: String)

    @Serializable
    data class LanguageChoosePageData(val ext: String)

    @Serializable
    data class HomePageData(val ext: String)

    @Serializable
    data class MarketPageData(val ext: String)

    @Serializable
    data class TradePageData(val ext: String)

    @Serializable
    data class ContractPageData(val ext: String)

    @Serializable
    data class ProfilePageData(val ext: String)
}