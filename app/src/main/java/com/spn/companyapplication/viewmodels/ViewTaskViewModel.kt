package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.content.*
import android.net.Uri
import com.itextpdf.text.Document
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import com.google.firebase.firestore.FirebaseFirestore
import com.spn.companyapplication.R
import com.spn.companyapplication.models.Task
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class ViewTaskViewModel() : ViewModel() {
    val filterOptions = listOf("Opened", "In-Progress", "Testing", "Completed")
    val dateSortOptions = listOf("Ascending", "Descending")
    val downloadOptions = listOf("Excel", "PDF")
    var showOptions by mutableStateOf(false)

    var showImage by mutableStateOf(false)
    var currentImageUrl by mutableStateOf("")

    var ascending by mutableStateOf(false)
    var showDateSortOptions by mutableStateOf(false)

    var showDownloadOptions by mutableStateOf(false)

    var selectedOption by mutableStateOf("")
    var dateSortSelectedOption by mutableStateOf("")

    var currentTaskId by mutableStateOf("")
    var selectedStatusForUpdate by mutableStateOf("Select Status")
    var downloadOption by mutableStateOf("")
    var commentForStatusUpdate by mutableStateOf("")
    var showStatusUpdateOptions by mutableStateOf(false)
    val setShowStatusUpdateOptions: (Boolean) -> Unit = { status ->
        showStatusUpdateOptions = status
    }

    var showContent by mutableStateOf(false)

    var showUpdateDialog by mutableStateOf(false)

    var tasksList = mutableListOf<Task>()
    var completeTasksList = mutableListOf<Task>()

    @RequiresApi(Build.VERSION_CODES.Q)
    fun generatePDF(tasksList: List<Task>, context: Context) {
        val document = Document()

        try {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Tasks.pdf")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")

            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )

            if (uri != null) {
                val fileOutputStream = context.contentResolver.openOutputStream(uri)
                PdfWriter.getInstance(document, fileOutputStream)

                document.open()

                for (task in tasksList) {
                    document.add(Paragraph("Name: ${task.name}"))
                    document.add(Paragraph("Project Name: ${task.projectName}"))
                    document.add(Paragraph("Modules Included: ${task.modulesIncluded}"))
                    document.add(Paragraph("Deadline: ${task.deadline}"))
                    document.add(Paragraph("Assign To: ${task.assignTo}"))
                    document.add(Paragraph("Mail ID: ${task.email}"))
                    document.add(Paragraph("Status: ${task.status}"))
                    document.add(Paragraph("\n"))
                }

                document.close()

                Toast.makeText(context, "PDF Generated Successfully in Downloads", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Error creating file", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("TAG190", e.message.toString())
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportTasksToExcel(
        tasks: List<Task>,
        contentResolver: ContentResolver,
        activity: Activity
    ) {
        val fileName = "Tasks.xlsx"
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
        contentValues.put(
            MediaStore.MediaColumns.MIME_TYPE,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
        )

        val uri: Uri? =
            contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)

        try {
            if (uri != null) {
                val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                if (outputStream != null) {
                    // Now you can write to the Excel file using POI
                    val workbook: Workbook = XSSFWorkbook()
                    val sheet: Sheet = workbook.createSheet("Tasks")

                    val headerRow: Row = sheet.createRow(0)
                    val taskProperties =
                        Task::class.java.declaredFields.filter { it.name != "\$stable" && it.name != "id" && it.name != "documentUrl" && it.name != "documentName" && it.name != "documentType" && it.name != "imageUrl" }
                    for ((index, property) in taskProperties.withIndex()) {
                        val cell = headerRow.createCell(index)
                        cell.setCellValue(property.name.capitalize())
                    }

                    for ((rowIndex, task) in tasks.withIndex()) {
                        val dataRow = sheet.createRow(rowIndex + 1)
                        for ((columnIndex, property) in taskProperties.withIndex()) {
                            val cell = dataRow.createCell(columnIndex)

                            // Set cell value based on task property type
                            property.isAccessible = true
                            val value = property.get(task)
                            when (value) {
                                is String -> cell.setCellValue(value)
                                is Int -> cell.setCellValue(value.toDouble())
                                else -> cell.setCellValue(value.toString())
                            }
                        }
                    }
                    workbook.write(outputStream)
                    workbook.close()
                    outputStream.flush()
                    outputStream.close()
                    Log.d("TAG61", "Excel File Generated Successfully in Downloads")
                }
            }

            val toast = "Excel File Generated Successfully"
            val centeredText = SpannableString(toast)
            centeredText.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
                0,
                toast.length - 1,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            Toast.makeText(activity, centeredText, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun createTextFromTaskList(activity: Activity) {
        val builder = StringBuilder()

        for (task in tasksList) {
            builder.append("Name: ${task.name}\n")
            builder.append("Project Name: ${task.projectName}\n")
            builder.append("Modules Included: ${task.modulesIncluded}\n")
            builder.append("Deadline: ${task.deadline}\n")
            builder.append("Email: ${task.email}\n")
            builder.append("Assigned To: ${task.assignTo}\n")
            builder.append("Status: ${task.status}\n")
            builder.append("\n\n") // Separate each task
        }

        shareTaskListViaText(activity, builder.toString())
    }

    fun shareTaskListViaText(activity: Activity, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)

        activity.startActivity(Intent.createChooser(shareIntent, "Share Task List"))
    }


    fun hideLoader() {
        showContent = true
    }

    fun showLoader() {
        showContent = false
    }

    fun onStatusFilterDropdownOptionSelect(text: String) {
        selectedOption = text
    }

    fun onDateSortDropdownOptionSelect(text: String) {
        dateSortSelectedOption = text
    }

    fun onStatusUpdateDropdownOptionSelect(text: String) {
        selectedStatusForUpdate = text
    }

    fun onDownloadOptionSelect(text: String) {
        downloadOption = text
    }

    fun setupTasksListener(activity: Activity) {
        val firestore = FirebaseFirestore.getInstance()
        val leadsCollection = firestore.collection("tasks")

        leadsCollection.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TAG253", "Error listening to tasks: $exception")
                return@addSnapshotListener
            }

            snapshot?.let { querySnapshot ->
                fetchTasks(activity)
            }
        }
    }

    fun fetchTasks(activity: Activity) {
        showLoader()
        tasksList.clear()
        val firestore = FirebaseFirestore.getInstance()
        val tasksCollection = firestore.collection("tasks")

        tasksCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val task = document.toObject(Task::class.java)
                    task?.let {
                        tasksList.add(it)
                        completeTasksList.add(it)
                    }
                }

                tasksList.forEach {
                    Log.d("TAG116", it.name)
                }

                var _tasksList: MutableList<Task> = mutableListOf()
                _tasksList.addAll(tasksList)

                //Logic for bifurcating lists according to User Type
//                if (getCurrentUserRole(activity) != "Admin") {
//                    _tasksList.forEach {
//                        if (it.createdBy == "Admin") {
//                            tasksList.remove(it)
//                        }
//                    }
//                }

                //Logic for bifurcating lists according to Status Filter Option
                if (selectedOption != "") {
                    tasksList.forEach {
                        if (!_tasksList.contains(it)) {
                            _tasksList.add(it)
                        }
                    }

                    _tasksList.forEach {
                        if (it.status != selectedOption) {
                            tasksList.remove(it)
                        }
                    }
                }

                hideLoader()
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error fetching tasks: $exception")

                hideLoader()
            }
    }

    fun deleteTask(taskId: String) {

        val firestore = FirebaseFirestore.getInstance()

        val tasksCollection = firestore.collection("tasks")
        tasksCollection.document(taskId)
            .delete()
            .addOnSuccessListener {

                Log.d("TAG253", "Works delete to tasks")

            }
            .addOnFailureListener { e ->
                Log.e("TAG253", "Fails delete to tasks")

            }
    }

    fun getStatusFilterSelectedOption(): String {
        return selectedOption
    }

    fun getDateSortTypeSelectedOption(): String {
        return dateSortSelectedOption
    }

    fun getCurrentUserRole(activity: Activity): String {
        val sharedPreferences =
            activity.getSharedPreferences(R.string.app_name.toString(), Context.MODE_PRIVATE)
        return sharedPreferences.getString("CurrentUserRole", "").toString()
    }

    fun openDocument(context: Context, documentUrl: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(documentUrl)
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            val toast = "There was no application found to open the document"
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

    fun sortTasksByDate(): List<Task> {
        val dateFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy", Locale.getDefault())

        val sortedTasks = if (dateSortSelectedOption == "Ascending") {
            completeTasksList.sortedBy { task -> dateFormat.parse(task.deadline) ?: Date() }
        } else {
            completeTasksList.sortedByDescending { task ->
                dateFormat.parse(task.deadline) ?: Date()
            }
        }

        showLoader()

        tasksList.clear()
        tasksList.addAll(sortedTasks)

        hideLoader()

        return sortedTasks
    }

    fun onSearch(searchQuery: String) {
        showLoader()

        val query = searchQuery.lowercase()

        if (query != "" || query.isNotBlank() || query.isNotEmpty()) {
            val _tasksList = mutableListOf<Task>()
            _tasksList.addAll(completeTasksList)

            tasksList.clear()

            tasksList = _tasksList.filter { task ->
                task.name.lowercase().contains(query) ||
//                        task.role.lowercase().contains(query) ||
//                        task.requirement.lowercase().contains(query) ||
                        task.status.lowercase().contains(query)
//                        task.organization.lowercase().contains(query)
            } as MutableList<Task>
        } else {
            tasksList.clear()
            tasksList.addAll(completeTasksList)
        }

        hideLoader()
    }

    fun updateStatus(taskId: String, context: Context) {
        val firebaseFirestore = FirebaseFirestore.getInstance()

        val taskReference = firebaseFirestore.collection("tasks").document(taskId)

        val updates = mapOf(
            "status" to selectedStatusForUpdate
        )

        taskReference.update(updates)
            .addOnSuccessListener {
//                Toast.makeText(context, "Status Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->

            }
    }

    fun commentForStatusUpdateChange(text: String) {
        commentForStatusUpdate = text
    }

    fun clearDialogDetails() {
        showUpdateDialog = false
        selectedStatusForUpdate = "Select Status"
        commentForStatusUpdate = ""
    }

    fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
        when (status) {
            "Opened" -> return Color(("#008080").toColorInt()).copy(0.5f)
            "Contacted" -> return Color(("#191744").toColorInt()).copy(0.5f)
            "Hold" -> return Color(("#0054a6").toColorInt()).copy(0.5f)
            "Lost/Closed" -> return Color(("#001f3f").toColorInt()).copy(0.5f)
            "Converted" -> return Color(("#87ceeb").toColorInt()).copy(0.5f)
        }


        return Color(("#103b5c").toColorInt()).copy(0.5f)
    }

}