package com.aidaole.bian.features.home.widget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.rounded.ChairAlt
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.Message
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aidaole.bian.core.theme.TextGray

@Preview
@Composable
fun FeedExploreitemWidgetPreview(modifier: Modifier = Modifier) {
    FeedExploreItemWidget()
}

@Composable
fun FeedExploreItemWidget(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.height(150.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(10.dp))
            Icon(Icons.Outlined.AccountCircle, contentDescription = null)
            Spacer(Modifier.width(10.dp))
            Column {
                Text("UserName", style = MaterialTheme.typography.bodyMedium)
                Text("time", style = MaterialTheme.typography.bodySmall, color = TextGray)
            }
            Spacer(modifier = Modifier.weight(1F))
            Button(
                onClick = {}, modifier = Modifier
                    .padding(0.dp)
                    .height(20.dp), shape = RoundedCornerShape(5.dp)
            ) {
                Text("关注", fontSize = 10.sp)
            }
            Spacer(Modifier.width(10.dp))
            Icon(Icons.Default.Menu, contentDescription = null)
            Spacer(Modifier.width(10.dp))
        }

        Text("content")

        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Icon(
                Icons.Rounded.Image, contentDescription = null
            )
            Icon(
                Icons.Rounded.Message, contentDescription = null
            )
            Icon(
                Icons.Rounded.FavoriteBorder, contentDescription = null
            )
            Icon(
                Icons.Rounded.ChairAlt, contentDescription = null
            )
        }
    }
}

@Composable
fun BottomIcons(
    imageVector: ImageVector
) {
    Icon(
        imageVector,
        modifier = Modifier.size(10.dp),
        contentDescription = null, tint = Color.DarkGray,
    )
}