package com.spn.companyapplication.components

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.spn.companyapplication.R
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
                showOptions = viewModel.showOptions,
                onDismiss = { viewModel.showOptions = false },
                options = viewModel.filterOptions,
                viewLeadViewModel = viewModel,
                activity = activity
            )
        }
    }
}