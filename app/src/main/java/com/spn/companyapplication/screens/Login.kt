package com.spn.companyapplication.screens

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.toColorInt
import com.google.firebase.auth.FirebaseAuth
import com.spn.companyapplication.R
import com.spn.companyapplication.components.Button
import com.spn.companyapplication.components.TextInput
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.LoginViewModel

class Login : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val firebaseAuth = FirebaseAuth.getInstance()

        if (firebaseAuth.currentUser != null) {
            startActivity(this@Login, Intent(this@Login, Home::class.java), null)
            finish()
        }
        val viewModel by viewModels<LoginViewModel>()

        super.onCreate(savedInstanceState)
        setContent {
            viewModel.validation()
            val keyboardController = LocalSoftwareKeyboardController.current
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = Color(("#ffffff").toColorInt())
                ) {
                    LazyColumn(Modifier
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null,
                            onClick = {
                                keyboardController?.hide()
                            }
                        )) {
                        item() {
                            Image(
                                painter = painterResource(id = R.drawable.logo_with_bg),
                                contentDescription = "logo",
                                modifier = Modifier.width(130.dp)
                            )

                            Text(
                                text = "Lets Sign you in", style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                    fontSize = 30.sp,
                                    color = Color.Black
                                )
                            )
                            Text(
                                text = "Welcome back,\nYou have been missed", style = TextStyle(
                                    fontFamily = FontFamily(Font(R.font.outfit_regular)),
                                    fontSize = 24.sp,
                                    color = Color.Black
                                )
                            )

                            Image(
                                painter = painterResource(id = R.drawable.ic_login_illustration),
                                contentDescription = "",
                                modifier = Modifier
                                    .size(330.dp)
                            )

                            TextInput(
                                label = "Email",
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
                                text = "Login",
                                enabled = viewModel.validate,
                                onClick = {
                                    viewModel.loginUser(this@Login)
                                },
                                color = Color(("#130b5c").toColorInt()),
                                uppercase = false,
                                showLoader = viewModel.showLoader
                            )

                            val annotatedString = buildAnnotatedString {
                                append("Don't have an account?")
                                pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
                                append(" Register Now")
                            }

                            Spacer(Modifier.height(20.dp))

                            Text(
                                text = annotatedString,
                                textAlign = TextAlign.Center,
                                style = TextStyle(
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
                                            startActivity(Intent(this@Login, Register::class.java))
                                        })
                            )
                        }
                    }
                }
            }
        }
    }
}