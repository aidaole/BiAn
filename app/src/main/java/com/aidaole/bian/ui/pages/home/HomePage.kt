package com.aidaole.bian.ui.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.MailOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidaole.bian.R
import com.aidaole.bian.ui.theme.InputFieldBg

@Preview
@Composable
private fun HomePagePreview() {
    HomePage()
}

@Composable
fun HomePage(modifier: Modifier = Modifier, onLoginClicked: () -> Unit = {}) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            HomeTopBar()
            Spacer(Modifier.height(30.dp))
            Text(
                "欢迎探索数字资产的世界!",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.W800)
            )
            Spacer(Modifier.height(30.dp))
            Button(modifier = Modifier.width(200.dp), shape = RoundedCornerShape(10.dp), onClick = {
                onLoginClicked.invoke()
            }) {
                Text("注册/登陆", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(30.dp))
            QuotesWidget()
        }
    }
}

@Composable
fun QuotesWidget(modifier: Modifier = Modifier) {
    LazyColumn {

    }
}

@Composable
fun HomeTopBar() {
    Row(
        modifier = Modifier
            .height(80.dp)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(R.drawable.google),
            contentDescription = "bian",
            tint = Color.Unspecified,
        )
        Spacer(Modifier.width(20.dp))
        BiAnSearchBar(
            modifier = Modifier.weight(1F)
        )
        SearchBarIcon(imageVector = Icons.Default.Call)
        SearchBarIcon(imageVector = Icons.Default.MailOutline)
        SearchBarIcon(imageVector = Icons.Default.Person)
        SearchBarIcon(imageVector = Icons.Default.Info)
    }
}

@Composable
fun BiAnSearchBar(modifier: Modifier) {
    val state = rememberTextFieldState()
    Row(
        modifier = modifier
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
        BasicTextField(
            state = state,
            decorator = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = "TRUMP", color = Color.Gray
                        )
                    }
                    innerTextField() // 渲染实际的输入框
                }
            }
        )
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
