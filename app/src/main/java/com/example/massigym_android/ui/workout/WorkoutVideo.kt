package com.example.massigym_android.ui.workout

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.MediaController
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

        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.workoutVideoView)
        val uri = Uri.parse(videoUrl)
        binding.workoutVideoView.setMediaController(mediaController)
        binding.workoutVideoView.setVideoURI(uri)
        binding.workoutVideoView.requestFocus()
        binding.workoutVideoView.start()
    }

}