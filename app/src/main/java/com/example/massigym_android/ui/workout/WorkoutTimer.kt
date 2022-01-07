package com.example.massigym_android.ui.workout

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.databinding.ActivityWorkoutTimerBinding
import java.sql.Time


class WorkoutTimer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutTimerBinding

    private var duration: Int? = null

    private lateinit var workoutName: String

    enum class TimerState {stopped, paused, running}

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds: Int = 0
    private var timerState = TimerState.stopped
    private var secondsRemaining: Int = 0

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

        binding.playButton.setOnClickListener {
            startTimer()
            timerState = TimerState.running
            updateButtons()
        }

        binding.stopButton.setOnClickListener {
            timer.cancel()
            timerState = TimerState.paused
            updateButtons()
        }

        binding.resetButton.setOnClickListener {
            timer.cancel()
            onTimerfinished()
        }
    }

    override fun onResume() {
        super.onResume()

        initTimer()
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.running) {
            timer.cancel()
        } else if (timerState == TimerState.paused) {

        }
    }

}