package com.yusuf.mymecraapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler


class StartUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)

        Handler().postDelayed({
            val transition = Intent (applicationContext,MainActivity::class.java)
            startActivity(transition)
            finish()
        }, 3000)
    }
}