package com.spn.companyapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R

@Composable
fun DocumentCard(documentName: String, documentType: String){
    Card(
        modifier = Modifier.heightIn(min = 50.dp),
        shape = RoundedCornerShape(3.dp),
        backgroundColor = Color(("#c3c3c3").toColorInt()),
        elevation = 0.dp
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                documentName, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.outfit_regular)),
                    fontSize = 17.sp,
                    color = Color.Black
                ),
                modifier = Modifier.widthIn(max = 250.dp)
            )
            Text(
                documentType, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                    fontSize = 12.sp,
                    color = Color.Black
                )
            )
        }
    }
}