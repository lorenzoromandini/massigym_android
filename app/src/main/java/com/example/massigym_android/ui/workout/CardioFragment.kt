package com.example.massigym_android.ui.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.massigym_android.Workout
import com.example.massigym_android.WorkoutAdapter
import com.example.massigym_android.databinding.FragmentCardioBinding
import com.google.firebase.firestore.FirebaseFirestore

class CardioFragment : Fragment() {

    private lateinit var binding: FragmentCardioBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentCardioBinding.inflate(inflater, container, false)

        binding.recyclerCardio.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        getData()
        return binding.root
    }


    private fun getData() {
        FirebaseFirestore.getInstance().collection("workouts").whereEqualTo("category", "cardio").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val workout = documents.toObjects(Workout::class.java)
                    binding.recyclerCardio.adapter = WorkoutAdapter(requireContext(), workout)
                }

            }.addOnFailureListener {
                Toast.makeText(context, "An error occurred: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }


}