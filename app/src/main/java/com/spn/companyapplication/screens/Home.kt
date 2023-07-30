package com.spn.companyapplication.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.spn.companyapplication.components.AppBar
import com.spn.companyapplication.components.DrawerBody
import com.spn.companyapplication.components.DrawerHeader
import com.spn.companyapplication.models.MenuItem
import com.spn.companyapplication.ui.theme.CompanyApplicationTheme
import kotlinx.coroutines.launch
import com.spn.companyapplication.R
import com.spn.companyapplication.components.Drawer

class Home : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompanyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Drawer(title = "Home", context = this@Home, activity = this@Home, content = { Text(text = "TEST") })
                }
            }
        }
    }
}