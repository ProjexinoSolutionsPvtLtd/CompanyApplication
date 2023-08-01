package com.spn.companyapplication.components

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.spn.companyapplication.R
import com.spn.companyapplication.models.MenuItem
import com.spn.companyapplication.screens.AddLead
import com.spn.companyapplication.screens.Home
import com.spn.companyapplication.screens.Login
import com.spn.companyapplication.screens.ViewLead
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Drawer(title: String, context: Context, activity: Activity, content: @Composable () -> Unit) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            AppBar(
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                title
            )
        },
        drawerGesturesEnabled = scaffoldState.drawerState.isOpen,
        drawerContent = {
            DrawerHeader()
            DrawerBody(
                items = listOf(
                    MenuItem(
                        id = "home",
                        title = "Home",
                        contentDescription = "",
                        icon = R.drawable.ic_baseline_home_24
                    ),
                    MenuItem(
                        id = "add_lead",
                        title = "Add Lead",
                        contentDescription = "",
                        icon = R.drawable.ic_baseline_person_add_alt_1_24
                    ),
                    MenuItem(
                        id = "view_leads",
                        title = "View Leads",
                        contentDescription = "",
                        icon = R.drawable.ic_baseline_people_24
                    ),
                    MenuItem(
                        id = "logout",
                        title = "Logout",
                        contentDescription = "",
                        icon = R.drawable.ic_logout
                    ),
                ),
                onItemClick = {
                    when (it.id) {
                        "home" -> startActivity(
                            context,
                            Intent(
                                activity,
                                Home::class.java
                            ),
                            null
                        )
                        "add_lead" -> startActivity(
                            context,
                            Intent(
                                activity,
                                AddLead::class.java
                            ),
                            null
                        )
                        "view_leads" -> startActivity(
                            context,
                            Intent(
                                activity,
                                ViewLead::class.java
                            ),
                            null
                        )
                        "logout" -> {
                            val firebaseAuth = FirebaseAuth.getInstance()
                            firebaseAuth.signOut()

                            activity.finish()
                            startActivity(context, Intent(activity, Login::class.java),null)
                        }
                    }
                }
            )
        }
    ) {
        content.invoke()
    }
}