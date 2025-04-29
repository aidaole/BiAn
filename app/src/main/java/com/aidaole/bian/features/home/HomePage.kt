package com.aidaole.bian.features.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.R
import com.aidaole.bian.core.theme.StockDownColor
import com.aidaole.bian.core.theme.StockUpColor
import com.aidaole.bian.features.home.widget.HomeHeaderContent
import com.aidaole.bian.features.home.widget.HomeAppBar
import kotlinx.coroutines.launch

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onLoginClicked: () -> Unit = {}
) {
    val stockItems = homeViewModel.stockItems.collectAsState()
    val tabTitles = listOf("火币", "币安")
    val pagerState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Log.d(TAG, "screenHeight: $screenHeight")
    val outDispatcher = remember { NestedScrollDispatcher() }
    val outerScrollState = rememberScrollState()

    var appBarHeight by remember { mutableStateOf(0) }
    var stockLIstHeight by remember { mutableStateOf(0) }

    // 计算 IconInfosTabRow 是否到顶
    val isStickyHeaderPinned = remember(outerScrollState, stockLIstHeight) {
        derivedStateOf {
            val pinned = outerScrollState.value >= stockLIstHeight
            Log.d(TAG, "isStickyHeaderPinned: $pinned, scrollState: ${outerScrollState.value}")
            pinned
        }
    }

    Scaffold(
        topBar = {
            HomeAppBar(onSizeChanged = { size ->
                appBarHeight = size.height
                Log.d(TAG, "appBarHeight: $appBarHeight")
            })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(outerScrollState)
                .nestedScroll(
                    dispatcher = outDispatcher,
                    connection = object : NestedScrollConnection {
                        override fun onPreScroll(
                            available: Offset,
                            source: NestedScrollSource
                        ): Offset {
                            if (available.y < 0){
                                // 向上滑动
                                Log.d(
                                    TAG,
                                    "onPreScroll: 父布局收到 $available, isStickyHeaderPinned: ${isStickyHeaderPinned.value}"
                                )
                                // 防止快速拖动, 导致向上移动超出最大距离
                                val restHeight = outerScrollState.value - stockLIstHeight
                                var actual = restHeight.toFloat()
                                Log.d(TAG, "onPreScroll: restHeight: $restHeight, available: $available")
                                if (restHeight + available.y > 0) {
                                    actual = available.y
                                }
                                outerScrollState.dispatchRawDelta(-actual)
                                return Offset(0f, actual)
                            } else {
                                // 向下滑动
                                Log.d(TAG, "onPreScroll: 1父布局收到 $available")
                                if (isStickyHeaderPinned.value) {
                                    return Offset.Zero
                                } else {
                                    outerScrollState.dispatchRawDelta(-available.y)
                                    return Offset(0f, available.y)
                                }
                            }

                        }
                    }
                )
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            HomeHeaderContent(
                modifier = Modifier.onSizeChanged { size ->
                    stockLIstHeight = size.height
                },
                stockItems,
                onLoginClicked
            )
            IconInfosTabRow(
                tabTitles = tabTitles,
                pagerState = pagerState,
                onTabSelected = { index ->
                    coroutineScope.launch { pagerState.animateScrollToPage(index) }
                }
            )
            // 计算Pager高度，考虑内边距
            val pagerHeight = screenHeight - innerPadding.calculateTopPadding()
            Log.d(TAG, "pagerHeight: $pagerHeight")
            IconInfosPager(
                pagerState = pagerState,
                tabContents = listOf(
                    { TabContent(isStickyHeaderPinned.value, "火币", outDispatcher) },
                    { TabContent(isStickyHeaderPinned.value, "BTC", outDispatcher) }
                ),
                modifier = Modifier.height(pagerHeight)
            )
        }
    }
}

@Composable
private fun TabContent(
    isStickyHeaderPinned: Boolean,
    title: String,
    outDispatcher: NestedScrollDispatcher
) {
    val listState = rememberLazyListState()

    Log.d(TAG, "TabContent: $isStickyHeaderPinned")
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    val delta = available.y
                    return if (!isStickyHeaderPinned && delta < 0) {  // 向上滑动且未吸顶
                        // 分发滑动事件给外层LazyColumn
                        val parentConsumed = outDispatcher.dispatchPreScroll(
                            available = available, source = source
                        )
                        Log.d(TAG, "Parent consumes scroll: $available, consumed: $parentConsumed")
                        parentConsumed
                    } else {
                        Offset.Zero
                    }
                }
            }),
    ) {
        items(50) {
            Text(
                "$title $it",
                Modifier
                    .height(50.dp)
                    .padding(5.dp)
            )
        }
    }
}

@Composable
fun StockPercentWidget(percent: Float) {
    Box(
        modifier = Modifier
            .background(
                color = if (percent > 0) StockUpColor else StockDownColor,
                shape = RoundedCornerShape(10.dp)
            )
            .width(80.dp)
            .height(30.dp), contentAlignment = Alignment.Center
    ) {
        Text(
            "$percent",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W800),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}


@Composable
fun IconInfosTabRow(
    tabTitles: List<String>, pagerState: PagerState, onTabSelected: (Int) -> Unit
) {
    TabRow(selectedTabIndex = pagerState.currentPage) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                selected = pagerState.currentPage == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) })
        }
    }
}

@Composable
fun IconInfosPager(
    pagerState: PagerState, tabContents: List<@Composable () -> Unit>, modifier: Modifier = Modifier
) {
    HorizontalPager(
        state = pagerState, modifier = modifier.fillMaxWidth()
    ) { page ->
        tabContents[page]()
    }
}