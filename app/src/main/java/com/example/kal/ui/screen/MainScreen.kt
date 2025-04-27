package com.example.kal.ui.screen

import android.annotation.SuppressLint
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
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
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.kal.MainActivity
import com.example.kal.R
import com.example.kal.db.*
import com.example.kal.db.vacancy.Vacancy
import com.example.kal.ui.items.ShimmerLoading
import com.example.kal.ui.items.VacancyItem
import com.example.kal.ui.theme.KalTheme
import com.example.kal.utils.SharedManager
import kotlinx.coroutines.launch

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun MainScreen(
    isStatusBarIconsDark: MutableState<Boolean>,
    view: View,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shared = SharedManager(context)
    val userId = shared.getLocalUserId()

    var userFavList by remember { mutableStateOf(emptyList<Vacancy>()) }
    var vacancies by remember { mutableStateOf(emptyList<Vacancy>()) }
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }
    val height = 5
    val barHeight = 40
    val listState = rememberLazyListState()

    val firstVisibleItemIndex = remember { derivedStateOf { listState.firstVisibleItemIndex } }
    val firstVisibleItemScrollOffset = remember { derivedStateOf { listState.firstVisibleItemScrollOffset } }
    val threshold = height * 2 - 10
    expanded = (firstVisibleItemIndex.value > 0 || firstVisibleItemScrollOffset.value > threshold)

    // Обновление цвета иконок статус-бара
    LaunchedEffect(expanded) {
        isStatusBarIconsDark.value = expanded
        val window = (view.context as MainActivity).window
        val controller = window.decorView.systemUiVisibility
        if (isStatusBarIconsDark.value) {
            window.decorView.systemUiVisibility =
                controller or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            window.decorView.systemUiVisibility =
                controller and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.statusBarColor = Color.Transparent.toArgb()
    }

    // Загружаем вакансии и избранные при запуске
    LaunchedEffect(Unit) {
        userFavList = getUserFavList(userId)
        vacancies = getAllVacancies()
    }

    val alpha by animateFloatAsState(
        targetValue = if (!expanded) 1.0f else 0.0f,
        animationSpec = tween(500),
        label = ""
    )
    val corner by animateIntAsState(
        targetValue = if (!expanded) 32 else 0,
        animationSpec = tween(500),
        label = ""
    )
    val offsetY by animateIntAsState(
        targetValue = if (expanded) 0 else barHeight,
        animationSpec = tween(500),
        label = ""
    )
    val baseHeaderHeight = 185.dp
    val headerHeight by animateDpAsState(
        targetValue = if (listState.firstVisibleItemScrollOffset > 50 || listState.firstVisibleItemIndex > 0) 0.dp else baseHeaderHeight,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .animateContentSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 45.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.haharu),
                    contentDescription = "Логотип",
                    modifier = Modifier.padding(start = 17.dp)
                )
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.notification),
                    contentDescription = "Уведомления",
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(52.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable(null, null) {
                            navController.navigate("active_search")
                        }
                        .border(
                            width = 1.dp,
                            color = Color(0xFFE7E8FF),
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                        .height(52.dp)
                        .padding(horizontal = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.search),
                            contentDescription = "Поиск",
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Text(
                            text = "Должность",
                            color = Color(0xFF8991FE),
                            fontWeight = FontWeight.Normal,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat))
                        )
                    }
                }
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.filter),
                    contentDescription = "Фильтр",
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(52.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = headerHeight),
            state = listState
        ) {
            item { Spacer(Modifier.height(height.dp)) }
            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxSize()
                        .heightIn(min = 800.dp)
                        .padding(bottom = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
                    shape = RoundedCornerShape(topStart = corner.dp, topEnd = corner.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Доступные вакансии",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            fontFamily = FontFamily(Font(R.font.montserrat))
                        )

                        if (vacancies.isNotEmpty()) {
                            vacancies.forEach { vac ->
                                VacancyItem(
                                    vacancy = vac,
                                    favorite = userFavList.contains(vac),
                                    onFavClick = {
                                        if (!loading) {
                                            scope.launch {
                                                loading = true
                                                if (!userFavList.contains(vac)) {
                                                    val response = addToFav(userId, vac.id.toInt())
                                                    if (response) userFavList = userFavList.plus(vac)
                                                } else {
                                                    val response = deleteFromFav(userId, vac.id.toInt())
                                                    if (response) userFavList = userFavList.minus(vac)
                                                }
                                                loading = false
                                            }
                                        }
                                    },
                                    onDeleteClick = {
                                        scope.launch {
                                            if (deleteFromVac(userId, vac.id.toInt())) {
                                                vacancies = vacancies.minus(vac)
                                            }
                                        }
                                    },
                                    onClick = {
                                        navController.navigate("vacancyInfo/${vac.id}")
                                    },
                                    onRespondClick = {
                                        if (!loading) {
                                            scope.launch {
                                                loading = true
                                                val response = addToVac(userId, vac.id.toInt())
                                                loading = false
                                                if (response) {
                                                    println("Успешный отклик на вакансию: ${vac.job}")
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                        Spacer(Modifier.height(72.dp))
                    }
                }
            }
        }

        if (expanded) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(barHeight.dp)
                    .background(MaterialTheme.colorScheme.background)
                    .alpha(alpha)
                    .offset(y = offsetY.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TestPreview() {
    KalTheme {
        val isStatusBarIconsDark = remember { mutableStateOf(true) }
        val navController = rememberNavController() // Создаем фиктивный navController
        MainScreen(
            isStatusBarIconsDark = isStatusBarIconsDark,
            view = LocalView.current,
            navController = navController
        )
    }
}