package com.spn.companyapplication.screens

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
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
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.components.Button
import com.spn.companyapplication.components.TextInput
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import com.spn.companyapplication.viewmodels.LoginViewModel
import com.spn.companyapplication.viewmodels.RegsiterViewModel


class Register : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<RegsiterViewModel>()
        setContent {
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    color = Color(("#ffffff").toColorInt())
                ) {
                    Column(Modifier.verticalScroll(ScrollState(0))) {
                        Image(
                            painter = painterResource(id = R.drawable.logo_with_bg),
                            contentDescription = "logo",
                            modifier = Modifier.width(180.dp)
                        )

                        Text(
                            text = "Lets Register Your Account", style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_bold)),
                                fontSize = 37.sp,
                                color = Color.Black
                            )
                        )
                        Text(
                            text = "Welcome! Have a fantastic journey with out app!", style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_medium)),
                                fontSize = 28.sp,
                                color = Color.Black
                            )
                        )

                        Spacer(Modifier.height(50.dp))

                        TextInput(
                            label = "Name",
                            value = viewModel.name,
                            onChange = { viewModel.nameChange(it) })
                        TextInput(
                            label = "Mobile Number",
                            value = viewModel.number,
                            onChange = { viewModel.numberChange(it) })
                        TextInput(
                            label = "Email",
                            value = viewModel.email,
                            onChange = { viewModel.emailChange(it) })
                        TextInput(
                            label = "Username",
                            value = viewModel.username,
                            onChange = { viewModel.usernameChange(it) })
                        TextInput(
                            label = "Password",
                            value = viewModel.password,
                            onChange = { viewModel.passwordChange(it) })


                        Spacer(Modifier.height(50.dp))

                        Button(
                            text = "Sign In",
                            onClick = {
                                startActivity(
                                    Intent(
                                        this@Register,
                                        Home::class.java
                                    )
                                )
                            },
                            color = Color(("#130b5c").toColorInt()),
                            uppercase = false
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
                            modifier = Modifier.fillMaxWidth()
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