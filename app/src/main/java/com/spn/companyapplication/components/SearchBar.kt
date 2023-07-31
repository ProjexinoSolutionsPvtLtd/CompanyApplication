package com.spn.companyapplication.components

import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spn.companyapplication.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var focusState by remember { mutableStateOf(false) }
    var text by remember {
        mutableStateOf("")
    }

    Box(modifier = modifier) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
            },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                fontSize = 17.sp,
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
//                .shadow(5.dp, CircleShape)
//                .background(Color.White, CircleShape)
                .padding(horizontal = 0.dp, vertical = 12.dp)
                .onFocusChanged {
                    focusState = it.isFocused
                },
        ) {
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = text,
                innerTextField = it,
                singleLine = true,
                enabled = true,
                visualTransformation = VisualTransformation.None,
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_custom_search),
                        "",
                        modifier = Modifier
                            .padding(start = 10.dp)
                    )
                },
                placeholder = {
                    Text(
                        text = hint,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.outfit_regular)),
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    )
                },
                interactionSource = MutableInteractionSource(),
                // keep horizontal paddings but change the vertical
                contentPadding = TextFieldDefaults.outlinedTextFieldPadding(top = 0.dp),
            )
        }
    }
}