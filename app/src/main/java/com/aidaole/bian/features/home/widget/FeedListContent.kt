package com.aidaole.bian.features.home.widget

import android.util.Log
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

private const val TAG = "FeedListContent"

@Composable
fun FeedListPagers(
    modifier: Modifier = Modifier,
    isStickyHeaderPinned: Boolean,
    outerDispatcher: NestedScrollDispatcher
) {
    val tabTitles = listOf("火币", "币安")
    val pagerState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        IconInfosTabRow(
            tabTitles = tabTitles,
            pagerState = pagerState,
            onTabSelected = { index ->
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            },
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
    onSizeChanged: (IntSize) -> Unit = {}
) {
    TabRow(selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.onSizeChanged { size ->
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