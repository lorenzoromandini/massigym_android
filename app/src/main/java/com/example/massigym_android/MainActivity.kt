package com.example.massigym_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.massigym_android.databinding.ActivityMainBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.example.massigym_android.ui.common.BottomNavBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var user: FirebaseUser? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        user = FirebaseAuth.getInstance().currentUser
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        if (user != null) {
            val intent = Intent(this, BottomNavBar::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}