package com.example.massigym_android.ui.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.massigym_android.databinding.FragmentWorkoutDetailsBinding

class WorkoutDetailsFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutDetailsBinding

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentWorkoutDetailsBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()
        return binding.root
    }


    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarWorkoutDetails
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }


}