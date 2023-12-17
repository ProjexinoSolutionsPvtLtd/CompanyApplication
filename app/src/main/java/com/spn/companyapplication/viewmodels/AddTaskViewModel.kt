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

    val firebaseFirestore = FirebaseFirestore.getInstance()


    fun nameChange(text: String) {
        name = text
        validation()
    }

    fun projectNameChange(text: String) {
        projectName = text
        validation()
    }

    fun modulesIncludedChange(text: String) {
        modulesIncluded = text
        validation()
    }

    fun emailChange(text: String) {
        email = text
        validation()
    }

    private val assignToChangeFunctions: MutableList<(String) -> Unit> = mutableListOf()

    fun getAssignToChangeFunction(index: Int): (String) -> Unit {
        return assignToChangeFunctions.getOrElse(index) { { _ -> /* Default empty function */ } }
    }
    fun addAssignToChangeFunction() {
        assignToChangeFunctions.add { newValue -> assignTo = newValue }
    }

    fun removeLastAssignToChangeFunction() {
        assignToChangeFunctions.removeAt(assignToChangeFunctions.lastIndex)
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
        validation()
    }

    fun uploadTask(context: Context, activity: Activity, assignToList: List<String>) {
        showButtonLoader = true
        taskDataUpload(context, activity, assignToList)
    }

    fun setDefaultDate() {
        val dateFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy", Locale.getDefault())
        val currentDate = Date()
        deadline = dateFormat.format(currentDate)
    }

    fun taskDataUpload(context: Context, activity: Activity,assignToList: List<String>) {
        val task = hashMapOf(
            "name" to name,
            "modulesIncluded" to modulesIncluded,
            "projectName" to projectName,
            "email" to email,
            "assignTo" to assignToList,
            "deadline" to deadline,
            "status" to "Opened"
        )

        firebaseFirestore.collection("tasks").add(task).addOnSuccessListener {
            val taskId = mapOf("id" to it.id)
            firebaseFirestore.collection("tasks").document(it.id).update(taskId)
                .addOnSuccessListener {
                    Toast.makeText(context, "Task Added Successfully", Toast.LENGTH_SHORT).show()
                    val content = "<p style=\"font-size: 16px; font-weight: bold;\">You have been assigned a new task! Here are the details: -</p>\n" +
                            "<p><b>Project Name:</b> $projectName</p>\n" +
                            "<p><b>Task Name:</b> $name</p>\n" +
                            "<p><b>Modules Included:</b> $modulesIncluded</p>\n" +
                            "<p><b>Deadline:</b> $deadline</p>"
                    assignToList.forEach { assignedUser ->
                        sendEmail(assignedUser, "New Task Assigned", content, context) {
                            // onSuccess logic, if needed
                        }
                    }
                    activity.startActivity(Intent(activity, Home::class.java))
                }

        }.addOnFailureListener { e ->
            Log.d("TAG225", e.toString())
        }

    }

    fun validation() {
//        val allFieldsFilled = name.isNotBlank() && projectName.isNotBlank() && modulesIncluded.isNotBlank() && email.isNotBlank() && deadline.isNotBlank()
//        val validEmails = fieldValues.all { isValidEmail(it.trim()) }
//        validate = allFieldsFilled && validEmails
        validate =
            !(name == "" || projectName == "" || modulesIncluded == "" || email == "" || !isValidEmail(email) || deadline == "")
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }
}