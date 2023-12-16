package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.content.ContentResolver
import androidx.compose.runtime.remember
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.spn.companyapplication.R
import com.spn.companyapplication.screens.Home
import com.spn.companyapplication.services.sendEmail
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTaskViewModel : ViewModel() {
    private val dateTimeFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy")

    var deadline by mutableStateOf("")
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


    val subjectState =  mutableStateOf("")
    val emailState =  mutableStateOf("")
    val contentState =  mutableStateOf("")
    val buttonText =  mutableStateOf("")
    val emailErrorState =  mutableStateOf("")

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
            deadline = dateTimeFormat.format(time.time)
        } else {
            val toast = "Deadline cannot be set to a date earlier than the current one"
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

    fun uploadTask(context: Context, activity: Activity) {
        showButtonLoader = true
        taskDataUpload(context, activity)
    }

    fun setDefaultDate() {
        val dateFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy", Locale.getDefault())
        val currentDate = Date()
        deadline = dateFormat.format(currentDate)
    }

    fun taskDataUpload(context: Context, activity: Activity) {
        val task = hashMapOf(
            "name" to name,
            "modulesIncluded" to modulesIncluded,
            "projectName" to projectName,
            "email" to email,
            "assignTo" to assignTo,
            "deadline" to deadline,
            "status" to "Opened"
        )

        firebaseFirestore.collection("tasks").add(task).addOnSuccessListener {
            val taskId = mapOf("id" to it.id)
            firebaseFirestore.collection("tasks").document(it.id).update(taskId)
                .addOnSuccessListener {
                    Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                    val content = "You have been assigned a new task! Here are the details: -\n\nProject Name: $projectName\nTask Name: $name\nModules Included: $modulesIncluded\nDeadline: $deadline"
                    sendEmail(assignTo, "New Task Assigned", content, context, onSuccess = {
                    })
                    activity.startActivity(Intent(activity, Home::class.java))
                }

        }.addOnFailureListener { e ->
            Log.d("TAG225", e.toString())
        }

    }

    fun validation() {
        validate =
            !(name == "" || projectName == "" || modulesIncluded == "" || email == "" || !isValidEmail(email) || assignTo == "" || !isValidEmail(assignTo) || deadline == "")
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }
}