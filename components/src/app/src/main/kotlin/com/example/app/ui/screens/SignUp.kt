package com.example.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpScreen() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isTermsAccepted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 74.dp, bottom = 74.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Регистрация",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF151518),
            letterSpacing = 0.02.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // First Name Field
        Text(
            text = "Имя",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF151518)
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp),
            placeholder = { Text("Имя", color = Color(0xFFC1C1C5)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                unfocusedBorderColor = Color(0xFFC1C1C5)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Last Name Field
        Text(
            text = "Фамилия",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF151518)
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp),
            placeholder = { Text("Фамилия", color = Color(0xFFC1C1C5)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                unfocusedBorderColor = Color(0xFFC1C1C5)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Email Field
        Text(
            text = "Почта",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF151518)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp),
            placeholder = { Text("example@gmail.com", color = Color(0xFFC1C1C5)) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                unfocusedBorderColor = Color(0xFFC1C1C5)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Password Field
        Text(
            text = "Пароль",
            modifier = Modifier.align(Alignment.Start),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF151518)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .height(48.dp),
            placeholder = { Text("∗∗∗∗∗∗∗∗∗∗", color = Color(0xFFC1C1C5)) },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                        tint = Color(0xFFC1C1C5)
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                unfocusedBorderColor = Color(0xFFC1C1C5)
            ),
            shape = RoundedCornerShape(8.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Terms Checkbox
        Row(
            modifier = Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isTermsAccepted,
                onCheckedChange = { isTermsAccepted = it },
                modifier = Modifier
                    .size(20.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFF75767E),
                        shape = RoundedCornerShape(8.dp)
                    ),
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF0837E9),
                    uncheckedColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Даю согласие на обработкуперсональных данных",
                color = Color(0xFF75767E),
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Register Button
        Button(
            onClick = { /* Handle registration */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0837E9)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "Зарегистрироваться",
                color = Color(0xFFE7E8FF),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(91.dp))

        // Login Link
        Row(
            modifier = Modifier.padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Уже есть аккаунт? ",
                color = Color(0xFF75767E),
                fontSize = 16.sp
            )
            Text(
                text = "Войти",
                color = Color(0xFF151518),
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { /* Handle login navigation */ }
            )
        }
    }
}