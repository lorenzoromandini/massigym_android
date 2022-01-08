package com.example.massigym_android.ui.workout

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.PrefUtil
import com.example.massigym_android.databinding.ActivityWorkoutTimerBinding
import java.sql.Time


class WorkoutTimer : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutTimerBinding

    private var duration: Int? = null

    private lateinit var workoutName: String

    enum class TimerState { stopped, paused, running }

    private lateinit var timer: CountDownTimer
    private var timerLengthSeconds = 0L
    private var timerState = TimerState.stopped
    private var secondsRemaining = 0L

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
        } else if (timerState == TimerState.paused) {

        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, this)
        PrefUtil.setSecondsRemaining(secondsRemaining, this)
        PrefUtil.setTimerState(timerState, this)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(this)

        if (timerState == TimerState.stopped) {
            setNewTimerLength()
        } else {
            setPreviousTimerLength()
        }

        if (timerState == TimerState.running || timerState == TimerState.paused) {
            secondsRemaining = PrefUtil.getSecondsRemaining(this)
        } else {
            secondsRemaining = timerLengthSeconds
        }

        if (timerState == TimerState.running) {
            startTimer()
        }

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.stopped

        setNewTimerLength()

        binding.timerView.progressBar.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, this)
        secondsRemaining = timerLengthSeconds

        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000

                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(this)
        timerLengthSeconds = (lengthInMinutes * 60L)
        binding.timerView.progressBar.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(this)
        binding.timerView.progressBar.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        binding.timerView.countdownTextView.text = "$minutesUntilFinished : " +
                "${
                    if (secondsStr.length == 2) secondsStr
                    else "0" + secondsStr
                }"
        binding.timerView.progressBar.progress = (timerLengthSeconds - secondsRemaining).toInt()
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
