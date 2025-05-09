package com.aidaole.bian.features.home

import android.annotation.SuppressLint
import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.R
import com.aidaole.bian.features.home.widget.FeedListPagers
import com.aidaole.bian.features.home.widget.HomeAppBar
import com.aidaole.bian.features.home.widget.HomeHeaderContent

private const val TAG = "HomePage"

@Preview
@Composable
private fun HomePagePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.screen_home), contentDescription = null)
        Column {
            Spacer(Modifier.height(30.dp))
            HomePage(
                homeViewModel = HomeViewModel(Application()),
            )
        }

    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ConfigurationScreenWidthHeight")
@Composable
fun HomePage(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onLoginClicked: () -> Unit = {},
    bottomBarHeight: Int = 0,
) {
    val stockItems = homeViewModel.stockItems.collectAsState()
    val outerDispatcher = remember { NestedScrollDispatcher() }
    val outerScrollState = rememberScrollState()

    var allHeight by remember { mutableIntStateOf(0) }
    val allHeightDp = with(LocalDensity.current) { allHeight.toDp() }
    var stockListHeight by remember { mutableIntStateOf(0) }

    // 获取屏幕高度
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current

    // 计算 IconInfosTabRow 是否到顶
    val isStickyHeaderPinned by remember(outerScrollState) {
        derivedStateOf {
            // 向上滑动的距离超过展示的stockListHeight高度, 说明tabRow已经到顶
            val pinned = outerScrollState.value >= stockListHeight
//            Log.d(TAG, "isStickyHeaderPinned: $pinned, scrollState: ${outerScrollState.value}")
            pinned
        }
    }

    Scaffold(
        topBar = {
            HomeAppBar()
        },
        bottomBar = {
            Spacer(Modifier.height(with(LocalDensity.current) { bottomBarHeight.toDp() }))
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .onSizeChanged { size ->
                    allHeight = size.height
                }
        ) {
            item {
                HomeHeaderContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .onSizeChanged { size ->
                            stockListHeight = size.height
                        },
                    stockItems,
                    onLoginClicked
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeight - with(density) {
                            (bottomBarHeight + innerPadding.calculateTopPadding().toPx()).toDp()
                        })
                ) {
                    FeedListPagers(
                        modifier = Modifier
                            .height(allHeightDp)
                            .padding(horizontal = 20.dp),
                        isStickyHeaderPinned,
                        outerDispatcher
                    )
                }
            }
        }
    }
}
