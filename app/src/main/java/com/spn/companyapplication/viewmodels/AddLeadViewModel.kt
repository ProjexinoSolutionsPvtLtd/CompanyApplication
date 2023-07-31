package com.spn.companyapplication.viewmodels

import android.content.ContentResolver
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddLeadViewModel(): ViewModel(){
    private val dateTimeFormat = SimpleDateFormat("HH:mm dd-MMM-yyyy")

    var dateTimeValue by mutableStateOf("")
    var dateTime: Calendar by mutableStateOf(Calendar.getInstance())

    var name by mutableStateOf("")
    var organization by mutableStateOf("")
    var role by mutableStateOf("")
    var number by mutableStateOf("")
    var email by mutableStateOf("")
    var address by mutableStateOf("")
    var requirement by mutableStateOf("")

    var selectedDocumentUri: Uri? by mutableStateOf(null)
    var selectedDocumentName: String by mutableStateOf("")
    var selectedDocumentSize: Long by mutableStateOf(0L)
    var selectedDocumentMimeType: String by mutableStateOf("")

    var capturedBitmap by mutableStateOf<Bitmap?>(null)
    var showImage by mutableStateOf(false)

    fun nameChange(text: String){
        name = text
    }

    fun organizationChange(text: String){
        organization = text
    }

    fun roleChange(text: String){
        role = text
    }

    fun emailChange(text: String){
        email = text
    }

    fun numberChange(text: String){
        number = text
    }

    fun addressChange(text: String){
        address = text
    }

    fun requirementChange(text: String){
        requirement = text
    }

    fun onDateTimeSelect(time: Calendar) {
        dateTime = time
        dateTimeValue = dateTimeFormat.format(time.time)
    }

    fun getDocumentName(contentResolver: ContentResolver, uri: Uri): String? {
        var displayName: String? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        cursor?.close()
        return displayName
    }

    fun getDocumentSize(contentResolver: ContentResolver, uri: Uri): Long? {
        var size: Long? = null
        val cursor: Cursor? = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                size = it.getLong(it.getColumnIndex(OpenableColumns.SIZE))
            }
        }
        cursor?.close()
        return size
    }

    fun getDocumentMimeType(contentResolver: ContentResolver, uri: Uri): String? {
        var type = ""
        var list = contentResolver.getType(uri)?.split("/")!!

        for (a in list.indices){
            if(a == 1){
                type = list[a]
                Log.d("TAG99", type)
            }
        }
        return contentResolver.getType(uri)?.split("/")!![1].uppercase()
    }
}