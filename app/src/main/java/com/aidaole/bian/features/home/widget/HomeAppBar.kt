package com.aidaole.bian.features.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.aidaole.bian.R
import com.aidaole.bian.core.theme.InputFieldBg

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeAppBar(onSizeChanged: (IntSize) -> Unit = {}) {
    androidx.compose.material3.TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged { size ->
                onSizeChanged.invoke(size)
            },
        title = { BiAnSearchBar() },
        navigationIcon = {
            Row {
                Spacer(Modifier.width(10.dp))
                Icon(
                    modifier = Modifier
                        .size(35.dp)
                        .padding(5.dp),
                    contentDescription = "",
                    painter = painterResource(R.drawable.google),
                    tint = Color.Unspecified
                )
            }
        },
        actions = {
            SearchBarIcon(imageVector = Icons.Outlined.CameraAlt)
            SearchBarIcon(imageVector = Icons.Default.Call)
            SearchBarIcon(imageVector = Icons.Outlined.Email)
            SearchBarIcon(imageVector = Icons.Default.AddCard)
            Spacer(Modifier.width(20.dp))
        }
    )
}

@Composable
fun BiAnSearchBar(modifier: Modifier = Modifier) {
    val state = rememberTextFieldState()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(color = InputFieldBg)
            .padding(horizontal = 5.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
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
                        text = "TRUMP",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
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
