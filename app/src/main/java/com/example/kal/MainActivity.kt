package com.example.kal

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.kal.NavGraph
import com.example.kal.R
import com.example.kal.ui.screen.SignUpScreen
import com.example.kal.ui.theme.KalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        // Настройка прозрачного статус-бара
        window.setDecorFitsSystemWindows(false)
        window.statusBarColor = Color.Transparent.toArgb()

        setContent {

            KalTheme {
                val view = LocalView.current
                val isStatusBarIconsDark = remember { mutableStateOf(true) } // Состояние иконок статус-бара

                DisposableEffect(view) {
                    val window = (view.context as MainActivity).window
                    val controller = window.decorView.systemUiVisibility

                    // Обновление цвета иконок статус-бара
                    if (isStatusBarIconsDark.value) {
                        window.decorView.systemUiVisibility =
                            controller or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    } else {
                        window.decorView.systemUiVisibility =
                            controller and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                    }

                    onDispose {}
                }

                // Навигация
                val navController = rememberNavController()
                var selectedTabIndex by remember { mutableStateOf(0) }
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route?: "splash"

                val navigationItems = listOf(
                    NavigationItem(
                        route = "search",
                        icon = R.drawable.searchnav,
                        activeIcon = R.drawable.searchnav_active,
                        label = "Поиск"
                    ),
                    NavigationItem(
                        route = "favorite",
                        icon = R.drawable.favoritenav,
                        activeIcon = R.drawable.favoritenav_active,
                        label = "Избранное"
                    ),
                    NavigationItem(
                        route = "responses",
                        icon = R.drawable.responsesnav,
                        activeIcon = R.drawable.responsesnav_active,
                        label = "Отклики"
                    ),
                    NavigationItem(
                        route = "profile",
                        icon = R.drawable.profilenav,
                        activeIcon = R.drawable.profilenav_active,
                        label = "Профиль"
                    )
                )

                Box(modifier = Modifier.fillMaxSize()) {
                    // Основной контент (NavGraph)
                    NavGraph(
                        navController = navController,
                        isStatusBarIconsDark = isStatusBarIconsDark,
                        view = view
                    )
                    val routes_ex = listOf(
                        "splash",
                        "login",
                        "registration",
                        "active_search"
                    )
                    if (currentRoute !in routes_ex) {
                        // BottomNavigation
                        BottomNavigation(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter) // Явное размещение внизу центра
                                .navigationBarsPadding(),
                            backgroundColor = Color.White // Устанавливаем белый фон для BottomNavigation
                        ) {


                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                navigationItems.forEachIndexed { index, item ->
                                    val isSelected = currentRoute == item.route

                                    Box(
                                        modifier = Modifier
                                            .clickable(
                                                indication = null, // Отключаем стандартное взаимодействие
                                                interactionSource = remember { MutableInteractionSource() }
                                            ) {
                                                selectedTabIndex = index
                                                navController.navigate(item.route) {
                                                    popUpTo(navController.graph.startDestinationId) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Icon(
                                                painter = painterResource(
                                                    id = if (isSelected) item.activeIcon else item.icon
                                                ),
                                                contentDescription = item.label,
                                                tint = Color.Unspecified, // Используем цвет из ресурсов
                                                modifier = Modifier.size(24.dp) // Размер иконки
                                            )
                                            Spacer(modifier = Modifier.height(2.dp)) // Расстояние между иконкой и текстом
                                            Text(
                                                text = item.label,
                                                style = TextStyle(
                                                    fontFamily = FontFamily(Font(R.font.montserrat)), // Устанавливаем шрифт Montserrat
                                                    fontWeight = FontWeight.Medium,
                                                    fontSize = 12.sp, // Устанавливаем размер шрифта 12sp
                                                    color = if (isSelected) Color(0xFF0837E9) else Color(0xFF9A9BA1) // Цвет текста
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}

data class NavigationItem(
    val route: String,
    val icon: Int, // Неактивная иконка
    val activeIcon: Int, // Активная иконка
    val label: String
)