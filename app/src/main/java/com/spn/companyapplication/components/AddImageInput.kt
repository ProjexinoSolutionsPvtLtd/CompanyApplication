package com.spn.companyapplication.components

import ShowImage
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.AddLeadViewModel

@Composable
fun AddImageInput(context: Context, viewModel: AddLeadViewModel) {
    val focusManager = LocalFocusManager.current
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
        ) { bitmap ->
            viewModel.capturedBitmap = bitmap
        }

    if(viewModel.showImage){
        ShowImage(showImage = viewModel.showImage, onDismiss = { viewModel.showImage = false }, bitmap = viewModel.capturedBitmap!!)
    }

    Card(
        elevation = 0.dp,
        border = BorderStroke(
            1.dp, Color(("#9f9f9f").toColorInt())
        ),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 60.dp)
            .clickable {
                launcher.launch(null)
            }) {
        Column(
            Modifier
                .padding(
                    horizontal = 13.dp,
                    vertical = if (viewModel.capturedBitmap != null) 8.dp else 0.dp
                )
                .fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (viewModel.capturedBitmap != null) "Image" else "Upload Image",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(if (viewModel.capturedBitmap != null) R.font.outfit_medium else R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )

                if (viewModel.capturedBitmap == null) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (viewModel.capturedBitmap != null) {
                Spacer(modifier = Modifier.height(5.dp))
                Image(
                    bitmap = viewModel.capturedBitmap!!.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(200.dp)
                        .clickable {
                            viewModel.showImage = true
                        })
            }
        }
    }
}

private fun hasCameraPermission(activity: Activity): Boolean {
    return ContextCompat.checkSelfPermission(
        activity,
        android.Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED
}