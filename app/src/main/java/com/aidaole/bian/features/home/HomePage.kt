package com.aidaole.bian.features.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aidaole.bian.R
import com.aidaole.bian.core.theme.InputFieldBg
import com.aidaole.bian.core.theme.StockDownColor
import com.aidaole.bian.core.theme.StockUpColor

@Preview
@Composable
private fun HomePagePreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.screen_home), contentDescription = null)
        Column {
            Spacer(Modifier.height(30.dp))
            HomePage(
                modifier = Modifier.alpha(0.8f), homeViewModel = HomeViewModel(Application())
            )
        }

    }
}

private const val TAG = "HomePage"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(
    homeViewModel: HomeViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onLoginClicked: () -> Unit = {}
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val stockItems = homeViewModel.stockItems.collectAsState()

    Scaffold(
        modifier = modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 20.dp),
                title = {
                    BiAnSearchBar()
                },
                scrollBehavior = topAppBarScrollBehavior,
                navigationIcon = {
                    Icon(
                        modifier = modifier
                            .size(35.dp)
                            .padding(5.dp),
                        contentDescription = "",
                        painter = painterResource(R.drawable.google),
                        tint = Color.Unspecified
                    )
                }, actions = {
                    SearchBarIcon(imageVector = Icons.Outlined.CameraAlt)
                    SearchBarIcon(imageVector = Icons.Default.Call)
                    SearchBarIcon(imageVector = Icons.Outlined.Email)
                    SearchBarIcon(imageVector = Icons.Default.AddCard)
                })
        },
        content = { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        "欢迎探索数字资产的世界!",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W800, fontSize = 28.sp)
                    )
                    Spacer(Modifier.height(30.dp))
                    Button(
                        modifier = Modifier.width(180.dp),
                        shape = RoundedCornerShape(10.dp),
                        onClick = { onLoginClicked.invoke() }
                    ) {
                        Text("注册/登陆", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(30.dp))
                }
                itemsIndexed(stockItems.value) { index, it ->
                    StockItemWidget(index, it)
                }
            }
        }
    )
}

@Composable
fun QuotesWidget(stockItems: State<List<StockItem>>) {
    Log.d(TAG, "QuotesWidget: ")
    LazyColumn {
        Log.d(TAG, "QuotesWidget: 1")
        itemsIndexed(stockItems.value) { index, it ->
            StockItemWidget(index, it)
        }
    }
}

data class StockItem(
    val name: String, val withFire: Boolean, val price: Float, val convertPrice: Float, val percent: Float
)

@Composable
fun StockItemWidget(index: Int, stockItem: StockItem) {
    Log.d(TAG, "StockItemWidget: $index")
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

@Composable
fun StockPercentWidget(percent: Float) {
    Box(
        modifier = Modifier
            .background(
                color = if (percent > 0) StockUpColor else StockDownColor, shape = RoundedCornerShape(10.dp)
            )
            .width(80.dp)
            .height(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "$percent",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.W800),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }

}

@Composable
fun BiAnSearchBar(modifier: Modifier = Modifier) {
    val state = rememberTextFieldState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = InputFieldBg)
            .padding(horizontal = 5.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = Icons.Default.Search,
            contentDescription = "seach",
            tint = Color.Unspecified,
        )
        Spacer(Modifier.width(5.dp))
        BasicTextField(state = state, decorator = { innerTextField ->
            Box(
                contentAlignment = Alignment.CenterStart
            ) {
                if (state.text.isEmpty()) {
                    Text(
                        text = "TRUMP", color = Color.Gray, style = MaterialTheme.typography.bodyMedium
                    )
                }
                innerTextField() // 渲染实际的输入框
            }
        })
    }
}

@Composable
fun SearchBarIcon(
    imageVector: ImageVector
) {
    Spacer(Modifier.width(10.dp))
    Icon(
        modifier = Modifier
            .size(25.dp)
            .padding(2.dp),
        imageVector = imageVector,
        contentDescription = "chat",
        tint = Color.Unspecified,
    )
}
