package com.spn.companyapplication.components

import android.app.Activity
import android.content.ContentResolver
import android.graphics.drawable.Icon
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun BoxScope.Filter(
    viewModel: ViewLeadViewModel,
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
                viewModel.exportLeadsToExcel(
                    viewModel.leadsList,
                    contentResolver,
                    activity
                )
            } //Download Leads

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
            } //Filter based on Lead Status


//            Icon(
//                painter = painterResource(id = R.drawable.ic_baseline_sort_24),
//                contentDescription = "Sort by Date",
//                tint = Color.Gray,
//                modifier = Modifier
//                    .size(30.dp)
//                    .clickable {
//                        viewModel.showDateSortOptions = true
//                    }
//            )
//            Spacer(Modifier.width(10.dp))
//            Icon(
//                painter = painterResource(id = R.drawable.ic_filter),
//                contentDescription = "Filter based on Lead Status",
//                tint = Color.Gray,
//                modifier = Modifier
//                    .size(30.dp)
//                    .clickable {
//                        viewModel.showOptions = true
//                    },
//            )
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
                        Pair("Opened") { viewModel.fetchLeads(activity) },
                        Pair("Contacted") { viewModel.fetchLeads(activity) },
                        Pair("Hold") { viewModel.fetchLeads(activity) },
                        Pair("Lost/Closed") { viewModel.fetchLeads(activity) },
                        Pair("Converted") { viewModel.fetchLeads(activity) },
                        Pair("") { viewModel.fetchLeads(activity) },
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
                        Pair("Ascending") { viewModel.sortLeadsByDate() },
                        Pair("Descending") { viewModel.sortLeadsByDate() },
                        Pair("") { viewModel.fetchLeads(activity) },
                    ),
                    getSelectedOption = { viewModel.getDateSortTypeSelectedOption() }
                )
            }
        }
    }
}


@Composable
fun IconWithText(icon: Int, text: String, onClick: () -> Unit) {
    Row(Modifier
        .padding(horizontal = 10.dp)
        .clickable(
            interactionSource = MutableInteractionSource(),
            indication = null,
            onClick = {
                onClick.invoke()
            }
        )) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = Color.Gray,
            modifier = Modifier
                .size(23.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = text, style = TextStyle(
                fontFamily = FontFamily(Font(R.font.outfit_semibold)),
                fontSize = 15.sp,
                color = Color.Gray
            ),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}