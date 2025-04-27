package com.example.kal.ui.screen

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kal.R
import com.example.kal.db.*
import com.example.kal.db.vacancy.Vacancy
import com.example.kal.ui.items.VacancyItem
import com.example.kal.ui.theme.KalTheme
import com.example.kal.utils.SharedManager
import kotlinx.coroutines.launch

@Composable
fun ActiveSearchScreen(
    navController: NavHostController
) {
    var searchText by remember { mutableStateOf("") }
    var vacancies by remember { mutableStateOf(emptyList<Vacancy>()) }
    var userFavList by remember { mutableStateOf(emptyList<Vacancy>()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val shared = SharedManager(context)
    val userId = shared.getLocalUserId()
    val view = LocalView.current
    val isStatusBarIconsDark = remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }

    // Настройка цвета статус-бара и иконок
    LaunchedEffect(Unit) {
        isStatusBarIconsDark.value = true
        val window = (context as? android.app.Activity)?.window
        window?.statusBarColor = Color.White.toArgb()
        val controller = window?.decorView?.systemUiVisibility ?: 0
        if (isStatusBarIconsDark.value) {
            window?.decorView?.systemUiVisibility =
                controller or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window?.decorView?.systemUiVisibility =
                controller and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
    }

    // Загрузка вакансий и избранных
    LaunchedEffect(Unit) {
        vacancies = getAllVacancies()
        userFavList = getUserFavList(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp, start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.backarrow),
                contentDescription = "Назад",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { navController.popBackStack() }
            )
            Text(
                text = "Поиск",
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF151518)
            )
            Spacer(modifier = Modifier.width(24.dp))
        }

        // Поисковое поле
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            BasicTextField(
                value = searchText,
                onValueChange = { newText -> searchText = newText },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(2.dp, Color(0xFF0837E9), RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(top = 16.dp, start = 48.dp, end = 48.dp),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    color = Color.Black
                ),
                singleLine = true
            )

            // Иконка поиска
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.search),
                contentDescription = "Поиск",
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 16.dp),
                colorFilter = ColorFilter.tint(Color(0xFF75767E))
            )

            // Плейсхолдер
            if (searchText.isEmpty()) {
                Text(
                    text = "Должность",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFC1C1C5),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 48.dp)
                )
            }

            // Иконка очистки
            if (searchText.isNotEmpty()) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.close),
                    contentDescription = "Очистить",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                        .clickable { searchText = "" }
                )
            }
        }

        // Список вакансий
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            val filteredVacancies = if (searchText.isEmpty()) {
                vacancies
            } else {
                vacancies.filter {
                    it.job.contains(searchText, ignoreCase = true) ||
                            it.organization.contains(searchText, ignoreCase = true)
                }
            }

            items(filteredVacancies.size) { index ->
                val vacancy = filteredVacancies[index]
                if (index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                VacancyItem(
                    vacancy = vacancy,
                    favorite = userFavList.contains(vacancy),
                    onFavClick = {
                        scope.launch {
                            if (!userFavList.contains(vacancy)) {
                                val response = addToFav(userId, vacancy.id.toInt())
                                if (response) userFavList = userFavList.plus(vacancy)
                            } else {
                                val response = deleteFromFav(userId, vacancy.id.toInt())
                                if (response) userFavList = userFavList.minus(vacancy)
                            }
                        }
                    },
                    onDeleteClick = { /* Можно потом добавить удаление */ },
                    onClick = {
                        navController.navigate("vacancyInfo/${vacancy.id}")
                    },
                    onRespondClick = {
                        if (!loading) {
                            scope.launch {
                                loading = true
                                val response = addToVac(userId, vacancy.id.toInt())
                                loading = false
                                if (response) {
                                    println("Отклик на вакансию успешно отправлен: ${vacancy.job}")
                                }
                            }
                        }
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(73.dp))
            }
        }
    }
}

@Preview(showBackground = true, name = "Active Search Screen Preview")
@Composable
private fun ActiveSearchScreenPreview() {
    KalTheme {
        val navController = rememberNavController()
        ActiveSearchScreen(navController = navController)
    }
}
