package com.spn.companyapplication.screens

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.spn.companyapplication.R
import com.spn.companyapplication.components.*
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.AddLeadViewModel
import com.spn.companyapplication.viewmodels.LoginViewModel
import java.util.*


class AddLead : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<AddLeadViewModel>()
        super.onCreate(savedInstanceState)
        setContent {
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Drawer(
                        title = "Add Lead",
                        context = this@AddLead,
                        activity = this@AddLead,
                        content = {
                            Column(Modifier.padding(16.dp).verticalScroll(ScrollState(0))) {
                                Text(
                                    text = "Fill the form to add a new Lead", style = TextStyle(
                                        fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                        fontSize = 28.sp,
                                        color = Color.Black
                                    )
                                )

                                TextInput(
                                    label = "Name",
                                    value = viewModel.name,
                                    onChange = { viewModel.nameChange(it) })
                                Spacer(Modifier.height(10.dp))
                                TextInput(
                                    label = "Organization",
                                    value = viewModel.organization,
                                    onChange = { viewModel.organizationChange(it) })
                                Spacer(Modifier.height(10.dp))
                                TextInput(
                                    label = "Role",
                                    value = viewModel.role,
                                    onChange = { viewModel.roleChange(it) })
                                Spacer(Modifier.height(10.dp))
                                TextInput(
                                    label = "Number",
                                    value = viewModel.number,
                                    onChange = { viewModel.numberChange(it) })
                                Spacer(Modifier.height(10.dp))
                                TextInput(
                                    label = "Mail ID",
                                    value = viewModel.email,
                                    onChange = { viewModel.emailChange(it) })
                                Spacer(Modifier.height(10.dp))
                                TextInput(
                                    label = "Address",
                                    value = viewModel.address,
                                    onChange = { viewModel.addressChange(it) })
                                Spacer(Modifier.height(10.dp))
                                TextInput(
                                    label = "Requirement",
                                    value = viewModel.requirement,
                                    onChange = { viewModel.requirementChange(it) })
                                Spacer(Modifier.height(10.dp))
                                DateTimePicker(
                                    context = this@AddLead,
                                    value = viewModel.dateTimeValue,
                                    label = "Date/Time",
                                    selectedDate = viewModel.dateTime,
                                    onSelect = { viewModel.onDateTimeSelect(it) }
                                )
//                                AddImageInput(context = this@AddLead)
                                val contentResolver = LocalContext.current.contentResolver
                                AddDocumentInput(this@AddLead, onDocumentPicked = { uri ->
                                    // Handle the selected document URI here
                                    // uri is the URI of the selected document
                                }, viewModel, contentResolver)
                            }
                        })
                }
            }
        }
    }
}

//private fun hasCameraPermission(activity: Activity): Boolean {
//    return ContextCompat.checkSelfPermission(
//        activity,
//        Manifest.permission.CAMERA
//    ) == PackageManager.PERMISSION_GRANTED
//}
//
//private fun requestCameraPermission(activity: Activity){
//    var permissionsToRequest = mutableListOf<String>()
//
//    if (!hasCameraPermission(activity)) {
//        permissionsToRequest.add(Manifest.permission.CAMERA)
//    }
//
//    if (permissionsToRequest.isNotEmpty()) {
//        ActivityCompat.requestPermissions(
//            activity,
//            permissionsToRequest.toTypedArray(),
//            0
//        )
//    }
//}