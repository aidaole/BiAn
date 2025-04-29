package com.aidaole.bian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.aidaole.bian.features.home.HomePage
import com.aidaole.bian.core.theme.BiAnTheme
import com.aidaole.bian.features.language.LanguageChoosePage
import com.aidaole.bian.features.login.LoginPage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiAnTheme {
                Box(modifier = Modifier.fillMaxSize()) {
                    MainRoute()
                }
            }
        }
    }
}

@Composable
fun MainRoute() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomePage(
                onLoginClicked = {
                    navController.navigate("login", navOptions = navOptions {
                        anim {
                            enter = android.R.anim.slide_in_left
                        }
                    })
                }
            )
        }
        composable("language_choose") {
            LanguageChoosePage()
        }
        composable("login",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(200),
                )
            }) {
            LoginPage(onCloseClicked = {
                navController.navigateUp()
            })
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainRoutePreview() {
    BiAnTheme {
        MainRoute()
    }
}