package com.spn.companyapplication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddLeadViewModel(): ViewModel(){
    private val dateTimeFormat = SimpleDateFormat("HH:mm dd-MMM-yyyy")

    var dateTimeValue by mutableStateOf("")
    var dateTime: Calendar by mutableStateOf(Calendar.getInstance())

    var name by mutableStateOf("")
    var organization by mutableStateOf("")
    var role by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var address by mutableStateOf("")
    var requirement by mutableStateOf("")

    fun nameChange(text: String){
        name = text
    }

    fun organizationChange(text: String){
        organization = text
    }

    fun roleChange(text: String){
        role = text
    }

    fun emailChange(text: String){
        email = text
    }

    fun numberChange(text: String){
        number = text
    }

    fun addressChange(text: String){
        address = text
    }

    fun requirementChange(text: String){
        requirement = text
    }

    fun onDateTimeSelect(time: Calendar) {
        dateTime = time
        dateTimeValue = dateTimeFormat.format(time.time)
    }
}