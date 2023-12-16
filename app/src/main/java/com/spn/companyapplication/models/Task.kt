package com.spn.companyapplication.models

import androidx.compose.ui.graphics.vector.ImageVector

data class Task(
    val id: String = "",
    val name: String = "",
    val projectName: String = "",
    val modulesIncluded: String = "",
    val assignTo: List<String> = listOf(""),
    val email: String = "",
    val deadline: String = "",
    val status: String = "",         //Opened, In-Progress, Completed etc.
)