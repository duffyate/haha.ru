package com.example.kal.ui.screen

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
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
import com.example.kal.MainActivity
import com.example.kal.R
import com.example.kal.db.addToFav
import com.example.kal.db.addToVac
import com.example.kal.db.deleteFromFav
import com.example.kal.db.getUserFavList
import com.example.kal.db.getVacancyById
import com.example.kal.db.vacancy.Vacancy
import com.example.kal.ui.theme.KalTheme
import com.example.kal.utils.MyDateSerializer.getRelativeTimeString
import com.example.kal.utils.SharedManager
import kotlinx.coroutines.launch

@Composable
fun VacancyInfoScreen(
    vacId: Int,
    navController: NavHostController,
    isStatusBarIconsDark: MutableState<Boolean>,
    view: View
) {
    var vac by remember { mutableStateOf<Vacancy?>(null) }
    val context = LocalContext.current
    val shared = SharedManager(context)
    val userid = shared.getLocalUserId()
    var userFavList by remember { mutableStateOf(emptyList<Vacancy>()) }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    // Настройка статус-бара
    LaunchedEffect(Unit) {
        isStatusBarIconsDark.value = true
        val window = (view.context as MainActivity).window
        window.statusBarColor = Color.White.toArgb()
        val controller = window.decorView.systemUiVisibility
        if (isStatusBarIconsDark.value) {
            window.decorView.systemUiVisibility =
                controller or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility =
                controller and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        vac = getVacancyById(vacId)
        userFavList = getUserFavList(userid)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .statusBarsPadding()
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.backarrow),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { navController.popBackStack() },
                        contentScale = ContentScale.Fit
                    )
                    Box(
                        modifier = Modifier.padding(end = 16.dp)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(
                                if (!userFavList.contains(vac)) R.drawable.favorite2 else R.drawable.filledheart
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterEnd)
                                .clickable(null, null) {
                                    if (!loading && vac != null) {
                                        scope.launch {
                                            loading = true
                                            if (!userFavList.contains(vac)) {
                                                val response = addToFav(userid, vac!!.id.toInt())
                                                if (response) userFavList = userFavList.plus(vac!!)
                                            } else {
                                                val response = deleteFromFav(userid, vac!!.id.toInt())
                                                if (response) userFavList = userFavList.minus(vac!!)
                                            }
                                            loading = false
                                        }
                                    }
                                },
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = vac?.job ?: "",
                        fontSize = 20.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF151518)
                    )
                    Text(
                        text = "от ${vac?.money} рублей за месяц",
                        modifier = Modifier.padding(top = 16.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF151518)
                    )
                    Text(
                        text = vac?.organization ?: "",
                        modifier = Modifier.padding(top = 16.dp),
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        lineHeight = 25.6.sp
                    )
                }
            }

            item {
                Section(title = "Обязанности:", content = vac?.duties ?: "")
                Section(title = "Требования:", content = vac?.requirements ?: "")
                Section(title = "Условия:", content = vac?.conditions ?: "")
                Section(title = "Адрес, где предстоит работать:", content = vac?.address ?: "")
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 72.dp, start = 16.dp, end = 16.dp)
                ) {
                    Text(
                        text = vac?.date?.getRelativeTimeString() ?: "",
                        fontSize = 16.sp,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Medium,
                        lineHeight = 25.6.sp,
                        color = Color(0xFF75767E)
                    )
                }
            }

            // Кнопка "Откликнуться"
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (!loading && vac != null) {
                            scope.launch {
                                loading = true
                                val response = addToVac(userid, vac!!.id.toInt())
                                loading = false
                                if (response) {
                                    println("Успешный отклик на вакансию: ${vac!!.job}")
                                    navController.popBackStack()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF0837E9)
                    )
                ) {
                    Text(
                        text = "Откликнуться",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
fun Section(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = title,
            fontFamily = FontFamily(Font(R.font.montserrat)),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF151518)
        )
        Text(
            text = content.trimIndent(),
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 12.sp,
            fontFamily = FontFamily(Font(R.font.montserrat)),
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            lineHeight = 19.sp
        )
    }
}

@Preview(showBackground = true, name = "Vacancy Info Screen Preview")
@Composable
private fun VacancyInfoScreenPreview() {
    KalTheme {
        val navController = rememberNavController()
        val isStatusBarIconsDark = remember { mutableStateOf(true) }
        VacancyInfoScreen(
            vacId = 0,
            navController = navController,
            isStatusBarIconsDark = isStatusBarIconsDark,
            view = LocalView.current
        )
    }
}
