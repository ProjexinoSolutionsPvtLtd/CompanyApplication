package com.spn.companyapplication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegsiterViewModel(): ViewModel(){
    var showRoleOptions by mutableStateOf(false)
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var role by mutableStateOf("Select Role")
    var email by mutableStateOf("")

    val roleOptions = listOf("Admin", "Business Development Manager")

    fun usernameChange(text: String){
        username = text
    }

    fun passwordChange(text: String){
        password = text
    }

    fun roleChange(text: String){
        role = text
    }

    fun emailChange(text: String){
        email = text
    }

    fun nameChange(text: String){
        name = text
    }
}