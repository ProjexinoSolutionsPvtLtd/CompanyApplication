package com.spn.companyapplication.services

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun sendEmail(
    to: String,
    from: String,
    subject: String,
    content: String,
    context: Context,
    onSuccess: () -> Unit
) {
    // Create a CoroutineScope using the IO dispatcher to perform IO operations to prevent UI blocking ANRs
    CoroutineScope(Dispatchers.IO).launch {
        // SMTP server details
        val host = "smtp.hostinger.com"
        val port = 587
        val username = "pranav@projexino.tech"
        val password = "Projexino@1234"

        val to = to

        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = host
        props["mail.smtp.port"] = port

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        try {
            // Create a new MimeMessage
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(username))
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            message.subject = subject

            message.setContent(content, "text/html; charset=utf-8")
            Transport.send(message)

            CoroutineScope(Dispatchers.Main).launch {
                // Display a success toast message
//                Toast.makeText(context, "Sent Successfully.", Toast.LENGTH_LONG).show()

                // Invoke the onSuccess callback function
                onSuccess.invoke()
            }
        } catch (e: MessagingException) {
            e.printStackTrace()

            // Perform UI operations on the
            //
            //
            //
            // Main dispatcher
            CoroutineScope(Dispatchers.Main).launch {
                // Display an error toast message
                Log.d("LOGs76", "$e")
                Toast.makeText(context, "Problem exists: $e", Toast.LENGTH_LONG).show()
            }
        }
    }
}