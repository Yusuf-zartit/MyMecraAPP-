package com.yusuf.mymecraapp

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_kayit_ol_page.*
import kotlinx.android.synthetic.main.activity_sign_page.*

class SignPage : AppCompatActivity() {
    private lateinit var auth :FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_page)
        auth = FirebaseAuth.getInstance()

    }



    fun kayitOl(view: View){
        val kayitOl = Intent (applicationContext,KayitOlPage::class.java)
        startActivity(kayitOl)
    }



    fun girisYap(view: View){
        val email = editTextEmail.text.toString()
        val pass = editTextPassword.text.toString()
        if(email != "" && pass != ""){
            auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val kullanci = auth.currentUser?.email.toString()
                        Toast.makeText(applicationContext,"Hoş Geldin",Toast.LENGTH_LONG).show()
                        val intent = Intent(this, AnaSayfa::class.java)
                        intent.putExtra("email",kullanci)
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