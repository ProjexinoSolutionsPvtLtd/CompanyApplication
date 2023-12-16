package com.spn.companyapplication.components

import android.app.Activity
import android.content.ContentResolver
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.ViewTaskViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BoxScope.TaskFilter(
    viewModel: ViewTaskViewModel,
    activity: Activity,
    contentResolver: ContentResolver
) {
    Card(
        backgroundColor = Color.White, modifier = Modifier
            .fillMaxWidth()
            .align(
                Alignment.BottomCenter
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 11.dp, vertical = 9.dp)
        ) {
            IconWithText(icon = R.drawable.ic_download, text = "Download") {
                viewModel.showDownloadOptions = true
            } //Download Tasks

            Spacer(
                modifier = Modifier
                    .height(25.dp)
                    .width(1.dp)
                    .background(Color.LightGray)
            )

            IconWithText(
                icon = R.drawable.ic_baseline_sort_24,
                text = "Sort"
            ) { viewModel.showDateSortOptions = true } //Sort by Date

            Spacer(
                modifier = Modifier
                    .height(25.dp)
                    .width(1.dp)
                    .background(Color.LightGray)
            )

            IconWithText(icon = R.drawable.ic_filter, text = "Filter") {
                viewModel.showOptions = true
            } //Filter based on Task Status
        }

        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        if (viewModel.showOptions) {
            Box(modifier = Modifier.offset(x = (screenWidthDp - 125.dp))) {
                DropdownMenu(
                    showOptions = viewModel.showOptions,
                    onDismiss = { viewModel.showOptions = false },
                    options = viewModel.filterOptions,
                    onSelected = { viewModel.onStatusFilterDropdownOptionSelect(it) },
                    onClickMap = mapOf(
                        Pair("Opened") { viewModel.fetchTasks(activity) },
                        Pair("In-Progress") { viewModel.fetchTasks(activity) },
                        Pair("Testing") { viewModel.fetchTasks(activity) },
                        Pair("Completed") { viewModel.fetchTasks(activity) },
                        Pair("") { viewModel.fetchTasks(activity) },
                    ),
                    getSelectedOption = { viewModel.getStatusFilterSelectedOption() }
                )
            }
        }

        if (viewModel.showDateSortOptions) {
            Box(modifier = Modifier.offset(x = screenWidthDp.times(0.38f))) {
                DropdownMenu(
                    showOptions = viewModel.showDateSortOptions,
                    onDismiss = { viewModel.showDateSortOptions = false },
                    options = viewModel.dateSortOptions,
                    onSelected = { viewModel.onDateSortDropdownOptionSelect(it) },
                    onClickMap = mapOf(
                        Pair("Ascending") { viewModel.sortTasksByDate() },
                        Pair("Descending") { viewModel.sortTasksByDate() },
                        Pair("") { viewModel.fetchTasks(activity) },
                    ),
                    getSelectedOption = { viewModel.getDateSortTypeSelectedOption() }
                )
            }
        }

        if (viewModel.showDownloadOptions) {
            Box(modifier = Modifier.offset(x = screenWidthDp.times(0.05f))) {
                DropdownMenu(
                    showOptions = viewModel.showDownloadOptions,
                    onDismiss = { viewModel.showDownloadOptions = false },
                    options = viewModel.downloadOptions,
                    onSelected = { viewModel.onDownloadOptionSelect(it) },
                    onClickMap = mapOf(
                        Pair("Excel") {
                            viewModel.exportTasksToExcel(
                                viewModel.tasksList,
                                contentResolver,
                                activity
                            )
                        },
                        Pair("PDF") { viewModel.generatePDF(viewModel.tasksList, activity) },
                    ),
                    getSelectedOption = { viewModel.getDateSortTypeSelectedOption() }
                )
            }
        }
    }
}
