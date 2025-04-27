package com.example.kal.ui.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import com.example.kal.R
import com.example.kal.ui.theme.KalTheme
import com.example.kal.utils.SharedManager

@Composable
fun SplashScreen(navController: NavHostController) {
    var start by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sharedManager = SharedManager(context)
    val id = sharedManager.getLocalUserId()

    LaunchedEffect(Unit) {
        start = true
        delay(2000) // 2 секунды
        val route = if (id == -1) "login" else "search"
        navController.navigate(route) {
            popUpTo("splash") { inclusive = true } // Удаляем SplashScreen из стека
        }
    }

    val alpha by animateFloatAsState(
        targetValue = if (start) 1.0f else 0f,
        animationSpec = tween(500)
    )

    // Фоновый цвет экрана
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0837E9)), // Синий фон
        contentAlignment = Alignment.Center
    ) {
        // Логотип по центру
        Image(
            painter = painterResource(id = R.drawable.splash_haha),
            contentDescription = "Логотип",
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
                .alpha(alpha)// Размер логотипа
        )

        // Текст "найди работу"
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 148.dp)
                .alpha(alpha), // Отступ снизу
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "найди работу",
                fontFamily = FontFamily(Font(R.font.benzin)),
                fontSize = 32.sp,
                color = Color(0xFFE7E8FF),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha)
            )

            // Подтекст "(ну надо)"
            Text(
                text = "(ну надо)",
                fontFamily = FontFamily(Font(R.font.benzin)),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0x33E7E8FF),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .alpha(alpha)
            )
        }
    }
}

// Preview для SplashScreen
@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    KalTheme {
        SplashScreen(navController = rememberNavController())
    }
}
