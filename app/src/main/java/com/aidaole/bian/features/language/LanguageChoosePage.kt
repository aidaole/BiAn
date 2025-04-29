package com.aidaole.bian.features.language

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.features.home.HomeViewModel

@Composable
fun LanguageChoosePage(
    modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()
) {
    NestedScrollExample()
}

private const val TAG = "NestedScrollExample"

@Composable
fun NestedScrollExample() {
    val listState = rememberLazyListState()
    val outerScrollState = rememberScrollState()
    val dispatcher = remember { NestedScrollDispatcher() }

    // 计算 stickyHeader 是否吸顶
    val isStickyHeaderPinned = remember(outerScrollState) {
        derivedStateOf {
            val pinned = outerScrollState.value >= 200
            Log.d(TAG, "isStickyHeaderPinned: $pinned, scrollState: ${outerScrollState.value}")
            pinned
        }
    }

    // 嵌套滑动连接器
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                return if (!isStickyHeaderPinned.value && delta < 0) {  // 向上滑动且未吸顶
                    // 分发滑动事件给外层LazyColumn
                    val parentConsumed = dispatcher.dispatchPreScroll(
                        available = available, source = source
                    )
                    Log.d(TAG, "Parent consumes scroll: $available, consumed: $parentConsumed")
                    parentConsumed
                } else {
                    Offset.Zero
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(outerScrollState)
            .nestedScroll(
                dispatcher = dispatcher,
                connection = object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource
                    ): Offset {
                        Log.d(TAG, "onPreScroll: 父布局收到 $available")
                        if (isStickyHeaderPinned.value){
                            return Offset.Zero
                        } else {
                            outerScrollState.dispatchRawDelta(-available.y)
                            return Offset(0f, available.y)
                        }
                    }
                }
            )
    ) {
        // stickyHeader
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isStickyHeaderPinned.value) "Sticky Header Pinned" else "Sticky Header Not Pinned"
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(800.dp)
        ) {
            // 内层 LazyColumn
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection)
            ) {
                items(50) { index ->
                    Text(
                        text = "Item $index", modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}