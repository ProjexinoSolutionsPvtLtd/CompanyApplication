package com.spn.companyapplication.components

import androidx.compose.foundation.layout.offset
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R

@Composable
fun AppBar(
    onNavigationIconClick: () -> Unit,
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.outfit_regular)),
                    fontSize = 22.sp,
                    color = Color.White
                ),
                modifier = Modifier.offset(x = (-10).dp)
            )
        },
        backgroundColor = Color(("#130b5c").toColorInt()),
        contentColor = Color(("#130b5c").toColorInt()),
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Toggle drawer",
                    tint = Color.LightGray
                )
            }
        }
    )
}