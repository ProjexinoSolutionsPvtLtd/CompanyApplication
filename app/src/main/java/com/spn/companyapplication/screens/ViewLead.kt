package com.spn.companyapplication.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.components.Drawer
import com.spn.companyapplication.components.LeadCard
import com.spn.companyapplication.components.SearchBar
import com.spn.companyapplication.models.Lead
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.ViewLeadViewModel


val leadList = listOf(
    Lead(
        "1",
        "Pranav Sanjay Rokade",
        "Decision Taker",
        "9561160673",
        "Projexino Solutions Private Limited",
        "pranav.rokade2011@gmail.com",
        "Enquiry",
        "Kothurd, Pune",
        "2:00 PM, 29/09/2023",
        "Closed",
        "Admin"
    ),
    Lead(
        "2",
        "Sanjay Ramdas Rokade",
        "CEO",
        "9561160502",
        "Deltafour",
        "sr.rokade@gmail.com",
        "Web Application",
        "Jeddah, Pune",
        "2:00 PM, 29/09/2023",
        "Contacted",
        "Business Development Manager"
    ),
    Lead(
        "3",
        "Aditya Katkar",
        "Owner",
        "9900214214",
        "MIT WPU",
        "adityakatkar@gmail.com",
        "Enquiry",
        "Kothurd, Pune",
        "2:00 PM, 29/09/2023",
        "Closed",
        "Admin"
    ),
    Lead(
        "4",
        "Ambuj Tripathi",
        "Owner",
        "9241160673",
        "Tripathi & Sons",
        "anushkatapal@gmail.com",
        "Enquiry",
        "Hadapsar, Pune",
        "10:00 PM, 31/10/2023",
        "Opened",
        "Business Development Manager"
    )
)

class ViewLead : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // Request permissions from the user
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1
            )
        }
        super.onCreate(savedInstanceState)

        setContent {
            val contentResolver = LocalContext.current.contentResolver
            val viewModel by viewModels<ViewLeadViewModel>()
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Drawer(
                        title = "View Leads",
                        context = this@ViewLead,
                        activity = this@ViewLead,
                        content = {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                SearchBar(hint = "Search Leads")
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
                                        // DropdownMenu to display sort options
                                        DropdownMenu(
                                            expanded = viewModel.showOptions,
                                            onDismissRequest = { viewModel.showOptions = false },
                                        ) {
                                            viewModel.sortOptions.forEach { option ->
                                                DropdownMenuItem(onClick = {
                                                    viewModel.selectedOption = option
                                                    viewModel.showOptions = false
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

                                LazyColumn() {
                                    items(leadList) { lead ->
                                        LeadCard(lead = lead)
                                    }
                                    item {
                                        com.spn.companyapplication.components.Button(
                                            text = "Download Leads",
                                            onClick = {
                                                viewModel.exportLeadsToExcel(
                                                    leadList,
                                                    contentResolver
                                                )
                                            },
                                            color = Color(("#130b5c").toColorInt()),
                                            uppercase = false,
                                        )
                                    }
                                }
                            }
                        })
                }
            }
        }
    }
}