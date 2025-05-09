package com.aidaole.bian.features.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.aidaole.bian.R
import com.aidaole.bian.core.route.Route
import com.aidaole.bian.features.home.HomePage

@Preview
@Composable
private fun MainPagePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.screen_home), contentDescription = null)
        Column {
            Spacer(Modifier.height(30.dp))
            MainPage()
        }

    }
}

private const val TAG = "MainPage"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainPage(
    onLoginClicked: () -> Unit = {}
) {
    Log.d(TAG, "MainPage: 重组")
    var selectedItem by remember { mutableIntStateOf(0) }
    val bottomBarItems = remember { listOf("首页", "行情", "交易", "合约", "我的") }
    val bottomBarRoutes = remember {
        listOf(
            Route.HomePageData(""),
            Route.MarketPageData(""),
            Route.TradePageData(""),
            Route.ContractPageData(""),
            Route.ProfilePageData(""),
        )
    }
    var bottomBarHeight by remember { mutableStateOf(0) }
    val navController = rememberNavController()
    val homeOuterScrollState = rememberSaveable("home_scroll", saver = ScrollState.Saver) { ScrollState(0) }

    Scaffold(bottomBar = {
        BottomNavigationBar(modifier = Modifier.onSizeChanged {
            bottomBarHeight = it.height
        }, bottomBarItems, selectedItem, onItemSelected = { index ->
            selectedItem = index
            val route = bottomBarRoutes[index]
            Log.d(TAG, "MainPage: $route")
            Log.d(TAG, "MainPage: $selectedItem")
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
            }
        })
    }) { innerPadding ->
        NavHost(
            navController = navController, startDestination = bottomBarRoutes[0]
        ) {
            composable<Route.HomePageData> { backStackEntry ->
                Log.d(TAG, "HomePageData: new ")
                Log.d(TAG, "HomePageData: ${homeOuterScrollState.value} ")
                HomePage(
                    onLoginClicked = {
                        onLoginClicked.invoke()
                    }, bottomBarHeight = bottomBarHeight,
                    outerScrollState = homeOuterScrollState
                )
            }
            composable<Route.MarketPageData> {
                Log.d(TAG, "MarketPageData: new")
                LazyColumn {
                    items(50) { it ->
                        Text(
                            "Market Screen: ${it}", modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        )
                    }
                }
            }
            composable<Route.TradePageData> {
                LazyColumn {
                    items(50) { it ->
                        Text(
                            "Trade Screen: ${it}", modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        )
                    }
                }
            }
            composable<Route.ContractPageData> {
                Text("Contract Screen")
            }
            composable<Route.ProfilePageData> {
                Text("Profile Screen")
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    modifier: Modifier,
    bottomBarItems: List<String>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar(
        modifier = modifier, containerColor = MaterialTheme.colorScheme.background
    ) {
        bottomBarItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (index) {
                        0 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_home_filled
                                else R.drawable.ic_home
                            ), contentDescription = item
                        )

                        1 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_market_filled
                                else R.drawable.ic_market
                            ), contentDescription = item
                        )

                        2 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_trade_filled
                                else R.drawable.ic_trade
                            ), contentDescription = item
                        )

                        3 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_contract_filled
                                else R.drawable.ic_contract
                            ), contentDescription = item
                        )

                        4 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_profile_filled
                                else R.drawable.ic_profile
                            ), contentDescription = item
                        )

                        else -> Icon(painterResource(R.drawable.ic_home), contentDescription = item)
                    }
                },
                label = { Text(item, fontSize = 12.sp) },
                selected = selectedItem == index,
                onClick = { onItemSelected.invoke(index) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = Color(0xFF1A1A1A),
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = Color(0xFF1A1A1A),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

