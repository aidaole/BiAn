package com.aidaole.bian.features.language

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
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
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    NestedScrollExample()
}

private const val TAG = "NestedScrollExample"

@Composable
fun NestedScrollExample() {
    val listState = rememberLazyListState()
    val scrollState = rememberScrollState()

    // 计算 stickyHeader 是否吸顶
    val isStickyHeaderPinned = remember(scrollState) {
        derivedStateOf {
            val pinned = scrollState.value >= 200
            Log.d(TAG, "isStickyHeaderPinned: $pinned, scrollState: ${scrollState.value}")
            pinned
        }
    }

    // 嵌套滑动连接器
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                return if (!isStickyHeaderPinned.value) {
                    Log.d(TAG, "Parent consumes scroll: $available")
                    available
                } else {
                    Offset.Zero
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // stickyHeader
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(
                    text = if (isStickyHeaderPinned.value) "Sticky Header Pinned" else "Sticky Header Not Pinned",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        item {
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
                            text = "Item $index",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}