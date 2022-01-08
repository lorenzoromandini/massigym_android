package com.example.massigym_android.ui.personale

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.massigym_android.databinding.FragmentPreferitiBinding
import com.example.massigym_android.model.Workout
import com.example.massigym_android.ui.workout.CLAWorkoutAdapter
import com.example.massigym_android.ui.workout.WorkoutDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class PreferitiFragment : Fragment() {

    private lateinit var binding: FragmentPreferitiBinding

    private lateinit var toolbar: Toolbar

    private var workoutIDList = ArrayList<String>()

    var id: String? = null

    private lateinit var auth: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentPreferitiBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        auth = FirebaseAuth.getInstance().currentUser!!

        binding.recyclerPreferiti.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        getListData()

        binding.recyclerPreferiti.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                id = workoutIDList[position]
                val intent = Intent(context, WorkoutDetails::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        })

        return binding.root
    }


    private fun getListData() {
        FirebaseFirestore.getInstance().collection("workouts")
            .whereArrayContains("favourites", auth.email.toString())
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val workout = documents.toObjects(Workout::class.java)
                    binding.recyclerPreferiti.adapter = CLAWorkoutAdapter(requireContext(), workout)
                    val id = document.id
                    workoutIDList.add(id)
                }

            }.addOnFailureListener {
                Toast.makeText(context,
                    "An error occurred: ${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int, view: View)
    }

    fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
        this.addOnChildAttachStateChangeListener(object :
            RecyclerView.OnChildAttachStateChangeListener {
            override fun onChildViewDetachedFromWindow(view: View) {
                view?.setOnClickListener(null)
            }

            override fun onChildViewAttachedToWindow(view: View) {
                view?.setOnClickListener({
                    val holder = getChildViewHolder(view)
                    onClickListener.onItemClicked(holder.adapterPosition, view)
                })
            }
        })
    }

    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarPreferiti
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}