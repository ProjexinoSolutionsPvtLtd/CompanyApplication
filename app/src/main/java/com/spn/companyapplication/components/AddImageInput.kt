package com.spn.companyapplication.components

import ShowImage
import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
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
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.core.graphics.toColorInt
import com.spn.companyapplication.R
import com.spn.companyapplication.viewmodels.AddLeadViewModel

@Composable
fun AddImageInput(
    showImage: Boolean,
    setShowImage: (status: Boolean) -> Unit,
    capturedBitmap: Bitmap?,
    setCapturedBitmap: (bitmap: Bitmap) -> Unit,
    context: Context
) {
    val focusManager = LocalFocusManager.current
    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
        ) { bitmap ->
            setCapturedBitmap(bitmap!!)
        }


    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview()
        ) { bitmap ->
            setCapturedBitmap(bitmap!!)
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            setCapturedBitmap(bitmap)
        }

    if (showImage) {
        ShowImage(
            showImage = showImage,
            onDismiss = { setShowImage(false) },
            bitmap = capturedBitmap!!
        )
    }


    val openDialog = remember { mutableStateOf(false) }
    if(openDialog.value){
        AlertDialog(
            modifier = Modifier.padding(0.dp),
            onDismissRequest = { openDialog.value = false },
            title = { Text("Image Options", style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.outfit_bold)),
                        fontSize = 20.sp,
                        color = Color.Black
                    ))},
            text = {
                Column {
                    val items = arrayOf("Take Photo", "Choose from Gallery")
                    items.forEachIndexed { index, text ->
                        Text(
                            text = text,
                            style = TextStyle(
                                fontFamily = FontFamily(Font(R.font.outfit_regular)),
                                fontSize = 17.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    when (index) {
                                        0 -> cameraLauncher.launch(null)
                                        1 -> galleryLauncher.launch("image/*")
                                    }
                                    openDialog.value = false
                                }
                                .padding(vertical = 8.dp)
                        )
                        if (index < items.size - 1) {
                            Divider()
                        }
                    }
                }
            },
            buttons = {
//                TextButton(onClick = { openDialog.value = false }) {
//                    Text("Cancel", style = TextStyle(
//                        fontFamily = FontFamily(Font(R.font.outfit_regular)),
//                        fontSize = 17.sp,
//                        color = Color.Black
//                    ))
//                }
            }
        )
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
//                launcher.launch(null)
                openDialog.value = true
            }) {
        Column(
            Modifier
                .padding(
                    horizontal = 13.dp,
                    vertical = if (capturedBitmap != null) 8.dp else 0.dp
                )
                .fillMaxSize(), verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    if (capturedBitmap != null) "Image" else "Upload Image",
                    style = TextStyle(
                        fontFamily = FontFamily(Font(if (capturedBitmap != null) R.font.outfit_medium else R.font.outfit_regular)),
                        fontSize = 17.sp,
                        color = Color.Black
                    )
                )

                if (capturedBitmap == null) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = "",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (capturedBitmap != null) {
                Spacer(modifier = Modifier.height(5.dp))
                Image(
                    bitmap = capturedBitmap!!.asImageBitmap(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(200.dp)
                        .clickable {

                            setShowImage(true)
                        })
            }
        }
    }
}