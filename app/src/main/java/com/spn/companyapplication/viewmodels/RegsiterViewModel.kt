package com.spn.companyapplication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class RegsiterViewModel(): ViewModel(){
    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var name by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")

    fun usernameChange(text: String){
        username = text
    }

    fun passwordChange(text: String){
        password = text
    }

    fun numberChange(text: String){
        number = text
    }
    fun emailChange(text: String){
        email = text
    }
    fun nameChange(text: String){
        name = text
    }
}