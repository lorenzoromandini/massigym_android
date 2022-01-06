package com.example.massigym_android.ui.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.massigym_android.databinding.FragmentWorkoutTimerBinding


class WorkoutTimerFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutTimerBinding

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentWorkoutTimerBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        return binding.root
    }

    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarWorkoutTimer
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

}