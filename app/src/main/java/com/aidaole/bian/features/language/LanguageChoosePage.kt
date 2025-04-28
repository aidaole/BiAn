package com.aidaole.bian.features.language

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.features.home.HomeViewModel
import com.aidaole.bian.features.home.StockItem
import com.aidaole.bian.features.home.StockPercentWidget
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LanguageChoosePage(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {
    val stockItems = homeViewModel.stockItems.collectAsState()
    CustomNestedScrollPage(stockItems = stockItems.value)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomNestedScrollPage(
    stockItems: List<StockItem>,
    tabTitles: List<String> = listOf("Tab1", "Tab2"),
) {
    val appBarHeight = 56.dp
    val loginHeight = 100.dp
    val appBarHeightPx = with(LocalDensity.current) { appBarHeight.toPx() }
    val loginHeightPx = with(LocalDensity.current) { loginHeight.toPx() }
    val totalHeaderHeightPx = appBarHeightPx + loginHeightPx

    // 用于控制 header 的偏移
    val headerOffsetPx = remember { mutableStateOf(0f) }

    // 自定义 NestedScrollConnection
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val oldOffset = headerOffsetPx.value
                val newOffset = (oldOffset + delta).coerceIn(-totalHeaderHeightPx, 0f)
                val consumed = newOffset - oldOffset
                headerOffsetPx.value = newOffset
                // 只要 header 还没完全收起/展开，就消耗这部分滑动
                return Offset(0f, consumed)
            }

            override fun onPostScroll(consumed: Offset, available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val oldOffset = headerOffsetPx.value
                val newOffset = (oldOffset + delta).coerceIn(-totalHeaderHeightPx, 0f)
                val consumedY = newOffset - oldOffset
                headerOffsetPx.value = newOffset
                // 只要 header 还没完全收起/展开，就消耗这部分滑动
                return Offset(0f, consumedY)
            }
        }
    }

    val pagerState = rememberPagerState { tabTitles.size }
    val scope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {
        Column {
            // AppBar + 登录区域，整体可偏移
            Column(Modifier.offset { IntOffset(x = 0, y = headerOffsetPx.value.roundToInt()) }) {
                TopAppBar(
                    title = { Text("AppBar") }, modifier = Modifier
                        .fillMaxWidth()
                        .height(appBarHeight)
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(loginHeight)
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { /* 登录 */ }) {
                        Text("注册/登录")
                    }
                }
            }

            // 股票列表
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                stockItems.forEachIndexed { index, item ->
                    item {
                        StockItemWidget(index, item)
                    }
                }
                item {
                    // TabRow + Pager
                    Column {
                        TabRow(
                            selectedTabIndex = pagerState.currentPage
                        ) {
                            tabTitles.forEachIndexed { index, title ->
                                Tab(
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        scope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    },
                                    text = { Text(title) }
                                )
                            }
                        }
                        HorizontalPager(
                            state = pagerState, modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) { page ->
                            // 这里可以放各自的内容
                            Box(
                                Modifier
                                    .fillMaxSize()
                                    .background(if (page % 2 == 0) Color.Cyan else Color.Yellow),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Page: ${tabTitles[page]}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StockItemWidget(index: Int, stockItem: StockItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stockItem.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        Icon(
            imageVector = Icons.Rounded.LocalFireDepartment,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(Modifier.weight(1f))
        Column {
            Text("${stockItem.price}", style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W800))
            Text("${stockItem.convertPrice}")
        }
        Spacer(Modifier.width(20.dp))
        StockPercentWidget(stockItem.percent)
    }
}
