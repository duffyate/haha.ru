package com.example.kal.ui.screen

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kal.R
import com.example.kal.db.authUser
import com.example.kal.db.getUserByEmail
import com.example.kal.db.getUserIdByEmail
import com.example.kal.db.registerUser
import com.example.kal.db.user.User
import com.example.kal.ui.theme.KalTheme
import com.example.kal.utils.SharedManager
import kotlinx.coroutines.launch


@Composable
fun SignInScreen(navController: NavController) {
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManger = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .clickable(null, null) {
                keyboard?.hide()
                focusManger.clearFocus()
            },
        contentAlignment = Alignment.TopCenter
    ) {
        SignInForm(navController)
    }
}

@Composable
fun SignInForm(
    navController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val sharedManager = SharedManager(context)

    Column(
        modifier = Modifier
            .width(360.dp)
            .background(Color.White)
            .padding(top = 72.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Войти",
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            color = Color(0xFF151518),
            letterSpacing = 0.02.sp,
            fontFamily = FontFamily(Font(R.font.montserrat))
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Email Field
            FormField(
                label = "Почта",
                value = email,
                onValueChange = { email = it },
                placeholder = "example@gmail.com",
                keyboardType = KeyboardType.Email,
                error = emailError
            )
            // Password Field
            PasswordField(
                label = "Пароль",
                value = password,
                onValueChange = { password = it },
                placeholder = "∗∗∗∗∗∗∗∗∗∗",
                isPasswordVisible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = it },
                error = passwordError
            )


            // Submit Button
            Button(
                onClick = {
                    emailError = if (email.isEmpty()) {
                        "Email обязателен"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        "Неверный формат email"
                    } else null
                    passwordError = if (password.isEmpty()) {
                        "Пароль обязателен"
                    } else if (password.length < 8) {
                        "Пароль должен содержать минимум 8 символов"
                    } else null
                    // Process if all valid
                    if (emailError == null && passwordError == null) {
                        scope.launch {
                            val response = authUser(email, password)
                            println("Response: $response")
                            if (response) {
                                val id = getUserIdByEmail(email)
                                println("Id: $id")
                                if (id != -1) {
                                    sharedManager.saveLocalUserId(id)
                                    navController.navigate("search")
                                }
                            } else {
                                return@launch
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0837E9)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Войти",
                    color = Color(0xFFE7E8FF),
                    fontFamily = FontFamily(Font(R.font.montserrat))
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 366.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Нету аккаунта? ",
                    color = Color(0xFF75767E),
                    fontFamily = FontFamily(Font(R.font.montserrat))
                )
                Text(
                    text = "Создать",
                    color = Color(0xFF151518),
                    textDecoration = TextDecoration.Underline,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    modifier = Modifier
                        .clickable(null, null) {
                            navController.navigate("registration")
                        }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Preview(showBackground = true, name = "Sign Up Screen Preview")
@Composable
private fun SignInScreenPreview() {
    KalTheme {
        SignInScreen(rememberNavController())
    }
}