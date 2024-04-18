package com.example.firstlab

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//import android.content.Context
//import kotlin.coroutines.jvm.internal.CompletedContinuation.context

//import android.content.Intent

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val coroutineScope = rememberCoroutineScope()
            val pagerState = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) {
                2
            }
//            UserRepositoryProvider {
                val userDao = remember { UserDatabase.getDatabase(context = applicationContext).userDao }
                val userRepository = UserRepository(userDao)
                PagerContent(pagerState, coroutineScope, userRepository)
//            }
        }
    }
}

//@Composable
//fun UserRepositoryProvider(content: @Composable () -> Unit) {
//    val context = LocalContext.current
//    val userRepository = remember { UserRepository(context) }
//    content()
//}

@Composable
fun MainActivityContent(
    loginValue: MutableState<TextFieldValue>,
    passwordValue: MutableState<TextFieldValue>,
    userDao: UserRepository,
    context: Context
){
    val coroutineScope = rememberCoroutineScope()
    val loginOrPasswordMismatchError = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Header(R.drawable.person, "person image")
        LoginTextField(loginValue)
        PasswordTextField(passwordValue)
        if (loginOrPasswordMismatchError.value){
            LoginOrPasswordMatchError()
        }
        LoginButton(
            onLoginClicked = {
                val login = loginValue.value.text
                val password = passwordValue.value.text


                println("Login: $login")
                println("Password: $password")

                coroutineScope.launch{
                    val existingUser = withContext(Dispatchers.IO){
                        userDao.getUser(login)
                    }
                    if (existingUser != null){
                        if (existingUser.userPassword == password){
                            withContext(Dispatchers.Main) {
                                val intent = Intent(context, SecondActivity::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }else{
                        loginOrPasswordMismatchError.value = true
                    }
                }
            }
        )
    }
}

@Composable
fun LoginOrPasswordMatchError(){
    Text(
        text = "Пользователь с таким логином не существует, либо пароль введён не правильный",
        color = Color.Red,
        modifier = Modifier.padding(vertical = 8.dp)
    )
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
fun LoginButton(
    onLoginClicked: () -> Unit
){
    Button(onClick = onLoginClicked
    ){
        Text(text = "Вход")
    }
}

@Composable
fun RegistrationButton(
    onRegistrationClicked: () -> Unit
){
    Button(
        onClick = onRegistrationClicked
    ) {
        Text(text = "Зарегистрироваться")
    }
}

@Composable
fun LoginTextField(loginValue: MutableState<TextFieldValue>) {
    TextField(
        value = loginValue.value,
        onValueChange = { newValue ->
            if (newValue.text.length <= 18) {
                loginValue.value = newValue
            }
        },
        label = { Text("Enter your login") },
        modifier = Modifier
            .height(IntrinsicSize.Min),
        maxLines = 1
    )
}

@Composable
fun PasswordTextField(passwordValue: MutableState<TextFieldValue>) {
    TextField(
        value = passwordValue.value,
        onValueChange = { newValue ->
            if (newValue.text.length <= 18) {
                passwordValue.value = newValue
            }
        },
        label = { Text("Enter your password")},
        modifier = Modifier
            .height(IntrinsicSize.Min),
        maxLines = 1
    )
}

@Composable
fun ConfirmPasswordTextField(confirmPasswordValue: MutableState<TextFieldValue>) {
    TextField(
        value = confirmPasswordValue.value,
        onValueChange = { newValue ->
            if (newValue.text.length <= 18) {
                confirmPasswordValue.value = newValue
            }
        },
        label = { Text("Confirm your password")},
        modifier = Modifier
            .height(IntrinsicSize.Min),
        maxLines = 1
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PagerContent(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    userDao: UserRepository,
) {
    val context = LocalContext.current

    val loginValue = remember { mutableStateOf(TextFieldValue("")) }
    val passwordValue = remember { mutableStateOf(TextFieldValue("")) }
    val confirmPasswordValue = remember { mutableStateOf(TextFieldValue("")) }

    Column(
        modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally,) {
        Row {
            Button(onClick = {
                coroutineScope.launch{
                    pagerState.scrollToPage(0)
                }
            }) {
                Text("Вход")
            }
            Button(onClick = {
                coroutineScope.launch {
                    pagerState.scrollToPage(1)
                }
            }) {
                Text("Регистрация")
            }
        }

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> MainActivityContent(loginValue,
                    passwordValue,
                    userDao = userDao,
                    context = context)
                1 -> RegisterActivityContent(
                    loginValue = loginValue,
                    passwordValue = passwordValue,
                    confirmPasswordValue = confirmPasswordValue,
                    userDao = userDao,
                    context = context
                )
                else -> error("Unknown page index: $page")
            }
        }
    }
}
@Composable
fun RegisterActivityContent(
    loginValue: MutableState<TextFieldValue>,
    passwordValue: MutableState<TextFieldValue>,
    confirmPasswordValue: MutableState<TextFieldValue>,
    userDao: UserRepository,
    context: Context
){
    val coroutineScope = rememberCoroutineScope()
    val passwordMismatchError = remember { mutableStateOf(false) }
    val loginMatchError = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {
        Header(R.drawable.person, "person image")
        LoginTextField(loginValue)
        PasswordTextField(passwordValue)
        ConfirmPasswordTextField(confirmPasswordValue)

        if (passwordMismatchError.value) {
            PasswordMismatchError()
        }
        if (loginMatchError.value){
            LoginMatchError()
        }

        RegistrationButton(
            onRegistrationClicked = {
                val login = loginValue.value.text
                val password = passwordValue.value.text
                val confirmPassword = confirmPasswordValue.value.text

                println("Login: $login")
                println("Password: $password")
                println("Confirm Password: $confirmPassword")

                coroutineScope.launch {
                    val existingUser = withContext(Dispatchers.IO) { userDao.getUser(login) }
                    if (existingUser != null) {
                        withContext(Dispatchers.Main) {
                            loginMatchError.value = true
                        }
                    } else {
                        if (password == confirmPassword) {
                            println("Passwords match!")
                            val user = User(userName = login, userPassword = password)
                            passwordMismatchError.value = false

                            withContext(Dispatchers.IO) {
                                userDao.insert(user)
                            }

                            withContext(Dispatchers.Main) {
                                val intent = Intent(context, SecondActivity::class.java)
                                context.startActivity(intent)
                            }
                        } else {
                            println("Passwords do not match!")
                            withContext(Dispatchers.Main) {
                                passwordMismatchError.value = true
                            }
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun PasswordMismatchError() {
    Text(
        text = "Пароли не совпадают",
        color = Color.Red,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun LoginMatchError(){
    Text(
        text = "Существует пользователь с таким логином",
        color = Color.Red,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}