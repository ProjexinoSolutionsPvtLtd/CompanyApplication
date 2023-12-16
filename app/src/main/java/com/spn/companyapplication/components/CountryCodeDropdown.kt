package com.spn.companyapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spn.companyapplication.R

@Composable
fun CountryCodeDropdown(
    getCountryCodeNumber: () -> String,
    number: String,
    numberChange:(String) -> Unit,
    formattedCountryCodes: List<String>,
    onCountryCodeSelected: (String)->Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box{
        OutlinedTextField(
            value = "${getCountryCodeNumber()} $number",
            onValueChange = { numberChange(it) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                fontSize = 17.sp,
                color = Color.Black
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            label = {
                Text(
                    text = "Mobile Number", fontSize = 17.sp, style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )
            },
            singleLine = true,
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )


        DropdownMenu(
            expanded = expanded,
            modifier = Modifier.height(200.dp),
            onDismissRequest = { expanded = false }
        ) {
            formattedCountryCodes.forEach { code ->
                DropdownMenuItem(
                    onClick = {
                        onCountryCodeSelected(code)
                        expanded = false
                    }
                ) {
                    Text(
                        text = code, fontSize = 17.sp, style = TextStyle(
                            fontFamily = FontFamily(Font(R.font.outfit_regular)),
                            fontSize = 17.sp,
                            color = Color.Black
                        )
                    )
                }
            }
        }
    }
}