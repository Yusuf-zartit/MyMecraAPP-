package com.yusuf.mymecraapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_kayit_ol_page.*

class KayitOlPage : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kayit_ol_page)
        auth = FirebaseAuth.getInstance()

    }


    fun kayitYap(view: View) {
        if (kayitPassword1.text.toString().equals(kayitPassword2.text.toString())) {
            val name = kayitName.text.toString()
            val email = kayitEmail.text.toString()
            val pass = kayitPassword1.text.toString()

            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        val intent = Intent(this, AnaSayfa::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG
                    ).show()
                }
        }
    }
}