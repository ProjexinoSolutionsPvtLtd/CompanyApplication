package com.spn.companyapplication.screens

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.components.*
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.AddTaskViewModel
import java.util.*

class AddTask : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        requestCameraPermission(this@AddTask)
        val viewModel by viewModels<AddTaskViewModel>()
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.validation()
            if(viewModel.dateTimeValue == "") {
                viewModel.setDefaultDate()
            }

            val keyboardController = LocalSoftwareKeyboardController.current
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Drawer(
                        title = "Add Lead",
                        context = this@AddTask,
                        activity = this@AddTask,
                        content = {
                            LazyColumn(
                                Modifier
                                    .padding(16.dp)
                                    .clickable(
                                        interactionSource = MutableInteractionSource(),
                                        indication = null,
                                        onClick = {
                                            keyboardController?.hide()
                                        }
                                    )
                            ) {
                                item()
                                {
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
                                    CountryCodeDropdown(viewModel)
                                    Spacer(Modifier.height(10.dp))
                                    TextInput(
                                        label = "Mail ID",
                                        value = viewModel.email,
                                        onChange = { viewModel.emailChange(it) },
                                        keyboardType = KeyboardType.Email
                                    )
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
                                        context = this@AddTask,
                                        value = viewModel.dateTimeValue,
                                        label = "Date/Time",
                                        selectedDate = viewModel.dateTime,
                                        onSelect = { viewModel.onDateTimeSelect(it, this@AddTask) }
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    AddDocumentInput(
                                        viewModel,
                                        LocalContext.current.contentResolver
                                    )
                                    Spacer(Modifier.height(20.dp))
                                    AddImageInput(this@AddTask, viewModel)
                                    Spacer(Modifier.height(30.dp))
                                    Button(
                                        text = "Submit",
                                        enabled = viewModel.validate,
                                        onClick = {
                                            viewModel.uploadLead(this@AddTask, this@AddTask)
//                                        ContextCompat.startActivity(
//                                            this@AddTask,
//                                            Intent(this@AddTask, Home::class.java),
//                                            null
//                                        )
                                        },
                                        color = Color(("#130b5c").toColorInt()),
                                        uppercase = false,
                                        showLoader = viewModel.showButtonLoader
                                    )
                                }
                            }
                        })
                }
            }
        }
    }
}

private fun hasCameraPermission(activity: Activity): Boolean {
    return ContextCompat.checkSelfPermission(
        activity,
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}

//}
//
private fun requestCameraPermission(activity: Activity) {
    var permissionsToRequest = mutableListOf<String>()

    if (!hasCameraPermission(activity)) {
        permissionsToRequest.add(android.Manifest.permission.CAMERA)
    }

    if (permissionsToRequest.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            activity,
            permissionsToRequest.toTypedArray(),
            0
        )
    }
}