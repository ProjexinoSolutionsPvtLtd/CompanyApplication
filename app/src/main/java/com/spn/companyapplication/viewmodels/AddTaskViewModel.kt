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

    fun taskDataUpload(context: Context, activity: Activity, assignToList: List<String>) {
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
                    val content = """
    <html lang="en">
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Simple Transactional Email</title>
            <style media="all" type="text/css">
    body {
        background-color: #ffffff;
        margin: 0;
        padding: 0;
    }

    .body {
        background-color: #ffffff;
        width: 100%;
    }

    .container {
        margin: 0 auto !important;
        max-width: 600px;
        padding: 0;
        padding-top: 24px;
        width: 100%;
    }

    .content {
        box-sizing: border-box;
        display: block;
        margin: 0 auto;
        max-width: 600px;
        padding: 0;
    }

    .main {
        background: #d56c6c;
        border: 1px solid #385a9d;
        border-radius: 16px;
        width: 100%;
    }

    .wrapper,
    .input-box {
        box-sizing: border-box;
        padding: 24px;
    }

    .footer {
        clear: both;
        padding-top: 24px;
        text-align: center;
        width: 100%;
    }

    .footer td,
    .footer p,
    .footer span,
    .footer a {
        color: #9a9ea6;
        font-size: 16px;
        text-align: center;
    }

    p {
        font-family: Helvetica, sans-serif;
        font-size: 16px;
        font-weight: normal;
        margin: 0;
        margin-bottom: 16px;
    }

    a {
        color: #0867ec;
        text-decoration: underline;
    }

    .btn {
        box-sizing: border-box;
        min-width: 100% !important;
        width: 100%;
    }

    .btn > tbody > tr > td {
        padding-bottom: 16px;
    }

    .btn table {
        width: auto;
    }

    .btn table td {
        background-color: #ffffff;
        border-radius: 4px;
        text-align: center;
    }

    .btn a {
        background-color: #ffffff;
        border: solid 2px #0867ec;
        border-radius: 4px;
        box-sizing: border-box;
        color: #0867ec;
        cursor: pointer;
        display: inline-block;
        font-size: 16px;
        font-weight: bold;
        margin: 0;
        padding: 12px 24px;
        text-decoration: none;
        text-transform: capitalize;
    }

    .btn-primary table td {
        background-color: #0867ec;
    }

    .btn-primary a {
        background-color: #0867ec;
        border-color: #0867ec;
        color: #ffffff;
    }

    @media all {
        .btn-primary table td:hover {
            background-color: #ec0867 !important;
        }

        .btn-primary a:hover {
            background-color: #ec0867 !important;
            border-color: #ec0867 !important;
        }
    }

    .last {
        margin-bottom: 0;
    }

    .first {
        margin-top: 0;
    }

    .align-center {
        text-align: center;
    }

    .align-right {
        text-align: right;
    }

    .align-left {
        text-align: left;
    }

    .text-link {
        color: #0867ec !important;
        text-decoration: underline !important;
    }

    .clear {
        clear: both;
    }

    .mt0 {
        margin-top: 0;
    }

    .mb0 {
        margin-bottom: 0;
    }

    .preheader {
        color: transparent;
        display: none;
        height: 0;
        max-height: 0;
        max-width: 0;
        opacity: 0;
        overflow: hidden;
        mso-hide: all;
        visibility: hidden;
        width: 0;
    }

    .powered-by a {
        text-decoration: none;
    }

    @media only screen and (max-width: 600px) {
        .container {
            width: 100% !important;
        }

        .btn {
            width: 100% !important;
        }

        .btn a {
            width: 100% !important;
        }

        .input-box {
            width: 100% !important;
        }
    }

    @media only screen and (max-width: 480px) {
        .wrapper {
            padding: 16px !important;
        }
    }
</style>

        </head>
        <body>
            <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                <tbody>
                    <tr>
                        <td class="container">
                            <div class="content">
                                <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="main">
                                    <tbody>
                                        <tr>
                                            <td class="wrapper">
                                                <img src="https://i.postimg.cc/WpZ696ks/removal-ai-321eab78-081c-408b-b258-af45226cb630-projexino.png" style="margin: 0; padding-top: 12px; padding-bottom: 12px; padding-right: auto; padding-left: auto; width: 100%;">
                                                <p><b>New Task Assignment – Let’s Take on a Challenge!.</b></p>
                                                <p>Hope you're doing well. I've got some exciting news for you! You've been assigned a new task that we think will really highlight your skills and make a big impact.</p>
                                                <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="btn btn-primary">
                                                    <tbody>
                                                        <tr>
                                                            <td align="left">
                                                                <a href="#" style="background-color: #3498db; border: 1px solid #2980b9; border-radius: 4px; color: #ffffff; cursor: pointer; display: inline-block; font-size: 16px; font-weight: bold; margin: 0; padding: 10px 20px; text-decoration: none; text-transform: capitalize; width: 85%;">New Task - $name</a><br><br>
                                                                <a href="#" style="background-color: #3498db; border: 1px solid #2980b9; border-radius: 4px; color: #ffffff; cursor: pointer; display: inline-block; font-size: 16px; font-weight: bold; margin: 0; padding: 10px 20px; text-decoration: none; text-transform: capitalize; width: 85%;">Modules - $modulesIncluded</a><br><br>
                                                                <a href="#" style="background-color: #3498db; border: 1px solid #2980b9; border-radius: 4px; color: #ffffff; cursor: pointer; display: inline-block; font-size: 16px; font-weight: bold; margin: 0; padding: 10px 20px; text-decoration: none; text-transform: capitalize; width: 85%;">Deadline - $deadline</a><br><br>
                                                                <div class="input-container"></div>
                                                            </td>
                                                        </tr>
                                                    </tbody>
                                                </table>
                                                <p>Thank you for your continued hard work and dedication to excellence. We look forward to the successful completion of this task and the valuable contributions you will undoubtedly make.</p>
                                                <p>Good luck!</p>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                        <td>&nbsp;</td>
                    </tr>
                </tbody>
            </table>
        </body>
    </html>
""".trimIndent()

//                    val content = "<p style=\"font-size: 16px; font-weight: bold;\">You have been assigned a new task! Here are the details: -</p>\n" +
//                            "<p><b>Project Name:</b> $projectName</p>\n" +
//                            "<p><b>Task Name:</b> $name</p>\n" +
//                            "<p><b>Modules Included:</b> $modulesIncluded</p>\n" +
//                            "<p><b>Deadline:</b> $deadline</p>"
                    assignToList.forEach { assignedUser ->
                        sendEmail(assignedUser, email, "New Task Assigned", content, context) {
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
            !(name == "" || projectName == "" || modulesIncluded == "" || email == "" || !isValidEmail(
                email
            ) || deadline == "")
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return emailRegex.matches(email)
    }
}