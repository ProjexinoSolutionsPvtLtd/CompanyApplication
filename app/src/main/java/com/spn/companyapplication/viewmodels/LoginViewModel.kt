package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import android.text.Layout
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AlignmentSpan
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.spn.companyapplication.R
import com.spn.companyapplication.screens.Home

class LoginViewModel(): ViewModel(){

    var username by mutableStateOf("")
    var password by mutableStateOf("")

    var validate by mutableStateOf(false)

    var showLoader by mutableStateOf(false)

    fun usernameChange(text: String){
        username = text
    }

    fun passwordChange(text: String){
        password = text
    }

    fun validation() {
        validate =
            !(username == "" || password == "")
    }

    fun loginUser(activity: Activity){
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseFirestore = FirebaseFirestore.getInstance()

        val sharedPreferences = activity.getSharedPreferences(R.string.app_name.toString(), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        showLoader = true
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener {
            val userId = firebaseAuth.currentUser?.uid.toString()

            firebaseFirestore.collection("users").document(userId).get().addOnSuccessListener {
                editor.putString("CurrentUserRole", it.get("role").toString())
                editor.apply()
            }.addOnFailureListener { e ->
                Log.d("TAG41", e.toString())
            }

            activity.startActivity(Intent(activity, Home::class.java))
            activity.finish()

        }.addOnFailureListener{e ->
            showLoader = false
            Log.d("TAG43", e.toString())
            Toast.makeText(activity, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}