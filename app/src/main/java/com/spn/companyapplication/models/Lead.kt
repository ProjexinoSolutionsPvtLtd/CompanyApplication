package com.spn.companyapplication.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Lead(
    val id: String = "",
    val name: String = "",
    val role: String = "",
    val number: String = "",
    val organization: String = "",
    val email: String = "",
    val requirement: String = "",
    val address: String = "",
    val dateTimeValue: String = "",
    val status: String = "",         //Opened, Closed, Contacted etc.
    val createdBy: String = "",
    val documentUrl: String = "",
    val documentName: String = "",
    val documentType: String = "",
    val imageUrl: String = ""
)