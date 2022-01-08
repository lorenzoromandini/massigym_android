package com.example.massigym_android.ui.workout

import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.databinding.ActivityWorkoutTimerBinding


class WorkoutTimer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutTimerBinding

    private var duration: Int? = null

    private lateinit var workoutName: String

    enum class TimerState { stopped, paused, running }

    private lateinit var timer: CountDownTimer
    private var timerState = TimerState.stopped
    private var secondsRemaining = 0

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
            onTimerFinished()
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
        }
    }

    private fun initTimer() {
        if (timerState == TimerState.stopped || timerState == TimerState.paused) {
            secondsRemaining = duration!!
        }

        if (timerState == TimerState.running) {
            startTimer()
        }

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.stopped

        binding.timerView.progressBar.max = duration!!

        binding.timerView.progressBar.progress = 0

        secondsRemaining = duration!!

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.running

        timer = object : CountDownTimer((secondsRemaining * 1000).toLong(), 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = (millisUntilFinished / 1000).toInt()

                updateCountdownUI()
            }
        }.start()
    }

    private fun updateCountdownUI() {
        binding.timerView.countdownTextView.text = "$secondsRemaining"
        binding.timerView.progressBar.progress = duration!! - secondsRemaining
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.running -> {
                binding.playButton.isEnabled = false
                binding.stopButton.isEnabled = true
                binding.resetButton.isEnabled = true
            }
            TimerState.paused -> {
                binding.playButton.isEnabled = true
                binding.stopButton.isEnabled = false
                binding.resetButton.isEnabled = true
            }
            TimerState.stopped -> {
                binding.playButton.isEnabled = true
                binding.stopButton.isEnabled = false
                binding.resetButton.isEnabled = false
            }

        }
    }

}
