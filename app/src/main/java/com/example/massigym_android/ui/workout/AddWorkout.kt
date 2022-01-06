package com.example.massigym_android.ui.workout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.databinding.ActivityAddWorkoutBinding

class AddWorkout : AppCompatActivity() {

    private lateinit var binding: ActivityAddWorkoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarAddWorkout.setNavigationOnClickListener { onBackPressed() }

    }

}