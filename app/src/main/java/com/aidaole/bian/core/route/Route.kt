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
    data class HomePageData(val tag: String)
}