package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.content.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.style.LeadingMarginSpan
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.spn.companyapplication.R
import com.spn.companyapplication.models.Lead
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.OutputStream


class ViewLeadViewModel(): ViewModel(){
    val filterOptions = listOf("Opened", "Contacted", "Hold", "Lost/Closed", "Converted")
    var showOptions by mutableStateOf(false)
    var selectedOption by mutableStateOf("")

    var showContent by mutableStateOf(false)

    var leadsList = mutableListOf<Lead>()
    var completeLeadsList = mutableListOf<Lead>()

    @RequiresApi(Build.VERSION_CODES.Q)
    fun exportLeadsToExcel(leads: List<Lead>, contentResolver: ContentResolver) {
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
                    val leadProperties = Lead::class.java.declaredFields.filter { it.name != "\$stable" && it.name != "id" }
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
                    Log.d("TAG61", "Excel file created successfully.")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun hideLoader(){
        showContent = true
    }

    fun showLoader(){
        showContent = false
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

                var _leadsList:  MutableList<Lead> = mutableListOf()
                _leadsList.addAll(leadsList)

                //Logic for bifurcating lists according to User Type
                if(getCurrentUserRole(activity) != "Admin"){
                    _leadsList.forEach {
                        if (it.createdBy == "Admin"){
                            leadsList.remove(it)
                        }
                    }
                }

                //Logic for bifurcating lists according to Sort Option
                if(selectedOption != ""){
                    leadsList.forEach {
                        if(!_leadsList.contains(it)){
                            _leadsList.add(it)
                        }
                    }

                    _leadsList.forEach {
                        if(it.status != selectedOption){
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

    fun getCurrentUserRole(activity: Activity): String {
        val sharedPreferences = activity.getSharedPreferences(R.string.app_name.toString(), Context.MODE_PRIVATE)
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
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, toast.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            Toast.makeText(context, centeredText, Toast.LENGTH_SHORT).show()
        }
    }

    fun onSearch(searchQuery: String){
        showLoader()

        val query = searchQuery.lowercase()

        if(query != "" || query.isNotBlank() || query.isNotEmpty()){
            val _leadsList = mutableListOf<Lead>()
            _leadsList.addAll(leadsList)

            leadsList.clear()

            leadsList = _leadsList.filter { lead ->
                lead.name.lowercase().contains(query) ||
                        lead.role.lowercase().contains(query) ||
                        lead.requirement.lowercase().contains(query) ||
                        lead.status.lowercase().contains(query) ||
                        lead.organization.lowercase().contains(query)
            } as MutableList<Lead>
        }
        else{
            leadsList.clear()
            leadsList.addAll(completeLeadsList)
        }

        hideLoader()
    }
}