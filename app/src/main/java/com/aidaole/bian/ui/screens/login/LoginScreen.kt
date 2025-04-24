package com.aidaole.bian.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aidaole.bian.R
import com.aidaole.bian.ui.theme.BiAnTheme
import com.aidaole.bian.ui.theme.ButtonBorderColor
import com.aidaole.bian.ui.theme.InputFieldBg

@Preview
@Composable
private fun LoginScreenPrev() {
    BiAnTheme  {
        LoginScreen()
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier, onCloseClicked: () -> Unit = {}, onCallClicked: () -> Unit = {}
) {

    Box(
        modifier = modifier.fillMaxSize()
    ) {
//        Image(painter = painterResource(R.drawable.screen_login), contentDescription = "login_screen")
        val inputState = rememberTextFieldState()
        var checkedState by remember { mutableStateOf(false) }

        Column(
            modifier
                .fillMaxSize()
                .padding(vertical = 40.dp, horizontal = 20.dp)

        ) {
            Row(
                modifier = modifier.fillMaxWidth()
            ) {
                Image(imageVector = Icons.Default.Close, contentDescription = "close", modifier = Modifier.clickable {
                    onCloseClicked.invoke()
                })
                Spacer(modifier = Modifier.weight(1f))
                Image(imageVector = Icons.Default.Call, contentDescription = "close", modifier = Modifier.clickable {
                    onCallClicked.invoke()
                })
            }

            Spacer(Modifier.height(20.dp))
            Text(
                "欢迎来到Bank", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.W800
            )

            Spacer(Modifier.height(30.dp))
            Text("邮箱/手机号码", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(10.dp))
            InputWidget(state = inputState, hint = "邮件/手机号码")
            Spacer(Modifier.height(10.dp))
            Row(
                modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = checkedState, onCheckedChange = {
                    checkedState = !checkedState
                })
                Text(
                    "创建账户即表示我同意币安的《服务条款》和《隐私声明》。", style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(20.dp))
            Button(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp), shape = RoundedCornerShape(10.dp), onClick = {}) {
                Text("下一步")
            }
            Spacer(Modifier.height(20.dp))
            OrWidget()
            Spacer(Modifier.height(20.dp))
            WithIconButton(
                text = "通过Google登陆", icon = painterResource(R.drawable.google)
            )
            Spacer(Modifier.height(10.dp))
            WithIconButton(
                text = "通过Telgram登陆", icon = painterResource(R.drawable.telegram)
            )
            Spacer(Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("创建企业账户",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.W800,
                    modifier = Modifier.clickable { })
                Spacer(Modifier.width(5.dp))
                Text("或")
                Spacer(Modifier.width(5.dp))
                Text("登陆",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.W800,
                    modifier = Modifier.clickable { })
            }
        }
    }
}

@Composable
fun WithIconButton(
    text: String = "", icon: Painter, onClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .clickable {
                onClicked.invoke()
            }
            .border(width = 1.dp, color = ButtonBorderColor, shape = RoundedCornerShape(10.dp))
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            modifier = Modifier.size(20.dp), painter = icon, contentDescription = "google", tint = Color.Unspecified
        )
        Text(
            text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OrWidget() {
    Row(
        modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text("或")
        Spacer(modifier = Modifier.width(20.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f)
        )

    }
}

@Composable
fun InputWidget(modifier: Modifier = Modifier, state: TextFieldState, hint: String = "") {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(color = InputFieldBg)
            .padding(vertical = 16.dp, horizontal = 20.dp),
    ) {
        BasicTextField(textStyle = MaterialTheme.typography.bodyLarge,
            modifier = modifier
                .fillMaxWidth()
                .height(28.dp),
            state = state,
            decorator = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = hint, color = Color.Gray
                        )
                    }
                    innerTextField() // 渲染实际的输入框
                }
            })
    }
}
