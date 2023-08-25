package com.spn.companyapplication.models

import androidx.lifecycle.ViewModel

data class DropdownModel(
    val option: String,
    val onClick: () -> Unit,
    val viewModel: ViewModel
)