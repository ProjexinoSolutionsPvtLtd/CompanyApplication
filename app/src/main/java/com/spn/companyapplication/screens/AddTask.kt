package com.spn.companyapplication.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.components.*
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.AddTaskViewModel
import java.util.*

class AddTask : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel by viewModels<AddTaskViewModel>()
        super.onCreate(savedInstanceState)
        setContent {
            viewModel.validation()
            if (viewModel.dateTimeValue == "") {
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
                        title = "Add Task",
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
                                        text = "Fill the form to add a new Task", style = TextStyle(
                                            fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                            fontSize = 28.sp,
                                            color = Color.Black
                                        )
                                    )

                                    TextInput(
                                        label = "Task Name",
                                        value = viewModel.name,
                                        onChange = { viewModel.nameChange(it) })
                                    Spacer(Modifier.height(10.dp))
                                    TextInput(
                                        label = "Project Name",
                                        value = viewModel.projectName,
                                        onChange = { viewModel.projectNameChange(it) })
                                    Spacer(Modifier.height(10.dp))
                                    TextInput(
                                        label = "Modules Included",
                                        value = viewModel.modulesIncluded,
                                        onChange = { viewModel.modulesIncludedChange(it) })
                                    Spacer(Modifier.height(10.dp))
                                    DateTimePicker(
                                        context = this@AddTask,
                                        value = viewModel.dateTimeValue,
                                        label = "Deadline to complete",
                                        selectedDate = viewModel.dateTime,
                                        onSelect = { viewModel.onDateTimeSelect(it, this@AddTask) }
                                    )
                                    Spacer(Modifier.height(10.dp))
                                    TextInput(
                                        label = "Assign To",
                                        value = viewModel.assignTo,
                                        onChange = { viewModel.assignToChange(it) })
                                    Spacer(Modifier.height(10.dp))
                                    TextInput(
                                        label = "Mail ID",
                                        value = viewModel.email,
                                        onChange = { viewModel.emailChange(it) },
                                        keyboardType = KeyboardType.Email
                                    )
                                    Spacer(Modifier.height(50.dp))
                                    Button(
                                        text = "Add Task",
                                        enabled = viewModel.validate,
                                        onClick = {
                                            viewModel.uploadTask(this@AddTask, this@AddTask)
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