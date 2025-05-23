package com.aidaole.bian.features.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocalFireDepartment
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aidaole.bian.core.theme.StockDownColor
import com.aidaole.bian.core.theme.StockUpColor
import com.aidaole.bian.data.entity.StockItem

private const val TAG = "HeaderContent"

@Composable
fun HomeHeaderContent(
    modifier: Modifier,
    stockItems: State<List<StockItem>>,
    onLoginClicked: () -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Spacer(Modifier.height(10.dp))
        Text(
            "欢迎探索数字资产的世界!",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.W800,
                fontSize = 28.sp
            )
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
        StockList(stockItems)
        Spacer(Modifier.height(20.dp))
        Text(
            "查看350余种代币",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.W800
            )
        )
        Spacer(Modifier.height(20.dp))
        HorizontalDivider(Modifier.height(1.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1F))
    }
}

@Composable
private fun StockList(stockItems: State<List<StockItem>>) {
    stockItems.value.forEachIndexed { index, item ->
        StockItemWidget(index, item)
    }
}

@Composable
fun StockItemWidget(index: Int, stockItem: StockItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(66.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stockItem.name,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
        Icon(
            imageVector = Icons.Rounded.LocalFireDepartment,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null
        )
        Spacer(Modifier.weight(1f))
        Column {
            Text(
                "${stockItem.price}",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.W800)
            )
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