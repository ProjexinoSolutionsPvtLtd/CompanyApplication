package com.spn.companyapplication.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R

@Composable
fun Button(
    text: String,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.primary,
    showLoader: Boolean = false,
    icon: Int = -1,
    textColor: Color = Color.White,
    tint: Color = Color.White,
    iconSize: Dp ?= null,
    padding: Dp ?= null,
    uppercase: Boolean = true,
    fontSize: TextUnit ?= null,
    border: Dp ?= null,
    trailingIcon: Int ?= null,
    enabled: Boolean = true,
    trailingIconRotationState: Float ?= null,
    borderColor: Color = Color(("#0074DE").toColorInt()),
    modifier: Modifier? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier?: Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
        enabled = enabled,
        border = border?.let { BorderStroke(it, color = borderColor,) }
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(padding ?: 6.dp)
        ) {
            if (showLoader)
                CircularProgressIndicator(
                    modifier = Modifier.size(25.dp),
                    color = Color.White
                )

            if (icon != -1) {
                Icon(
                    painter = painterResource(id = icon),
                    modifier = Modifier
                        .size(iconSize ?: 20.dp)
                        .offset(x = (-8).dp),
                    tint = tint,
                    contentDescription = null
                )
            }
            if(!showLoader){
                Text(
                    text = if (uppercase) text.uppercase() else text.replaceFirstChar { it.uppercase() },
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 16.sp,
                        color = Color.White
                    ),
                    color = textColor,
                    fontSize = fontSize ?: 17.sp,
                    textAlign = TextAlign.Center
                )
            }
            if(trailingIcon != null){
                Spacer(Modifier.width(10.dp))
                Icon(
                    painter = painterResource(id = trailingIcon),
                    modifier = Modifier
                        .size(iconSize ?: 20.dp)
                        .offset(x = (-8).dp)
                        .rotate(trailingIconRotationState!!),
                    tint = Color.Unspecified,
                    contentDescription = null
                )
            }
        }
    }
}