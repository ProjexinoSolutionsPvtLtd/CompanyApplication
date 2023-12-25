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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.spn.companyapplication.components.*
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.ViewLeadViewModel

class ViewLead : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
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

        viewModel.setupLeadsListener(this@ViewLead)

        setContent {
            val keyboardController = LocalSoftwareKeyboardController.current
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
                        onShareClicked = { viewModel.createTextFromLeadList(this@ViewLead) },
                        share = true,
                        content = {
                            if (viewModel.showContent) {
                                Box(Modifier.fillMaxSize()) {
                                    Column(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
                                            .clickable(
                                                interactionSource = MutableInteractionSource(),
                                                indication = null,
                                                onClick = {
                                                    keyboardController?.hide()
                                                }
                                            )

                                    ) {
                                        SearchBar(hint = "Search Leads", onSearch = {
                                            viewModel.onSearch(it)
                                        })

                                        Box(modifier = Modifier.fillMaxSize()) {
                                            LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
                                                items(viewModel.leadsList) { lead ->
                                                    LeadCard(
                                                        lead,
                                                        this@ViewLead,
                                                        viewModel,
                                                        this@ViewLead
                                                    )
                                                }
                                                item {
                                                    Spacer(Modifier.height(50.dp))
                                                }
                                            }
                                        }
                                    }
                                    LeadFilter(
                                        viewModel = viewModel,
                                        activity = this@ViewLead,
                                        contentResolver
                                    )
                                }
                            } else {
                                Loading("Fetching Leads")
                            }
                        })
                }
            }
        }
    }
}