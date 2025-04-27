package com.example.kal.ui.screen

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.kal.R
import com.example.kal.ui.theme.KalTheme
import com.example.kal.utils.SharedManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import android.view.WindowManager
import androidx.compose.ui.text.style.TextAlign
import com.example.kal.MainActivity

@Composable
fun ProfileScreen(
    navController: NavHostController
) {
    val view = LocalView.current
    val isStatusBarIconsDark = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val shared = remember { SharedManager(context) }

    // Get user data from SharedManager
    val firstName = shared.getLocalFirstName()
    val lastName = shared.getLocalLastName()
    val address = shared.getLocalAddress()

    // Set the status bar icons visibility to dark or light
    LaunchedEffect(isStatusBarIconsDark.value) {
        val window = (view.context as MainActivity).window
        val controller = window.decorView.systemUiVisibility
        if (isStatusBarIconsDark.value) {
            window.decorView.systemUiVisibility =
                controller or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility =
                controller and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()  // Adds padding for navigation bars
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 72.dp), // Adds top padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Space between firstName and lastName
                ) {
                    Text(
                        text = firstName ?: "",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF151518)
                    )
                    Text(
                        text = lastName ?: "",
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF151518)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.profilescreen),
                    contentDescription = "Profile Icon",
                )
            }


            Text(
                text = address ?: "",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontWeight = FontWeight.Normal,
                color = Color(0xFF75767E)
            )


            Button(
                onClick = {
                    shared.clearUser()
                    navController.navigate("login") {
                        popUpTo(0) // Remove all previous screens from stack
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFE90837),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Выйти из аккаунта",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    KalTheme {
        val navController = rememberNavController()
        ProfileScreen(navController = navController)
    }
}
