package com.spn.companyapplication.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ViewLeadViewModel(): ViewModel(){
    val sortOptions = listOf("Opened", "Contacted", "Hold", "Lost/Closed", "Converted")
    var showOptions by mutableStateOf(false)
    var selectedOption by mutableStateOf("")
}