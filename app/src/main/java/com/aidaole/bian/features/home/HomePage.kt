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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.R
import com.aidaole.bian.features.home.widget.HomeAppBar
import com.aidaole.bian.features.home.widget.HomeHeaderContent
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

    val outerDispatcher = remember { NestedScrollDispatcher() }
    val outerScrollState = rememberScrollState()

    var allHeight by remember { mutableIntStateOf(0) }
    var stockListHeight by remember { mutableIntStateOf(0) }
    var tabRowHeight by remember { mutableIntStateOf(0) }
    val density = LocalDensity.current

    val allHeightDp = with(density) { allHeight.toDp() }

    // 计算 IconInfosTabRow 是否到顶
    val isStickyHeaderPinned by remember(outerScrollState) {
        derivedStateOf {
            // 向上滑动的距离超过展示的stockListHeight高度, 说明tabRow已经到顶
            val pinned = outerScrollState.value >= stockListHeight
            Log.d(TAG, "isStickyHeaderPinned: $pinned, scrollState: ${outerScrollState.value}")
            pinned
        }
    }

    Scaffold(
        topBar = {
            HomeAppBar()
        }
    ) { innerPadding ->
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
                                // 向上滑动
                                val actual = if (outerScrollState.value - delta > stockListHeight) {
                                    // 这里处理是因为, 快速滑动时, 可能加上delta之后, 已经超过了stockListHeight,
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
            Column(
                modifier = Modifier.height(allHeightDp)
            ) {
                IconInfosTabRow(
                    tabTitles = tabTitles,
                    pagerState = pagerState,
                    onTabSelected = { index ->
                        coroutineScope.launch { pagerState.animateScrollToPage(index) }
                    },
                    onSizeChanged = {
                        tabRowHeight = it.height
                    }
                )
                IconInfosPager(
                    pagerState = pagerState,
                    tabContents = listOf(
                        { TabContent(isStickyHeaderPinned, "火币", outerDispatcher) },
                        { TabContent(isStickyHeaderPinned, "BTC", outerDispatcher) }
                    ),
                    modifier = Modifier.weight(1F)
                )
            }

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
fun IconInfosTabRow(
    tabTitles: List<String>,
    pagerState: PagerState,
    onTabSelected: (Int) -> Unit,
    onSizeChanged: (IntSize) -> Unit
) {
    TabRow(selectedTabIndex = pagerState.currentPage, modifier = Modifier.onSizeChanged { size ->
        onSizeChanged.invoke(size)
        Log.d(TAG, "IconInfosTabRow: ${size.height}")
    }) {
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