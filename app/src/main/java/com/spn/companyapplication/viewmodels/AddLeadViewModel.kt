package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.widget.Toast
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
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
import com.spn.companyapplication.models.Lead
import com.spn.companyapplication.screens.Home
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddLeadViewModel() : ViewModel() {
    private val dateTimeFormat = SimpleDateFormat("HH:mm, dd-MMM-yyyy")

    var dateTimeValue by mutableStateOf("")
    var dateTime: Calendar by mutableStateOf(Calendar.getInstance())


    var name by mutableStateOf("")
    var organization by mutableStateOf("")
    var role by mutableStateOf("")
    var number by mutableStateOf("")
    var countryCode by mutableStateOf("India (+91)")
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

    var showButtonLoader by mutableStateOf(false)

    val countryCodes = listOf(
        "+1 (US)",
        "+44 (UK)",
        "+91 (India)",
        "+86 (China)",
        "+81 (Japan)",
        "+33 (France)",
        "+49 (Germany)",
        "+39 (Italy)",
        "+7 (Russia)",
        "+55 (Brazil)",
        "+52 (Mexico)",
        "+61 (Australia)",
        "+64 (New Zealand)",
        "+234 (Nigeria)",
        "+27 (South Africa)",
        "+254 (Kenya)",
        "+20 (Egypt)",
        "+971 (United Arab Emirates)",
        "+966 (Saudi Arabia)",
        "+972 (Israel)",
        "+65 (Singapore)",
        "+60 (Malaysia)",
        "+82 (South Korea)",
        "+66 (Thailand)",
        "+1 (Canada)",
        "+54 (Argentina)",
        "+58 (Venezuela)",
        "+51 (Peru)",
        "+34 (Spain)",
        "+31 (Netherlands)",
        "+32 (Belgium)",
        "+41 (Switzerland)",
        "+43 (Austria)",
        "+46 (Sweden)",
        "+47 (Norway)",
        "+358 (Finland)",
        "+48 (Poland)",
        "+420 (Czech Republic)",
        "+36 (Hungary)",
        "+421 (Slovakia)",
        "+385 (Croatia)",
        "+386 (Slovenia)",
        "+380 (Ukraine)",
        "+373 (Moldova)",
        "+373 (Moldova)",
        "+373 (Moldova)",
        "+374 (Armenia)",
        "+995 (Georgia)",
        "+374 (Armenia)",
        "+61 (Australia)",
        "+63 (Philippines)",
        "+62 (Indonesia)",
        "+65 (Singapore)",
        "+66 (Thailand)",
        "+84 (Vietnam)",
        "+92 (Pakistan)",
        "+880 (Bangladesh)",
        "+93 (Afghanistan)",
        "+94 (Sri Lanka)",
        "+95 (Myanmar)",
        "+977 (Nepal)",
        "+98 (Iran)",
        "+20 (Egypt)",
        "+211 (South Sudan)",
        "+249 (Sudan)",
        "+251 (Ethiopia)",
        "+253 (Djibouti)",
        "+254 (Kenya)",
        "+255 (Tanzania)",
        "+256 (Uganda)",
        "+257 (Burundi)",
        "+258 (Mozambique)",
        "+260 (Zambia)",
        "+261 (Madagascar)",
        "+262 (Reunion Island)",
        "+263 (Zimbabwe)",
        "+264 (Namibia)",
        "+265 (Malawi)",
        "+266 (Lesotho)",
        "+267 (Botswana)",
        "+268 (Swaziland)",
        "+269 (Comoros)",
        "+290 (Saint Helena)",
        "+291 (Eritrea)",
        "+297 (Aruba)",
        "+298 (Faroe Islands)",
        "+299 (Greenland)",
        "+350 (Gibraltar)",
        "+351 (Portugal)",
        "+352 (Luxembourg)",
        "+353 (Ireland)",
        "+354 (Iceland)",
        "+355 (Albania)",
        "+356 (Malta)",
        "+357 (Cyprus)",
        "+358 (Finland)",
        "+359 (Bulgaria)",
        "+370 (Lithuania)",
        "+371 (Latvia)",
        "+372 (Estonia)",
        "+373 (Moldova)",
        "+374 (Armenia)",
        "+375 (Belarus)",
        "+376 (Andorra)",
        "+377 (Monaco)",
        "+378 (San Marino)",
        "+379 (Vatican City)",
        "+380 (Ukraine)",
        "+381 (Serbia)",
        "+382 (Montenegro)",
        "+385 (Croatia)",
        "+386 (Slovenia)",
        "+387 (Bosnia and Herzegovina)",
        "+389 (North Macedonia)",
        "+420 (Czech Republic)",
        "+421 (Slovakia)",
        "+423 (Liechtenstein)",
        "+500 (Falkland Islands)",
        "+501 (Belize)",
        "+502 (Guatemala)",
        "+503 (El Salvador)",
        "+504 (Honduras)",
        "+505 (Nicaragua)",
        "+506 (Costa Rica)",
        "+507 (Panama)",
        "+508 (Saint Pierre and Miquelon)",
        "+509 (Haiti)",
        "+590 (Guadeloupe)",
        "+591 (Bolivia)",
        "+592 (Guyana)",
        "+593 (Ecuador)",
        "+594 (French Guiana)",
        "+595 (Paraguay)",
        "+596 (Martinique)",
        "+597 (Suriname)",
        "+598 (Uruguay)",
        "+599 (Netherlands Antilles)",
        "+670 (East Timor)",
        "+672 (Norfolk Island)",
        "+673 (Brunei)",
        "+674 (Nauru)",
        "+675 (Papua New Guinea)",
        "+676 (Tonga)",
        "+677 (Solomon Islands)",
        "+678 (Vanuatu)",
        "+679 (Fiji)",
        "+680 (Palau)",
        "+681 (Wallis and Futuna)",
        "+682 (Cook Islands)",
        "+683 (Niue)",
        "+685 (Samoa)",
        "+686 (Kiribati)",
        "+687 (New Caledonia)",
        "+688 (Tuvalu)",
        "+689 (French Polynesia)",
        "+690 (Tokelau)",
        "+691 (Micronesia)",
        "+692 (Marshall Islands)",
        "+850 (North Korea)",
        "+852 (Hong Kong)",
        "+853 (Macau)",
        "+855 (Cambodia)",
        "+856 (Laos)",
        "+880 (Bangladesh)",
        "+960 (Maldives)",
        "+961 (Lebanon)",
        "+962 (Jordan)",
        "+963 (Syria)",
        "+964 (Iraq)",
        "+965 (Kuwait)",
        "+966 (Saudi Arabia)",
        "+967 (Yemen)",
        "+968 (Oman)",
        "+970 (Palestine)",
        "+971 (United Arab Emirates)",
        "+972 (Israel)",
        "+973 (Bahrain)",
        "+974 (Qatar)",
        "+975 (Bhutan)",
        "+976 (Mongolia)",
        "+977 (Nepal)",
        "+992 (Tajikistan)",
        "+993 (Turkmenistan)",
        "+994 (Azerbaijan)",
        "+995 (Georgia)",
        "+996 (Kyrgyzstan)",
        "+998 (Uzbekistan)",
        "+1242 (Bahamas)",
        "+1246 (Barbados)",
        "+1264 (Anguilla)",
        "+1268 (Antigua and Barbuda)",
        "+1284 (British Virgin Islands)",
        "+1340 (US Virgin Islands)",
        "+1345 (Cayman Islands)",
        "+1441 (Bermuda)",
        "+1473 (Grenada)",
        "+1649 (Turks and Caicos Islands)",
        "+1664 (Montserrat)",
        "+1670 (Northern Mariana Islands)",
        "+1671 (Guam)",
        "+1684 (American Samoa)",
        "+1721 (Sint Maarten)",
        "+1758 (Saint Lucia)",
        "+1767 (Dominica)",
    )

    val sortedCountryCodes = countryCodes.sortedBy { countryCode ->
        val countryName = countryCode.substringAfter("(").substringBefore(")")
        countryName
    }

    val formattedCountryCodes = sortedCountryCodes.map { formatCountryCode(it) }

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
        number = text.removePrefix(getCountryCodeNumber()).trim()
    }

    fun onCountryCodeSelected(text: String) {
        countryCode = text
    }

    fun getCountryCodeNumber(): String {
        return countryCode.split(" ")[1].replace("(", "").replace(")", "")
    }

    fun addressChange(text: String) {
        address = text
    }

    fun requirementChange(text: String) {
        requirement = text
    }

    fun onDateTimeSelect(time: Calendar, context: Context) {
        dateTime = time
        val currentDate = Calendar.getInstance()
        if (!time.before(currentDate)) {
            dateTimeValue = dateTimeFormat.format(time.time)
        }
        else{
            val toast = "Date/Time cannot be set to a date earlier than the current one"
            val centeredText = SpannableString(toast)
            centeredText.setSpan(
                AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, toast.length - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            Toast.makeText(context, centeredText, Toast.LENGTH_SHORT).show()
        }
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

    fun getCurrentUserRole(activity: Activity): String {
        val sharedPreferences = activity.getSharedPreferences(R.string.app_name.toString(), Context.MODE_PRIVATE)
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

    fun uploadLead(context: Context, activity: Activity) {
        showButtonLoader = true
        if (capturedBitmap != null) {
            imageUpload(context, activity)
        }

        if (selectedDocumentUri != null && capturedBitmap == null) {
            documentUpload(context, activity)
        }

        if (selectedDocumentUri == null && capturedBitmap == null) {
            leadDataUpload(context, activity)
        }
    }


    fun imageUpload(context: Context, activity: Activity) {
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
                    documentUpload(context, activity)
                } else {
                    leadDataUpload(context, activity)
                }

            }.addOnFailureListener { e ->
                Log.d("TAG169", e.toString())
            }
        }

    }

    fun documentUpload(context: Context, activity: Activity) {
        val documentReference =
            storageReference.child("lead_documents/${selectedDocumentUri!!.lastPathSegment}")

        documentReference.putFile(selectedDocumentUri!!).continueWithTask { documentTask ->
            if (!documentTask.isSuccessful) {
                documentTask.exception?.let { throw it }
            }
            return@continueWithTask documentReference.downloadUrl

        }.addOnCompleteListener { documentTask ->
            documentUrl = documentTask.result.toString()

            leadDataUpload(context, activity)
        }.addOnFailureListener { e ->
            Log.d("TAG180", e.toString())
        }

    }

    fun leadDataUpload(context: Context, activity: Activity) {
        val lead = hashMapOf(
            "name" to name,
            "role" to role,
            "number" to "${getCountryCodeNumber()} $number",
            "organization" to organization,
            "email" to email,
            "requirement" to requirement,
            "address" to address,
            "dateTimeValue" to dateTimeValue,
            "status" to "Opened",
            "createdBy" to getCurrentUserRole(activity),
            "documentUrl" to documentUrl,
            "documentName" to selectedDocumentName,
            "documentType" to selectedDocumentMimeType,
            "imageUrl" to imageUrl
        )

        firebaseFirestore.collection("leads").add(lead).addOnSuccessListener {
            val leadId = mapOf("id" to it.id)
            firebaseFirestore.collection("leads").document(it.id).update(leadId).addOnSuccessListener {
                Toast.makeText(context, "Lead Added Successfully", Toast.LENGTH_SHORT).show()
                activity.startActivity(Intent(activity, Home::class.java))
            }
        }.addOnFailureListener { e ->
            Log.d("TAG225", e.toString())
        }
    }

    fun validation() {
        validate =
            !(name == "" || organization == "" || role == "" || number == "" || email == "" || address == "" || requirement == "" || dateTimeValue == "")
    }

    fun formatCountryCode(countryCode: String): String {
        val countryName = countryCode.substringAfter("(").substringBefore(")")
        val code = countryCode.substringBefore(" (")
        return "$countryName ($code)"
    }
}