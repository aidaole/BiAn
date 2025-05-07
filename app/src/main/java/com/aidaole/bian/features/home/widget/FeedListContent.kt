package com.aidaole.bian.features.home.widget

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.aidaole.bian.features.home.data.FeedTabInfo
import kotlinx.coroutines.launch

private const val TAG = "FeedListContent"

@Composable
fun FeedListPagers(
    modifier: Modifier = Modifier,
    isStickyHeaderPinned: Boolean,
    outerDispatcher: NestedScrollDispatcher
) {
    val tabTitles = listOf(
        FeedTabInfo(0, "发现"),
        FeedTabInfo(1, "关注"),
    )
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
            tabContents = tabTitles.mapIndexed { index, item ->
                { TabContent(isStickyHeaderPinned, tabTitles[index], outerDispatcher) }
            },
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
private fun TabContent(
    isStickyHeaderPinned: Boolean,
    feedTabInfo: FeedTabInfo,
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
            FeedExploreItemWidget()
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Composable
fun IconInfosTabRow(
    tabTitles: List<FeedTabInfo>,
    pagerState: PagerState,
    onTabSelected: (Int) -> Unit,
    onSizeChanged: (IntSize) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                onSizeChanged.invoke(size)
                Log.d(TAG, "IconInfosTabRow: ${size.height}")
            }
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabTitles.forEachIndexed { index, feedTabInfo ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.noRippleClickable { onTabSelected(index) }
            ) {
                Text(
                    text = feedTabInfo.name,
                    color = if (pagerState.currentPage == index) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color(0xFF666666)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (pagerState.currentPage == index) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
                if (pagerState.currentPage == index) {
                    Box(
                        modifier = Modifier
                            .width(12.dp)
                            .height(2.dp)
                            .offset(y = (-8).dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                    )
                } else {
                    Spacer(modifier = Modifier.height(2.dp))
                }
            }
            if (index < tabTitles.size - 1) {
                Spacer(modifier = Modifier.width(24.dp))
            }
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

// Add this composable function for clickable without ripple effect
@Composable
fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }
    ) {
        onClick()
    }
}