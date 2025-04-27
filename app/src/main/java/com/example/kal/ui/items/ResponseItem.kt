package com.example.kal.ui.items

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun ResponseItem(
    vacancy: Vacancy,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFC1C1C5), RoundedCornerShape(16.dp))
            .clickable { onItemClick() }
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = vacancy.job,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF151518)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = vacancy.address,
                    fontFamily = FontFamily(Font(R.font.montserrat)),
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    color = Color(0xFF9A9BA1)
                )
            }

            // Иконка удаления
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.trash),
                contentDescription = "Удалить отклик",
                modifier = Modifier
                    .clickable(null, null){
                        onDeleteClick()
                    }
                    .padding(start = 16.dp)
            )
        }
    }
}
