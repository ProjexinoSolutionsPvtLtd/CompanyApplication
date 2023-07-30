package com.spn.companyapplication.components

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun DocumentPickerScreen(
    activity: ComponentActivity,
    onDocumentPicked: (Uri?) -> Unit
) {
    // Create the ActivityResultLauncher directly using GetContent()
    val documentPickerLauncher = rememberLauncherForDocumentPicker(activity, onDocumentPicked)

    // Use DisposableEffect to register and unregister the launcher
    DisposableEffect(documentPickerLauncher) {
        val callback = { result: Uri? -> onDocumentPicked(result) }
        val launcher =
            activity.registerForActivityResult(ActivityResultContracts.GetContent(), callback)
        onDispose { launcher.unregister() }
    }

    Column {
        // Your UI elements for document picker screen
        Button(
            "Select Document",
            onClick = {
                // Launch the document picker
                documentPickerLauncher.launch("*/*")
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun rememberLauncherForDocumentPicker(
    activity: ComponentActivity,
    onDocumentPicked: (Uri?) -> Unit
): ActivityResultLauncher<String?> {
    // Return the ActivityResultContracts.GetContent()
    return remember(activity) {
        activity.registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            onDocumentPicked(uri)
        }
    }
}