package com.spn.companyapplication.viewmodels

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.spn.companyapplication.screens.Home

class LoginViewModel(): ViewModel(){
    val firebaseAuth = FirebaseAuth.getInstance()

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
        showLoader = true
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener {
            activity.startActivity(Intent(activity, Home::class.java))
            activity.finish()

        }.addOnFailureListener{e ->
            showLoader = false
            Log.d("TAG43", e.toString())
            Toast.makeText(activity, "Something went wrong. Try Again", Toast.LENGTH_SHORT).show()
        }
    }
}