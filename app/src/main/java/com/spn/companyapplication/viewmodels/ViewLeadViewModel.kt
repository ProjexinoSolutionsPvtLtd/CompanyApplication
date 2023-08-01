package com.spn.companyapplication.viewmodels

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import com.spn.companyapplication.models.Lead
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.OutputStream


class ViewLeadViewModel(): ViewModel(){
    val sortOptions = listOf("Opened", "Contacted", "Hold", "Lost/Closed", "Converted")
    var showOptions by mutableStateOf(false)
    var selectedOption by mutableStateOf("")

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
}