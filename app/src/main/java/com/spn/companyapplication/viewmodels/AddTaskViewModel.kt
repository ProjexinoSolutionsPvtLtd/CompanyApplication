package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.spn.companyapplication.R
import com.spn.companyapplication.screens.Home
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskViewModel : ViewModel() {
    private val dateTimeFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy")

    var dateTimeValue by mutableStateOf("")
    var dateTime: Calendar by mutableStateOf(Calendar.getInstance())


    var name by mutableStateOf("")
    var projectName by mutableStateOf("")
    var modulesIncluded by mutableStateOf("")
    var assignTo by mutableStateOf("")
    var email by mutableStateOf("")

    var showButtonLoader by mutableStateOf(false)

    var validate by mutableStateOf(false)

    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val storageReference = FirebaseStorage.getInstance().reference

    fun nameChange(text: String) {
        name = text
    }

    fun projectNameChange(text: String) {
        projectName = text
    }

    fun modulesIncludedChange(text: String) {
        modulesIncluded = text
    }

    fun emailChange(text: String) {
        email = text
    }

    fun assignToChange(text: String) {
        assignTo = text
    }

    fun onDateTimeSelect(time: Calendar, context: Context) {
        dateTime = time
        val currentDate = Calendar.getInstance()
        if (!time.before(currentDate)) {
            dateTimeValue = dateTimeFormat.format(time.time)
        } else {
            val toast = "Date/Time cannot be set to a date earlier than the current one"
            val centeredText = SpannableString(toast)
            centeredText.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                toast.length - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            Toast.makeText(context, centeredText, Toast.LENGTH_SHORT).show()
        }
    }

    val getDocumentName: (ContentResolver, Uri) -> String = { contentResolver, uri ->
        var displayName: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        cursor?.close()
        displayName ?: ""
    }


    val getDocumentSize: (ContentResolver, Uri) -> Long = { contentResolver, uri ->
        var size: Long? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                size = it.getLong(it.getColumnIndex(OpenableColumns.SIZE))
            }
        }
        cursor?.close()
        size ?: 0
    }

    val getDocumentMimeType: (ContentResolver, Uri) -> String = { contentResolver, uri ->
        var type = ""
        var list = contentResolver.getType(uri)?.split("/")!!

        for (a in list.indices) {
            if (a == 1) {
                type = list[a]
                Log.d("TAG99", type)
            }
        }
        contentResolver.getType(uri)?.split("/")!![1].uppercase()
    }

    fun getCurrentUserRole(activity: Activity): String {
        val sharedPreferences =
            activity.getSharedPreferences(R.string.app_name.toString(), Context.MODE_PRIVATE)
        return sharedPreferences.getString("CurrentUserRole", "").toString()
    }

    fun getUriFromBitmap(context: Context, bitmap: Bitmap): Uri? {
        // Create a file in the external storage directory (or app-specific cache directory)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"
        val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val imageFile = File.createTempFile(fileName, ".jpg", storageDir)

        // Convert bitmap to JPEG and write it to the file
        val outputStream = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        // Get the URI for the file using FileProvider
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", imageFile)
    }

    fun uploadTask(context: Context, activity: Activity) {
        showButtonLoader = true
        taskDataUpload(context, activity)
    }

    fun setDefaultDate() {
        val dateFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy", Locale.getDefault())
        val currentDate = Date()
        dateTimeValue = dateFormat.format(currentDate)
    }

    fun taskDataUpload(context: Context, activity: Activity) {
        val task = hashMapOf(
            "name" to name,
            "role" to modulesIncluded,
            "organization" to projectName,
            "email" to email,
            "address" to assignTo,
            "dateTimeValue" to dateTimeValue,
            "status" to "Opened",
            "createdBy" to getCurrentUserRole(activity),
        )

        firebaseFirestore.collection("tasks").add(task).addOnSuccessListener {
            val leadId = mapOf("id" to it.id)
            firebaseFirestore.collection("tasks").document(it.id).update(leadId)
                .addOnSuccessListener {
                    Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                    activity.startActivity(Intent(activity, Home::class.java))
                }
        }.addOnFailureListener { e ->
            Log.d("TAG225", e.toString())
        }
    }

    fun validation() {
        validate =
            !(name == "" || projectName == "" || modulesIncluded == "" || email == "" || !isValidEmail(email) || assignTo == "" || dateTimeValue == "")
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }
}