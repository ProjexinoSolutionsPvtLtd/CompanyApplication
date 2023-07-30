package com.spn.companyapplication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel(): ViewModel(){
    var username by mutableStateOf("")
    var password by mutableStateOf("")

    fun usernameChange(text: String){
        username = text
    }

    fun passwordChange(text: String){
        password = text
    }
}