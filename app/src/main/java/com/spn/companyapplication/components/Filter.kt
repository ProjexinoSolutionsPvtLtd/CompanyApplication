package com.spn.companyapplication.components

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.HomeViewModel
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

@Composable
fun ColumnScope.Filter(viewModel: ViewLeadViewModel, activity: Activity) {
    Box(Modifier.align(Alignment.End)) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_sort_24),
            contentDescription = "",
            tint = Color.Gray,
            modifier = Modifier
                .clickable {
                    viewModel.showOptions = true
                }
        )

        if (viewModel.showOptions) {
            DropdownMenu(
                expanded = viewModel.showOptions,
                onDismissRequest = {
                    viewModel.showOptions = false
                },
            ) {
                viewModel.filterOptions.forEach { option ->
                    DropdownMenuItem(onClick = {
                        if(option == viewModel.selectedOption){
                            viewModel.selectedOption = ""
                        }
                        else {
                            viewModel.selectedOption = option
                        }
                        viewModel.showOptions = false

                        viewModel.fetchLeads(activity)
                    }) {
                        Text(
                            text = option, style = TextStyle(
                                fontFamily = FontFamily(Font(if (option != viewModel.selectedOption) R.font.outfit_regular else R.font.outfit_medium)),
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                        )
                    }
                }
            }
        }
    }
}