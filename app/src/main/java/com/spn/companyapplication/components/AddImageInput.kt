package com.spn.companyapplication.components
//
//import android.Manifest.permission.CAMERA
//import android.app.Activity
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.graphics.Bitmap
//import android.provider.MediaStore
//import androidx.activity.compose.ManagedActivityResultLauncher
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.text.KeyboardActions
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.onFocusEvent
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.Font
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.content.ContextCompat
//import androidx.core.graphics.createBitmap
//import androidx.core.graphics.toColorInt
//import com.spn.companyapplication.Manifest
//import com.spn.companyapplication.R
//
//@Composable
//fun AddImageInput(context: Context) {
//    val focusManager = LocalFocusManager.current
//    var imageCaptured by remember {
//        mutableStateOf(false)
//    }
//    val launcher =
//        rememberLauncherForActivityResult(
//            contract = ActivityResultContracts.TakePicturePreview(),
//        ) {
//
//        }
//    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
//    val captureImage = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
//        if (bitmap != null) {
//            capturedBitmap = bitmap
//        }
//    }
//
//    if (imageCaptured) {
//
//    }
//
//    Card(border = BorderStroke(1.dp, Color(("#130b5c").toColorInt()))) {
//        OutlinedTextField(
//            value = "",
//            onValueChange = { },
//            singleLine = true,
//            label = {
//                Text(
//                    text = if (!imageCaptured) "Add Image" else "Image",
//                    fontSize = 17.sp,
//                    style = TextStyle(
//                        fontFamily = FontFamily(Font(if (!imageCaptured) R.font.outfit_regular else R.font.outfit_medium)),
//                        fontSize = 17.sp,
//                        color = Color.Black
//                    )
//                )
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 10.dp, top = 0.dp, start = 0.dp, end = 0.dp)
//                .height(60.dp)
//                .onFocusEvent { focusState -> if (focusState.hasFocus) takePicture(bitmap, launcher) },
//            textStyle = TextStyle(
//                fontFamily = FontFamily(Font(R.font.outfit_regular)),
//                fontSize = 17.sp,
//                color = Color.Black
//            ),
//            keyboardActions = KeyboardActions(
//                onDone = { focusManager.clearFocus() }
//            ),
//            trailingIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_camera),
//                    contentDescription = "camera",
//                    modifier = Modifier.size(25.dp),
//                    tint = Color(0xFFBDBDBD)
//                )
//            },
//        )
//    }
//}
//
//private fun takePicture(bitmap: Bitmap, launcher: ManagedActivityResultLauncher<Void, Bitmap>) {
//    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//    val photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//    launcher.launch(bitmap)
//}
//
//private fun hasCameraPermission(activity: Activity): Boolean {
//    return ContextCompat.checkSelfPermission(
//        activity,
//        Manifest.permission.CAMERA
//    ) == PackageManager.PERMISSION_GRANTED
//}