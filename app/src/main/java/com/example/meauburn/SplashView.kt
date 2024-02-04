package com.example.meauburn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_view)
        Handler().postDelayed({
            startActivity(Intent(this@SplashView, LoginPhoneNum::class.java))
            finish()
        }, 3000)
        }


    }
