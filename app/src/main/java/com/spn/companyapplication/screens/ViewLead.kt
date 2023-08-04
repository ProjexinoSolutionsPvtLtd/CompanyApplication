package com.spn.companyapplication.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.spn.companyapplication.components.*
import com.spn.companyapplication.models.Lead
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

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
        val viewModel by viewModels<ViewLeadViewModel>()

        viewModel.fetchLeads(this@ViewLead)

        setContent {
            val contentResolver = LocalContext.current.contentResolver
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    Drawer(
                        title = "View Leads",
                        context = this@ViewLead,
                        activity = this@ViewLead,
                        content = {
                            if (viewModel.showContent) {
                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    SearchBar(hint = "Search Leads")

                                    Sort(viewModel = viewModel, activity = this@ViewLead)

                                    Box(modifier = Modifier.fillMaxSize()){
                                        LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
                                            items(viewModel.leadsList) { lead ->
                                                LeadCard(lead, this@ViewLead, viewModel)
                                            }
                                            item{
                                                Spacer(Modifier.height(50.dp))
                                            }
                                        }
                                        com.spn.companyapplication.components.Button(
                                            modifier = Modifier.align(Alignment.BottomCenter),
                                            text = "Download Leads",
                                            onClick = {
                                                viewModel.exportLeadsToExcel(
                                                    viewModel.leadsList,
                                                    contentResolver
                                                )
                                            },
                                            color = Color(("#130b5c").toColorInt()),
                                            uppercase = false,
                                            padding = 4.dp
                                        )
                                    }
                                }
                            } else {
                                Loading()
                            }
                        })
                }
            }
        }
    }
}