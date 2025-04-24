package com.aidaole.bian

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.aidaole.bian.ui.screens.home.HomeScreen
import com.aidaole.bian.ui.theme.BiAnTheme
import com.aidaole.bian.ui.screens.language.LanguageChooseScreen
import com.aidaole.bian.ui.screens.login.LoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiAnTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainRoute(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainRoute(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
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
            LanguageChooseScreen()
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
            LoginScreen(onCloseClicked = {
                navController.navigateUp()
            })
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainRoutePreview() {
    BiAnTheme  {
        MainRoute(modifier = Modifier)
    }
}