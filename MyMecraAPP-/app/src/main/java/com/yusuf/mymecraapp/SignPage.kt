package com.yusuf.mymecraapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

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


    }
}