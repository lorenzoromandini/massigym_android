package com.example.massigym_android.ui.workout

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.databinding.ActivityWorkoutTimerBinding


class WorkoutTimer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutTimerBinding

    private var duration: Int? = null

    private lateinit var workoutName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutTimerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val durationString = intent.extras!!.getString("duration")

        duration = durationString!!.toInt()
        workoutName = intent.getStringExtra("name")!!

        binding.toolbarWorkoutTimer.setNavigationOnClickListener { onBackPressed() }

        binding.toolbarWorkoutTimer.setTitle("$workoutName - Timer")

    }

}