package com.aidaole.bian.features.home.widget

import android.health.connect.datatypes.HeightRecord
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.aidaole.bian.data.entity.FeedTab
import kotlinx.coroutines.launch

private const val TAG = "FeedListContent"

@Composable
fun FeedListPagers(
    homeFeedTabInfos: List<FeedTab>,
    modifier: Modifier = Modifier,
    isStickyHeaderPinned: Boolean,
    outerDispatcher: NestedScrollDispatcher
) {
    val pagerState = rememberPagerState { homeFeedTabInfos.size }
    val coroutineScope = rememberCoroutineScope()

    // 1. 为每个 tab 创建 LazyListState，并用 rememberSaveable 保存
    val listStates = homeFeedTabInfos.associate { tab ->
        tab.id to rememberSaveable(tab.id, saver = LazyListState.Saver) { LazyListState() }
    }

    Column(
        modifier = modifier
    ) {
        IconInfosTabRow(
            tabTitles = homeFeedTabInfos,
            pagerState = pagerState,
            onTabSelected = { index ->
                coroutineScope.launch { pagerState.animateScrollToPage(index) }
            },
        )
        IconInfosPager(
            pagerState = pagerState,
            tabContents = homeFeedTabInfos.mapIndexed { index, item ->
                {
                    // 2. 传递 LazyListState
                    TabContent(
                        isStickyHeaderPinned,
                        homeFeedTabInfos[index],
                        outerDispatcher,
                        listStates[homeFeedTabInfos[index].id]!!
                    )
                }
            },
            modifier = Modifier.weight(1F)
        )
    }
}

@Composable
private fun TabContent(
    isStickyHeaderPinned: Boolean,
    feedTab: FeedTab,
    outDispatcher: NestedScrollDispatcher,
    listState: LazyListState
) {
    Log.d(TAG, "TabContent: $isStickyHeaderPinned")
    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (!isStickyHeaderPinned) {
//                        Log.d(TAG, "onPreScroll: $available")
                        val parentCustom = outDispatcher.dispatchPreScroll(available, source)
                        return parentCustom
                    } else {
                        return Offset.Zero
                    }
                }
            })
    ) {
        feedTab.contents.forEach {
            item {
                FeedExploreItemWidget(feedPost = it)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun IconInfosTabRow(
    tabTitles: List<FeedTab>,
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
                    text = feedTabInfo.tabName,
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