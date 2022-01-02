package com.example.massigym_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.massigym_android.databinding.FragmentPersonalWorkoutBinding

class PersonalWorkoutFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding: FragmentPersonalWorkoutBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_personal_workout, container, false)


        return binding.root
    }
}