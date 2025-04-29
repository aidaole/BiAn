package com.aidaole.bian.features.home.widget

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource

class HomePageNestedScrollConnection(
    private val onStickyHeaderPinned: (Boolean) -> Unit,
    private val listState: LazyListState
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        // 获取 stickyHeader 的位置信息
        val layoutInfo = listState.layoutInfo
        val stickyHeaderItem = layoutInfo.visibleItemsInfo.find { it.key == "stickyHeader" }
        val isPinned =
            stickyHeaderItem != null && stickyHeaderItem.offset <= layoutInfo.viewportStartOffset
        onStickyHeaderPinned(isPinned)
        return Offset.Zero
    }

    override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
    ): Offset {
        // 获取 stickyHeader 的位置信息
        val layoutInfo = listState.layoutInfo
        val stickyHeaderItem = layoutInfo.visibleItemsInfo.find { it.key == "stickyHeader" }
        val isPinned =
            stickyHeaderItem != null && stickyHeaderItem.offset <= layoutInfo.viewportStartOffset
        onStickyHeaderPinned(isPinned)
        return Offset.Zero
    }
}
