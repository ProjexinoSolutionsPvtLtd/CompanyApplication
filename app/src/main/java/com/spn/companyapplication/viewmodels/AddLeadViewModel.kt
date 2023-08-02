package com.spn.companyapplication.viewmodels

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
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
import com.spn.companyapplication.models.Lead
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddLeadViewModel() : ViewModel() {
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

    var validate by mutableStateOf(false)

    var imageUrl = ""
    var documentUrl = ""
    val firebaseAuth = FirebaseAuth.getInstance()
    val firebaseFirestore = FirebaseFirestore.getInstance()
    val storageReference =
        FirebaseStorage.getInstance().reference

    fun nameChange(text: String) {
        name = text
    }

    fun organizationChange(text: String) {
        organization = text
    }

    fun roleChange(text: String) {
        role = text
    }

    fun emailChange(text: String) {
        email = text
    }

    fun numberChange(text: String) {
        number = text
    }

    fun addressChange(text: String) {
        address = text
    }

    fun requirementChange(text: String) {
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

        for (a in list.indices) {
            if (a == 1) {
                type = list[a]
                Log.d("TAG99", type)
            }
        }
        return contentResolver.getType(uri)?.split("/")!![1].uppercase()
    }

    fun getCurrentUserRole(): String {
        val userId = firebaseAuth.currentUser?.uid.toString()

        var currentUserRole = "Admin"

        firebaseFirestore.collection("users").document(userId).get().addOnSuccessListener {
            currentUserRole = it.get("role").toString()
            Log.d("TAG39", currentUserRole)
        }.addOnFailureListener { e ->
            Log.d("TAG41", e.toString())
        }

        return currentUserRole
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

    fun uploadLead(context: Context) {
        if (capturedBitmap != null) {
            imageUpload(context)
        }

        if (selectedDocumentUri != null && capturedBitmap == null) {
            documentUpload(context)
        }

        if (selectedDocumentUri == null && capturedBitmap == null) {
            leadDataUpload(context)
        }
    }


    fun imageUpload(context: Context) {
        val imageReference =
            storageReference.child("lead_images/${System.currentTimeMillis()}.jpg")

        getUriFromBitmap(context = context, bitmap = capturedBitmap!!)?.let {
            imageReference.putFile(it).continueWithTask { imageTask ->
                if (!imageTask.isSuccessful) {
                    imageTask.exception?.let { throw it }
                }
                return@continueWithTask imageReference.downloadUrl

            }.addOnSuccessListener { imageTask ->
                Log.d("TAG169", imageTask.toString())
                imageUrl = imageTask.toString()

                if (selectedDocumentUri != null) {
                    documentUpload(context)
                } else {
                    leadDataUpload(context)
                }

            }.addOnFailureListener { e ->
                Log.d("TAG169", e.toString())
            }
        }

    }

    fun documentUpload(context: Context) {
        val documentReference =
            storageReference.child("lead_documents/${selectedDocumentUri!!.lastPathSegment}")

        documentReference.putFile(selectedDocumentUri!!).continueWithTask { documentTask ->
            if (!documentTask.isSuccessful) {
                documentTask.exception?.let { throw it }
            }
            return@continueWithTask documentReference.downloadUrl

        }.addOnCompleteListener { documentTask ->
            documentUrl = documentTask.result.toString()

            leadDataUpload(context)
        }.addOnFailureListener { e ->
            Log.d("TAG180", e.toString())
        }

    }

    fun leadDataUpload(context: Context) {
        val lead = hashMapOf(
            "name" to name,
            "role" to role,
            "number" to number,
            "organization" to organization,
            "email" to email,
            "requirement" to requirement,
            "address" to address,
            "dateTimeValue" to dateTimeValue,
            "status" to "Opened",
            "createdBy" to getCurrentUserRole(),
            "documentUrl" to documentUrl,
            "documentName" to selectedDocumentName,
            "imageUrl" to imageUrl
        )

        firebaseFirestore.collection("leads").add(lead).addOnSuccessListener {
            Toast.makeText(context, "Lead Added Successfully", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Log.d("TAG225", e.toString())
        }
    }

    fun validation() {
        validate =
            !(name == "" || organization == "" || role == "" || number == "" || email == "" || address == "" || requirement == "" || dateTimeValue == "")
    }
}