package com.spn.companyapplication.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.AddLeadViewModel

@Composable
fun CountryCodeDropdown(
    viewModel: AddLeadViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    val countryCodes = listOf(
        "+1 (US)",
        "+44 (UK)",
        "+91 (India)",
        // Add more countries and codes as needed
    )
    Box() {
        Text(
            text = viewModel.getCountryCodeNumber(),
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                fontSize = 17.sp,
                color = Color.Black
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
        )
        val xOffset = 16.dp

        // Create a VisualTransformation that applies the offset to the text
        val offsetTransformation = remember {
            VisualTransformation { text ->
                // Create a new transformed Text with the offset
                val transformedText = buildAnnotatedString {
                    repeat(xOffset.value.toInt()) { append(' ') }
                    append(text)
                }
                // Return the transformed text
                TransformedText(text = transformedText, offsetMapping = OffsetMapping.Identity)
            }
        }
        OutlinedTextField(
            value = viewModel.number,
            onValueChange = {
                viewModel.numberChange(it)
            },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = offsetTransformation,
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                fontSize = 17.sp,
                color = Color.Black
            ),
//        label = { Text(text = "Country Code") },
            singleLine = true,
//        readOnly = true, // Prevent text editing
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            }
        )
    }

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        countryCodes.forEach { code ->
            DropdownMenuItem(
                onClick = {
                    viewModel.onCountryCodeSelected(code)
                    expanded = false
                }
            ) {
                Text(text = code)
            }
        }
    }
}