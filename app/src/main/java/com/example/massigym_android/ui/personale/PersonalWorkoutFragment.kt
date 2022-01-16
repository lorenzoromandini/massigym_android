package com.example.massigym_android.ui.personale

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.massigym_android.databinding.FragmentPersonalWorkoutBinding
import com.example.massigym_android.model.Workout
import com.example.massigym_android.ui.workout.WorkoutDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// classe che gestisce la lista degli allenamenti inseriti dall'utente
class PersonalWorkoutFragment : Fragment() {

    private lateinit var binding: FragmentPersonalWorkoutBinding

    private lateinit var toolbar: Toolbar

    private var workoutIDList = ArrayList<String>()

    var id: String? = null

    private lateinit var auth: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentPersonalWorkoutBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        auth = FirebaseAuth.getInstance().currentUser!!

        // il layoutManager è responsabile della misurazione e del posizionamento delle viste degli elementi
        // all'interno della RecyclerView
        binding.recyclerPersonalWorkout.apply {
            layoutManager = LinearLayoutManager(requireContext())
        }


        @Suppress("DEPRECATION")
        // metodo che permette di eseguire i comandi al suo interno dopo un tempo prestabilito
        Handler().postDelayed(
            {
                getListData()
            },
            2000
        )

        // metodo che al click su un elemento della lista reindirizza l'utente alla schermata dei dettagli
        // di quell'allenamento, passando come parametro alla classe WorkouDetails l'id dell'allenamento selezionato
        binding.recyclerPersonalWorkout.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                id = workoutIDList[position]
                val intent = Intent(context, WorkoutDetails::class.java)
                intent.putExtra("id", id)
                startActivity(intent)
            }
        })

        return binding.root
    }

    // metodo che serve ad ottenere i workout inseriti dall'utente contenuti all'interno del database Cloud Firestore
    // ordinati per numero di likes decrescente e per ordine alfabetico
    private fun getListData() {
        FirebaseFirestore.getInstance().collection("workouts")
            .whereEqualTo("userMail", auth.email.toString())
            .orderBy("totalLikes", Query.Direction.DESCENDING)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                // se esistono allenamenti vengono collegati alla RecyclerView tramite l'adapter
                // definito in un'altra classe
                for (document in documents) {
                    val workout = documents.toObjects(Workout::class.java)
                    binding.recyclerPersonalWorkout.adapter =
                        FPWorkoutAdapter(requireContext(), workout)
                    val id = document.id
                    workoutIDList.add(id)
                }

            }.addOnFailureListener {
                Toast.makeText(context,
                    "An error occurred: ${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    // interfaccia che serve a settare il click di un elemento della lista
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

    // metodo che permette di tornare indietro alla schermata precedente premendo l'apposito pulsante sulla AppBar
    // in alto a sinistra, facendo uso del navigation
    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarPersonalWorkout
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}