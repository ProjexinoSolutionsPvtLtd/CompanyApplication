package com.spn.companyapplication.components

import android.app.Activity
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.RegisterViewModel
import com.spn.companyapplication.viewmodels.ViewLeadViewModel
import org.apache.poi.ss.formula.functions.Column

@Composable
fun BoxScope.DropdownMenu(
    showOptions: Boolean,
    onDismiss: () -> Unit,
    options: List<String>,
    onSelected: (String) -> Unit,
//    onClickMap: Map<String, () -> Unit>,
    activity: Activity? = null,
    isStatusUpdate: Boolean = false
) {
    val width = LocalContext
    DropdownMenu(
        expanded = showOptions,
        onDismissRequest = {
            onDismiss.invoke()
        },
    ) {
        options.forEach { option ->
            DropdownMenuItem(onClick = {
                onSelected.invoke(option)
                onDismiss.invoke()

//                if (viewLeadViewModel != null && isStatusUpdate) {
//                    viewLeadViewModel.selectedStatusForUpdate = option
//                    viewLeadViewModel.showStatusUpdateOptions = false
//                } else
//                if (option == viewLeadViewModel.selectedOption) {
//                    viewLeadViewModel.selectedOption = ""
//                }
//                    viewLeadViewModel.fetchLeads(activity!!)
            }) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = option,
                    style = TextStyle(
                        fontFamily = FontFamily(
                            Font(
//                                if (viewLeadViewModel != null && !isStatusUpdate) {
//                                    if (option != viewLeadViewModel.selectedOption) R.font.outfit_regular else R.font.outfit_medium
//                                } else {
                                    R.font.outfit_regular
//                                }
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                )
            }
        }
    }
}