package com.example.firstlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState

import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.firstlab.ui.theme.FirstLabTheme
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PagerContent()
        }
    }
}

@Composable
fun MainActivityContent(){
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header(R.drawable.person, "person image")
        Hello("someone")
        Hello("another one")
        LoginTextField()
        PasswordTextField()
        LoginButton {

        }
    }
}

@Composable
fun Hello(name: String){
    Text("Hello $name! Welcome")
}
@Composable
fun Header(image: Int, description: String){
    Image(
        painter = painterResource(image),
        contentDescription = description,
        modifier = Modifier
            .height(180.dp),
//            .fillMaxWidth(),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun LoginButton(clicked: () -> Unit){
    Button(onClick = clicked){
        Text(text = "Вход")
    }
}

@Composable
fun RegistrationButton(clicked: () -> Unit){
    Button(onClick = clicked ) {
        Text(text = "Зарегистрироваться")

    }
}

@Composable
fun LoginTextField(){
    val text = remember { mutableStateOf(TextFieldValue("")) }

    TextField(
        value = text.value,
        onValueChange = {
            if (it.text.length <= 18) {
                text.value = it
            }
        },
        label = { Text("Enter your login") },
        modifier = Modifier
            .height(IntrinsicSize.Min),
        maxLines = 1
    )
}
@Composable
fun PasswordTextField(){
    val text = remember { mutableStateOf(TextFieldValue("")) }
    TextField(value = text.value,
        onValueChange = {
            if (it.text.length <= 18) {
                text.value = it
        }},
        label = { Text("Enter your password")},
        modifier = Modifier
            .height(IntrinsicSize.Min),
        maxLines = 1
    )
}

@Composable
fun ConfirmPasswordTextField(){
    val text = remember { mutableStateOf(TextFieldValue("")) }
    TextField(value = text.value,
        onValueChange = {
            if (it.text.length <= 18) {
                text.value = it
            }},
        label = { Text("Confirm your password")},
        modifier = Modifier
            .height(IntrinsicSize.Min),
        maxLines = 1
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent() {
    var page by remember { mutableStateOf(0) }

//    val pagerState = rememberPagerState(pagerCount = {2})
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) {
        2
    }
    val coroutineScope = rememberCoroutineScope()
    Column (modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Row {
            Button(onClick = {
                coroutineScope.launch{
                    pagerState.scrollToPage(0)
                }
//            page = 0
            }) {
                Text("Вход")
            }
            Button(onClick = {
                coroutineScope.launch {
                    pagerState.scrollToPage(1)
                }
//            page = 1
            }) {
                Text("Регистрация")
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> MainActivityContent()
                1 -> RegisterActivityContent()
                else -> error("Unknown page index: $page")
            }
        }
    }
}

@Composable
fun RegisterActivityContent(){
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Header(R.drawable.person, "person image")
        LoginTextField()
        PasswordTextField()
        ConfirmPasswordTextField()
        RegistrationButton {
        }
    }
}