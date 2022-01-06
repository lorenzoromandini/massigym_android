package com.example.massigym_android.ui.workout

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.databinding.ActivityWorkoutVideoBinding


class WorkoutVideo : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutVideoBinding

    private var videoUrl: String? = null

    private lateinit var workoutName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutVideoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        videoUrl = intent.getStringExtra("videoUrl")
        workoutName = intent.getStringExtra("name")!!

        binding.toolbarWorkoutVideo.setNavigationOnClickListener { onBackPressed() }

        binding.toolbarWorkoutVideo.setTitle("$workoutName - Video")
    }

}