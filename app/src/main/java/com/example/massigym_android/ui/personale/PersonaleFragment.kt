package com.example.massigym_android.ui.personale

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.massigym_android.R
import com.example.massigym_android.databinding.FragmentPersonaleBinding

// classe che gestisce il rendirizzamento dell'utente all'interno della sua area personale
class PersonaleFragment : Fragment() {

    private lateinit var binding: FragmentPersonaleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentPersonaleBinding.inflate(inflater, container, false)

        // al click dell'apposito bottone l'utente viene reindirizzato alla lista degli allenamenti da lui inseriti
        binding.personalWorkoutButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.from_personale_to_personalWorkout)
        }

        // al click dell'apposito bottone l'utente viene reindirizzato alla lista degli allenamenti che ha inserito tra i preferiti
        binding.preferitiButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.from_personale_to_preferiti)
        }

        // al click dell'apposito bottone l'utente viene reindirizzato alla schermata del proprio profilo
        binding.profiloButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.from_personale_to_profilo)
        }

        return binding.root
    }

}