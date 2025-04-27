package com.example.kal.ui.screen

import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kal.MainActivity
import com.example.kal.R
import com.example.kal.db.*
import com.example.kal.db.vacancy.Vacancy
import com.example.kal.ui.items.VacancyItem
import com.example.kal.ui.theme.KalTheme
import com.example.kal.utils.SharedManager
import kotlinx.coroutines.launch

@Composable
fun FavoriteScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val isStatusBarIconsDark = remember { mutableStateOf(true) }
    val context = LocalContext.current
    val shared = SharedManager(context)
    val userId = shared.getLocalUserId()
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    var favoriteVacancies by remember { mutableStateOf(emptyList<Vacancy>()) }

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

    LaunchedEffect(Unit) {
        favoriteVacancies = getUserFavList(userId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(start = 16.dp, top = 72.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Избранные вакансии",
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.montserrat)),
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF151518),
            modifier = Modifier.align(Alignment.Start)
        )

        if (favoriteVacancies.isEmpty()) {
            Spacer(modifier = Modifier.height(194.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.filledheart),
                    contentDescription = "Иллюстрация избранных вакансий",
                    modifier = Modifier.size(104.dp),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "Добавьте вакансии\nв избранное",
                    fontSize = 20.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF151518),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 24.dp)
                )
                Text(
                    text = "Позже вы можете вернуться,\nчтобы откликнуться",
                    fontSize = 12.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF9A9BA1),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
            Spacer(modifier = Modifier.height(127.dp))
            Button(
                onClick = { navController.navigate("search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp)
                    .clip(RoundedCornerShape(26.dp)),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0837E9),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Искать вакансии",
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(favoriteVacancies.size) { index ->
                    val vacancy = favoriteVacancies[index]
                    VacancyItem(
                        vacancy = vacancy,
                        favorite = true,
                        onFavClick = {
                            scope.launch {
                                if (deleteFromFav(userId, vacancy.id.toInt())) {
                                    favoriteVacancies = favoriteVacancies.minus(vacancy)
                                }
                            }
                        },
                        onDeleteClick = {
                            scope.launch {
                                if (deleteFromVac(userId, vacancy.id.toInt())) {
                                    favoriteVacancies = favoriteVacancies.minus(vacancy)
                                }
                            }
                        },
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
                                        println("Вы откликнулись на вакансию: ${vacancy.job}")
                                    }
                                }
                            }
                        }
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Favorite Screen Preview")
@Composable
private fun FavoriteScreenPreview() {
    KalTheme {
        val navController = rememberNavController()
        FavoriteScreen(navController = navController)
    }
}
