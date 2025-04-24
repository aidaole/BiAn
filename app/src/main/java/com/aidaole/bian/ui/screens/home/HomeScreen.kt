package com.aidaole.bian.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aidaole.bian.R

@Composable
fun HomeScreen(modifier: Modifier = Modifier, onLoginClicked: () -> Unit = {}) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(painter = painterResource(R.drawable.screen_home), contentDescription = "home_content")

        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Button(onClick = {
                onLoginClicked.invoke()
            }) {
                Text("登陆")
            }
        }
    }
}