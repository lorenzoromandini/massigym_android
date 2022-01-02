package com.example.massigym_android

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.massigym_android.databinding.FragmentPersonaleBinding
import com.google.firebase.auth.FirebaseAuth

class PersonaleFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding: FragmentPersonaleBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_personale, container, false)




        binding.personalWorkoutButton.setOnClickListener {
            binding.root.findNavController().navigate(R.id.from_personale_to_personalWorkout)
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