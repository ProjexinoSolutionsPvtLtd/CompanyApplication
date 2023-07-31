package com.spn.companyapplication.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import java.util.*

@Composable
fun DateTimePicker(
    context: Context,
    value: String,
    label: String,
    selectedDate: Calendar,
    onSelect: (Calendar) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val time = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        R.style.Theme_Picker,
        { _: DatePicker, y: Int, m: Int, d: Int ->
            time.set(Calendar.DAY_OF_MONTH, d)
            time.set(Calendar.MONTH, m)
            time.set(Calendar.YEAR, y)
            onSelect(time)
        },
        selectedDate.get(Calendar.YEAR),
        selectedDate.get(Calendar.MONTH),
        selectedDate.get(Calendar.DAY_OF_MONTH),
    )
    val timePickerDialog = TimePickerDialog(
        context,
        R.style.Theme_Picker,
        { _, hour: Int, minute: Int ->
            time.set(Calendar.HOUR_OF_DAY, hour)
            time.set(Calendar.MINUTE, minute)
            datePickerDialog.show()

            focusManager.clearFocus()
        },
        selectedDate.get(Calendar.HOUR),
        selectedDate.get(Calendar.MINUTE),
        false
    )


    OutlinedTextField(
        value = value,
        textStyle = TextStyle(
            fontFamily = FontFamily(Font(R.font.outfit_regular)),
            fontSize = 17.sp,
            color = Color.Black
        ),
        onValueChange = {},
        readOnly = true,
        label = {
            Text(
                text = label, style = TextStyle(
                    fontFamily = FontFamily(Font(R.font.outfit_regular)),
                    fontSize = 17.sp,
                    color = Color.Black
                )
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 7.dp)
            .onFocusEvent { focusState -> if (focusState.hasFocus) timePickerDialog.show() },
        trailingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_datetime),
                contentDescription = "icon",
                tint = Color.Black
            )
        }

    )
}