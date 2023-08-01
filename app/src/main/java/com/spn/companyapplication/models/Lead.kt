package com.spn.companyapplication.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Lead(
    val id: String,
    val name: String,
    val role: String,
    val number: String,
    val organization: String,
    val email: String,
    val requirement: String,
    val address: String,
    val dateTime: String,
    val status: String,         //Opened, Closed, Contacted etc.
    val createdBy: String,
)