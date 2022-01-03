package com.example.massigym_android.ui.personale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.massigym_android.R
import com.example.massigym_android.databinding.FragmentPersonaleBinding

class PersonaleFragment : Fragment() {

    private lateinit var binding: FragmentPersonaleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentPersonaleBinding.inflate(inflater, container, false)

        binding.personalWorkoutButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.from_personale_to_personalWorkout)
        }

        binding.preferitiButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.from_personale_to_preferiti)
        }

        binding.profiloButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.from_personale_to_profilo)
        }

        return binding.root
    }

}