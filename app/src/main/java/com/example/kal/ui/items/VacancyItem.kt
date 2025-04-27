package com.example.kal.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kal.R
import com.example.kal.db.vacancy.Vacancy
import com.example.kal.utils.MyDateSerializer.getRelativeTimeString

@Composable
fun VacancyItem(
    vacancy: Vacancy,
    favorite: Boolean,
    onFavClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onClick: () -> Unit,
    onRespondClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = Color(0xFFC1C1C5), shape = RoundedCornerShape(size = 16.dp))
            .fillMaxWidth()
            .height(256.dp)
            .padding(16.dp)
            .clickable { onClick() } // Обработка нажатия
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = vacancy.job,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF151518)
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    imageVector = ImageVector.vectorResource(
                        if (!favorite) R.drawable.favorite else R.drawable.filledheart
                    ),
                    contentDescription = "Иконка избранного",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(null, null) {
                        onFavClick()
                    }
                )
            }

            // Описание зарплаты
            Text(
                text = "от ${vacancy.money} рублей за месяц",
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color(0xFF151518),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Компания и местоположение
            Text(
                text = vacancy.organization,
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontWeight = FontWeight(400),
                fontSize = 14.sp,
                color = Color(0xFF151518),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            Text(
                text = vacancy.address,
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontWeight = FontWeight(400),
                fontSize = 14.sp,
                color = Color(0xFF151518),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Дата публикации
            Text(
                text = vacancy.date.getRelativeTimeString(),
                fontFamily = FontFamily(Font(R.font.montserrat)),
                fontWeight = FontWeight(400),
                fontSize = 14.sp,
                color = Color(0xFF151518),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Кнопка "Откликнуться" и иконка избранного
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { onRespondClick() },
                    modifier = Modifier
                        .width(268.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0837E9))
                ) {
                    Text(
                        text = "Откликнуться",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.montserrat)),
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                }
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.hidevacancy),
                    contentDescription = "",
                    modifier = Modifier
                        .size(52.dp)
                        .background(Color.White, RoundedCornerShape(8.dp))
                        .clickable { onDeleteClick() }
                )
            }
        }
    }
}