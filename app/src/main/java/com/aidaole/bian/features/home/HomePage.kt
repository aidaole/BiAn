package com.aidaole.bian.features.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.R
import com.aidaole.bian.data.repo.fake.FakeFeedRepository
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
                homeViewModel = HomeViewModel(Application(), FakeFeedRepository(Application())),
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
    val outerDispatcher = remember { NestedScrollDispatcher() }
    val outerScrollState = rememberLazyListState()
    var allHeight by remember { mutableIntStateOf(0) }
    val allHeightDp = with(LocalDensity.current) { allHeight.toDp() }
    var stockListHeight by remember { mutableIntStateOf(0) }
    // 获取屏幕高度
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    // 计算头部是否滚动到顶部
    val isStickyHeaderPinned by remember {
        derivedStateOf {
            // 第0个item（头部）不可见或部分不可见时，说明头部已经滚动到顶部
            outerScrollState.firstVisibleItemIndex > 0
        }
    }
    // 创建一个NestedScrollConnection，用于控制外部容器的滚动
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta < 0) {
                    val actual = if (outerScrollState.firstVisibleItemScrollOffset - delta > stockListHeight) {
                        outerScrollState.firstVisibleItemScrollOffset - stockListHeight.toFloat()
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
    }

    val stockItems = homeViewModel.stockItems.collectAsState()
    val homeFeedTabs by homeViewModel.homeFeedTabs.collectAsState()

    Scaffold(
        topBar = {
            HomeAppBar()
        },
        bottomBar = {
            Spacer(Modifier.height(with(LocalDensity.current) { bottomBarHeight.toDp() }))
        }
    ) { innerPadding ->
        LazyColumn(
            state = outerScrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .onSizeChanged { size ->
                    allHeight = size.height
                }
                .nestedScroll(connection = nestedScrollConnection, dispatcher = outerDispatcher)
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
                    Log.d(TAG, "HomePage: isStickyHeaderPinned: $isStickyHeaderPinned")
                    FeedListPagers(
                        homeFeedTabs,
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
