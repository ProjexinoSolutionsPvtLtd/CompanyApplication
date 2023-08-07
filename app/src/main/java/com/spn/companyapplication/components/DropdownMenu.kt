package com.spn.companyapplication.components

import android.app.Activity
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.RegisterViewModel
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

@Composable
fun DropdownMenu(
    showOptions: Boolean,
    onDismiss: () -> Unit,
    options: List<String>,
    viewLeadViewModel: ViewLeadViewModel? = null,
    activity: Activity? = null,
    registerViewModel: RegisterViewModel? = null,
    isStatusUpdate: Boolean = false
) {
    DropdownMenu(
        expanded = showOptions,
        onDismissRequest = {
            onDismiss.invoke()
        },
    ) {
        options.forEach { option ->
            DropdownMenuItem(onClick = {
                if (viewLeadViewModel != null && !isStatusUpdate) {
                    if (option == viewLeadViewModel.selectedOption) {
                        viewLeadViewModel.selectedOption = ""
                    } else {
                        viewLeadViewModel.selectedOption = option
                    }
                    viewLeadViewModel.showOptions = false

                    viewLeadViewModel.fetchLeads(activity!!)
                } else if (viewLeadViewModel != null && isStatusUpdate) {
                    viewLeadViewModel.selectedStatusForUpdate = option
                    viewLeadViewModel.showStatusUpdateOptions = false
                } else if (registerViewModel != null) {
                    registerViewModel.role = option
                    registerViewModel.showRoleOptions = false
                }
            }) {
                Text(
                    text = option, style = TextStyle(
                        fontFamily = FontFamily(
                            Font(
                                if (viewLeadViewModel != null && !isStatusUpdate) {
                                    if (option != viewLeadViewModel.selectedOption) R.font.outfit_regular else R.font.outfit_medium
                                } else {
                                    R.font.outfit_regular
                                }
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