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
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import android.os.Environment
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.spn.companyapplication.R
import com.spn.companyapplication.models.Lead
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*


class ViewLeadViewModel() : ViewModel() {
    val filterOptions = listOf("Opened", "Contacted", "Hold", "Lost/Closed", "Converted")
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

    var currentLeadId by mutableStateOf("")
    var selectedStatusForUpdate by mutableStateOf("Select Status")
    var downloadOption by mutableStateOf("")
    var commentForStatusUpdate by mutableStateOf("")
    var showStatusUpdateOptions by mutableStateOf(false)

    var showContent by mutableStateOf(false)

    var showUpdateDialog by mutableStateOf(false)

    var leadsList = mutableListOf<Lead>()
    var completeLeadsList = mutableListOf<Lead>()

    @RequiresApi(Build.VERSION_CODES.Q)
    fun generatePDF(leadsList: List<Lead>, context: Context) {
        val document = Document()

        try {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Leads.pdf")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")

            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            )

            if (uri != null) {
                val fileOutputStream = context.contentResolver.openOutputStream(uri)
                PdfWriter.getInstance(document, fileOutputStream)

                document.open()

                for (lead in leadsList) {
                    document.add(Paragraph("Name: ${lead.name}"))
                    document.add(Paragraph("Role: ${lead.role}"))
                    document.add(Paragraph("Number: ${lead.number}"))
                    document.add(Paragraph("Organization: ${lead.organization}"))
                    document.add(Paragraph("Email: ${lead.email}"))
                    document.add(Paragraph("Requirement: ${lead.requirement}"))
                    document.add(Paragraph("Address: ${lead.address}"))
                    document.add(Paragraph("Date & Time: ${lead.dateTimeValue}"))
                    document.add(Paragraph("Status: ${lead.status}"))
                    document.add(Paragraph("Created By: ${lead.createdBy}"))
                    document.add(Paragraph("\n"))
                }

                document.close()

                Toast.makeText(context, "PDF Generated Successfully", Toast.LENGTH_SHORT).show()
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
    fun exportLeadsToExcel(leads: List<Lead>, contentResolver: ContentResolver, activity: Activity) {
        val fileName = "Leads.xlsx"
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
                    val sheet: Sheet = workbook.createSheet("Leads")

                    val headerRow: Row = sheet.createRow(0)
                    val leadProperties =
                        Lead::class.java.declaredFields.filter { it.name != "\$stable" && it.name != "id" && it.name != "documentUrl" && it.name != "documentName" && it.name != "documentType" && it.name != "imageUrl"}
                    for ((index, property) in leadProperties.withIndex()) {
                        val cell = headerRow.createCell(index)
                        cell.setCellValue(property.name.capitalize())
                    }

                    for ((rowIndex, lead) in leads.withIndex()) {
                        val dataRow = sheet.createRow(rowIndex + 1)
                        for ((columnIndex, property) in leadProperties.withIndex()) {
                            val cell = dataRow.createCell(columnIndex)

                            // Set cell value based on lead property type
                            property.isAccessible = true
                            val value = property.get(lead)
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
                    Log.d("TAG61", "Excel File Generated Successfully")
                }
            }

            val toast = "Excel File Generated Successfully"
            val centeredText = SpannableString(toast)
            centeredText.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, toast.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            Toast.makeText(activity, centeredText, Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun createTextFromLeadList(activity: Activity) {
        val builder = StringBuilder()

        for (lead in leadsList) {
            builder.append("Name: ${lead.name}\n")
            builder.append("Role: ${lead.role}\n")
            builder.append("Number: ${lead.number}\n")
            builder.append("Organization: ${lead.organization}\n")
            builder.append("Email: ${lead.email}\n")
            builder.append("Requirement: ${lead.requirement}\n")
            builder.append("Address: ${lead.address}\n")
            builder.append("Date & Time: ${lead.dateTimeValue}\n")
            builder.append("Status: ${lead.status}\n")
            builder.append("\n\n") // Separate each lead
        }

        shareLeadListViaText(activity, builder.toString())
    }

    fun shareLeadListViaText(activity: Activity, text: String) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)

        activity.startActivity(Intent.createChooser(shareIntent, "Share Lead List"))
    }



    fun hideLoader() {
        showContent = true
    }

    fun showLoader() {
        showContent = false
    }

    fun onStatusFilterDropdownOptionSelect(text: String){
        selectedOption = text
    }

    fun onDateSortDropdownOptionSelect(text: String){
        dateSortSelectedOption = text
    }

    fun onStatusUpdateDropdownOptionSelect(text: String){
        selectedStatusForUpdate = text
    }

    fun onDownloadOptionSelect(text: String){
        downloadOption = text
    }

    fun fetchLeads(activity: Activity) {
        showLoader()
        leadsList.clear()
        val firestore = FirebaseFirestore.getInstance()
        val leadsCollection = firestore.collection("leads")

        leadsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val lead = document.toObject(Lead::class.java)
                    lead?.let {
                        leadsList.add(it)
                        completeLeadsList.add(it)
                    }
                }

                leadsList.forEach {
                    Log.d("TAG116", it.name)
                }

                var _leadsList: MutableList<Lead> = mutableListOf()
                _leadsList.addAll(leadsList)

                //Logic for bifurcating lists according to User Type
                if (getCurrentUserRole(activity) != "Admin") {
                    _leadsList.forEach {
                        if (it.createdBy == "Admin") {
                            leadsList.remove(it)
                        }
                    }
                }

                //Logic for bifurcating lists according to Status Filter Option
                if (selectedOption != "") {
                    leadsList.forEach {
                        if (!_leadsList.contains(it)) {
                            _leadsList.add(it)
                        }
                    }

                    _leadsList.forEach {
                        if (it.status != selectedOption) {
                            leadsList.remove(it)
                        }
                    }
                }

                hideLoader()
            }
            .addOnFailureListener { exception ->
                Log.e("TAG", "Error fetching leads: $exception")

                hideLoader()
            }
    }

    fun getStatusFilterSelectedOption(): String{
        return selectedOption
    }

    fun getDateSortTypeSelectedOption(): String{
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
    fun sortLeadsByDate(): List<Lead> {
        val dateFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy", Locale.getDefault())

        val sortedLeads = if (dateSortSelectedOption == "Ascending") {
            completeLeadsList.sortedBy { lead -> dateFormat.parse(lead.dateTimeValue) ?: Date() }
        } else {
            completeLeadsList.sortedByDescending { lead -> dateFormat.parse(lead.dateTimeValue) ?: Date() }
        }

        showLoader()

        leadsList.clear()
        leadsList.addAll(sortedLeads)

        hideLoader()

        return sortedLeads
    }
    fun onSearch(searchQuery: String) {
        showLoader()

        val query = searchQuery.lowercase()

        if (query != "" || query.isNotBlank() || query.isNotEmpty()) {
            val _leadsList = mutableListOf<Lead>()
            _leadsList.addAll(completeLeadsList)

            leadsList.clear()

            leadsList = _leadsList.filter { lead ->
                lead.name.lowercase().contains(query) ||
//                        lead.role.lowercase().contains(query) ||
//                        lead.requirement.lowercase().contains(query) ||
                        lead.status.lowercase().contains(query)
//                        lead.organization.lowercase().contains(query)
            } as MutableList<Lead>
        } else {
            leadsList.clear()
            leadsList.addAll(completeLeadsList)
        }

        hideLoader()
    }

    fun updateStatus(leadId: String, context: Context) {
        val firebaseFirestore = FirebaseFirestore.getInstance()

//        Log.d("TAG219", lead.id)

        val leadReference = firebaseFirestore.collection("leads").document(leadId)

        val updates = mapOf(
            "status" to selectedStatusForUpdate
        )

        leadReference.update(updates)
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
        when (status){
            "Opened" -> return Color(("#008080").toColorInt()).copy(0.5f)
            "Contacted" -> return Color(("#191744").toColorInt()).copy(0.5f)
            "Hold" -> return Color(("#0054a6").toColorInt()).copy(0.5f)
            "Lost/Closed" -> return Color(("#001f3f").toColorInt()).copy(0.5f)
            "Converted" -> return Color(("#87ceeb").toColorInt()).copy(0.5f)
        }


        return Color(("#103b5c").toColorInt()).copy(0.5f)
    }
}