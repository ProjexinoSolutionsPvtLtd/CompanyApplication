package com.spn.companyapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
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
fun Loading(){
    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(30.dp),
            color = Color(("#130b5c").toColorInt())
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            "Fetching Leads", style = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                fontSize = 14.sp,
                color = Color.Gray
            )
        )
    }
}