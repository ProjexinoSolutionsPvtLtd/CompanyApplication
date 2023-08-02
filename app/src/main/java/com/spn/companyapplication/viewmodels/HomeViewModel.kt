package com.spn.companyapplication.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spn.companyapplication.R
import com.spn.companyapplication.models.HomeScreen

class HomeViewModel(): ViewModel(){
    val homeScreenItems = listOf(
        HomeScreen("WELCOME TO PROJEXINO SOLUTIONS", "Building Business Online", R.drawable.notebook_1),
        HomeScreen("WELCOME TO PROJEXINO SOLUTIONS", "Making Web work for you", R.drawable.coding),
        HomeScreen("YOUR CREATIVE WEB DEVELOPMENT PARTNER", "Where passion meets profession", R.drawable.notebook_2)
    )

    var currentHomeScreenItemIndex by mutableStateOf(0)

    val expertiseItems = listOf(
        HomeScreen("Web Development", "Unique, High impact and Creative budget friendly websites.", R.drawable.ic_web_development),
        HomeScreen("App Development", "We Develop Apps, enriching customer experience.", R.drawable.ic_app_development),
        HomeScreen("UI/UX Design", "Visual representation of ideas by combining words & Images.", R.drawable.ic_ui_ux),
        HomeScreen("Digital Marketing", "Content marketing, social media & digital presence.", R.drawable.ic_marketing)
    )

    var currentExpertiseItemIndex by mutableStateOf(0)
}