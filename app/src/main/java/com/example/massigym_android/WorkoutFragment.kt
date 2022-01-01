package com.example.massigym_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.massigym_android.databinding.FragmentWorkoutBinding

class WorkoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentWorkoutBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_workout, container, false)


        return binding.root
    }

}