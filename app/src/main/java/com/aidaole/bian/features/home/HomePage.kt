package com.aidaole.bian.features.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.R
import com.aidaole.bian.features.home.widget.FeedListPagers
import com.aidaole.bian.features.home.widget.HomeAppBar
import com.aidaole.bian.features.home.widget.HomeHeaderContent
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

private const val TAG = "HomePage"

@Preview
@Composable
private fun HomePagePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.screen_home), contentDescription = null)
        Column {
            Spacer(Modifier.height(30.dp))
            HomePage(
                homeViewModel = HomeViewModel(Application())
            )
        }

    }
}

@Serializable
data class HomePageData(val tag: String)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onLoginClicked: () -> Unit = {}
) {
    val stockItems = homeViewModel.stockItems.collectAsState()

    val outerDispatcher = remember { NestedScrollDispatcher() }
    val outerScrollState = rememberScrollState()

    var allHeight by remember { mutableIntStateOf(0) }
    val allHeightDp = with(LocalDensity.current) { allHeight.toDp() }
    var stockListHeight by remember { mutableIntStateOf(0) }

    // 计算 IconInfosTabRow 是否到顶
    val isStickyHeaderPinned by remember(outerScrollState) {
        derivedStateOf {
            // 向上滑动的距离超过展示的stockListHeight高度, 说明tabRow已经到顶
            val pinned = outerScrollState.value >= stockListHeight
            Log.d(TAG, "isStickyHeaderPinned: $pinned, scrollState: ${outerScrollState.value}")
            pinned
        }
    }

    var selectedItem by remember { mutableIntStateOf(0) }
    val bottomBarItems = listOf("首页", "行情", "交易", "合约", "我的")

    val navController = rememberNavController()

    Scaffold(
        topBar = {
            HomeAppBar()
        },
        bottomBar = {
            BottomNavigationBar(
                bottomBarItems,
                selectedItem,
                onItemSelected = { index ->
                    selectedItem = index
                    when (index) {
                        0 -> navController.navigate(HomePageData("home"))
                        1 -> navController.navigate(HomePageData("market"))
                        2 -> navController.navigate(HomePageData("trade"))
                        3 -> navController.navigate(HomePageData("contract"))
                        4 -> navController.navigate(HomePageData("profile"))
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HomePageData("home")
        ) {
            composable<HomePageData> { backStackEntry ->
                val data = backStackEntry.toRoute<HomePageData>()
                when (data.tag) {
                    "home" -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(horizontal = 20.dp)
                                .onSizeChanged { size ->
                                    allHeight = size.height
                                }
                                .verticalScroll(outerScrollState)
                                .nestedScroll(
                                    dispatcher = outerDispatcher,
                                    connection = object : NestedScrollConnection {
                                        override fun onPreScroll(
                                            available: Offset,
                                            source: NestedScrollSource
                                        ): Offset {
                                            val delta = available.y
                                            if (delta < 0) {
                                                val actual = if (outerScrollState.value - delta > stockListHeight) {
                                                    outerScrollState.value - stockListHeight.toFloat()
                                                } else {
                                                    delta
                                                }
                                                outerScrollState.dispatchRawDelta(-actual)
                                                return Offset(0f, actual)
                                            } else {
                                                return Offset.Zero
                                            }
                                        }
                                    }
                                )
                        ) {
                            HomeHeaderContent(
                                modifier = Modifier.onSizeChanged { size ->
                                    stockListHeight = size.height
                                },
                                stockItems,
                                onLoginClicked
                            )
                            FeedListPagers(
                                modifier = Modifier.height(allHeightDp),
                                isStickyHeaderPinned,
                                outerDispatcher
                            )
                        }
                    }

                    "market" -> {
                        // TODO: Add market screen
                        Text("Market Screen")
                    }

                    "trade" -> {
                        // TODO: Add trade screen
                        Text("Trade Screen")
                    }

                    "contract" -> {
                        // TODO: Add contract screen
                        Text("Contract Screen")
                    }

                    "profile" -> {
                        // TODO: Add profile screen
                        Text("Profile Screen")
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavigationBar(
    bottomBarItems: List<String>,
    selectedItem: Int,
    onItemSelected: (Int) -> Unit,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.background
    ) {
        bottomBarItems.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    when (index) {
                        0 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_home_filled
                                else R.drawable.ic_home
                            ),
                            contentDescription = item
                        )

                        1 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_market_filled
                                else R.drawable.ic_market
                            ),
                            contentDescription = item
                        )

                        2 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_trade_filled
                                else R.drawable.ic_trade
                            ),
                            contentDescription = item
                        )

                        3 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_contract_filled
                                else R.drawable.ic_contract
                            ),
                            contentDescription = item
                        )

                        4 -> Icon(
                            painter = painterResource(
                                if (selectedItem == index) R.drawable.ic_profile_filled
                                else R.drawable.ic_profile
                            ),
                            contentDescription = item
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

