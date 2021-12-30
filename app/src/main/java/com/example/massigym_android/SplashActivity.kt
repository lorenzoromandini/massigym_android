package com.example.massigym_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        @Suppress("DEPRECATION")
      Handler().postDelayed(
          {
              // Launch the Main Activity
              startActivity(Intent(this@SplashActivity, MainActivity::class.java))
              finish() // Call this when your activity is done and should be closed
          },
          2000
      )
    }
}