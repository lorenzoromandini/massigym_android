package com.example.massigym_android.ui.workout

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.massigym_android.model.Workout
import com.example.massigym_android.databinding.FragmentArmsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ArmsFragment : Fragment() {

    private lateinit var binding: FragmentArmsBinding

    private var workoutIDList = ArrayList<String>()

    var id: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentArmsBinding.inflate(inflater, container, false)

        binding.recyclerArms.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }

        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                getListData()
            },
            2000
        )

        binding.recyclerArms.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                id = workoutIDList[position]
                val intent = Intent(context, WorkoutDetails::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        })

        binding.searchButton.setOnClickListener {
            search()
        }

        return binding.root
    }


    private fun getListData() {
        FirebaseFirestore.getInstance().collection("workouts")
            .whereEqualTo("category", "arms")
            .orderBy("totalLikes", Query.Direction.DESCENDING)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val workout = documents.toObjects(Workout::class.java)
                    binding.recyclerArms.adapter = CLAWorkoutAdapter(requireContext(), workout)
                    val id = document.id
                    workoutIDList.add(id)
                }

            }.addOnFailureListener {
                Toast.makeText(context,
                    "An error occurred: ${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun search() {
        Toast.makeText(context,
            "Ricerca...",
            Toast.LENGTH_SHORT).show()
        val insertName = binding.searchName.text.toString()
        if (insertName == "") {
            getListData()
        } else {
            FirebaseFirestore.getInstance().collection("workouts")
                .whereEqualTo("category", "arms")
                .whereArrayContains("searchKeywords", insertName)
                .orderBy("totalLikes", Query.Direction.DESCENDING)
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val workout = documents.toObjects(Workout::class.java)
                        binding.recyclerArms.adapter =
                            CLAWorkoutAdapter(requireContext(), workout)
                        val id = document.id
                        workoutIDList.add(id)
                    }

                }.addOnFailureListener {
                    Toast.makeText(context,
                        "An error occurred: ${it.localizedMessage}",
                        Toast.LENGTH_SHORT).show()
                }
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


}