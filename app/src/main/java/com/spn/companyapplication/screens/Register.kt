package com.spn.companyapplication.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.components.Button
import com.spn.companyapplication.components.Dropdown
import com.spn.companyapplication.components.TextInput
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.RegisterViewModel

//import com.google.firebase.firestore.FirebaseFirestore


class Register : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<RegisterViewModel>()
        setContent {
            viewModel.validation()
            val keyboardController = LocalSoftwareKeyboardController.current
            CompanyApplicationTheme {
                val rotationState by animateFloatAsState(targetValue = if (viewModel.showRoleOptions) 180f else 0f)
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = Color(("#ffffff").toColorInt())
                ) {
                    Column(Modifier
                        .verticalScroll(ScrollState(0))
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                keyboardController?.hide()
                            }
                        )) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_with_bg),
                            contentDescription = "logo",
                            modifier = Modifier.width(180.dp)
                        )

                        Text(
                            text = "Lets Register Your Account", style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                fontSize = 27.sp,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = "Welcome! Have a fantastic journey with out app!",
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                                fontSize = 20.sp,
                                color = Color.Black
                            )
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_register_illustration),
                            contentDescription = "",
                            modifier = Modifier
                                .size(330.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        TextInput(
                            label = "Name",
                            value = viewModel.name,
                            onChange = { viewModel.nameChange(it) })
                        Spacer(Modifier.height(15.dp))
                        Dropdown(
                            onDropdownMenuChange = { viewModel.showRoleOptions = !viewModel.showRoleOptions },
                            onDismiss = { viewModel.showRoleOptions = false },
                            showOptions = viewModel.showRoleOptions,
                            onSelected = { viewModel.onRoleDropdownOptionSelect(it) },
                            hint = viewModel.role,
                            rotationState = rotationState,
                            options = viewModel.roleOptions
                        )
                        Spacer(Modifier.height(15.dp))
                        TextInput(
                            label = "Email",
                            value = viewModel.email,
                            onChange = { viewModel.emailChange(it) },
                            keyboardType = KeyboardType.Email
                        )
                        Spacer(Modifier.height(15.dp))
                        TextInput(
                            label = "Username",
                            value = viewModel.username,
                            onChange = { viewModel.usernameChange(it) })
                        Spacer(Modifier.height(15.dp))
                        TextInput(
                            label = "Password",
                            value = viewModel.password,
                            onChange = { viewModel.passwordChange(it) },
                            isPassword = true
                        )


                        Spacer(Modifier.height(50.dp))

                        Button(
                            text = "Sign In",
                            enabled = viewModel.validate,
                            onClick = {
                                viewModel.registerUser(this@Register)
                            },
                            color = Color(("#130b5c").toColorInt()),
                            uppercase = false,
                            showLoader = viewModel.showLoader
                        )

                        val annotatedString = buildAnnotatedString {
                            append("Already have an account?")
                            pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                            append(" Log In")
                        }

                        Spacer(Modifier.height(30.dp))

                        Text(
                            text = annotatedString, textAlign = TextAlign.Center, style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    indication = null,
                                    interactionSource = MutableInteractionSource(),
                                    onClick = {
                                        startActivity(Intent(this@Register, Login::class.java))
                                    })
                        )
                    }
                }
            }
        }
    }
}