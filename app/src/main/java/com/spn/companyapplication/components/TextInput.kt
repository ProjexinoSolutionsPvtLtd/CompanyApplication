package com.spn.companyapplication.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R

@Composable
fun TextInput(
    label: String,
    icon: @Composable() (() -> Unit)? = null,
    trailingIcon: Int? = null,
    toggleField:( () -> Unit) ? =null,
    value: String,
    onChange: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Next,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true,
    fontSize: TextUnit? = null,
    height: Dp? = null,
) {
    val focusManager = LocalFocusManager.current
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(
        value = value,
        onValueChange = { newText -> onChange(newText) },
        singleLine = true,
        label = {
            Text(
                text = label, fontSize = fontSize ?: 17.sp, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.outfit_regular)),
                    fontSize = 17.sp,
                    color = Color.Black
                )
            )
        },
        leadingIcon = icon,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 0.dp)
//            .border(BorderStroke(1.dp, Color(("#130b5c").toColorInt())))
            .height(height ?: 65.dp),
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.outfit_regular)),
            fontSize = 17.sp,
            color = Color.Black
        ),
        maxLines = 4,
        keyboardOptions = KeyboardOptions(imeAction = imeAction).also {
            KeyboardOptions.Default.copy(
                keyboardType = keyboardType
            )
        },
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        enabled = enabled,
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = "password",
                        modifier = Modifier.size(25.dp),
                        tint = Color(0xFFBDBDBD)
                    )
                }
            }
            else if(trailingIcon != null){
                IconButton(onClick = {
                    if (toggleField != null) {
                        toggleField()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = trailingIcon),
                        contentDescription = "add_assign",
                        modifier = Modifier.size(25.dp),
                        tint = Color.Black
                    )
                }
            }
        },
        visualTransformation = if (isPassword && !passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
    )
}