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
import com.example.massigym_android.databinding.FragmentLegsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

// classe che gestisce la lista degli allenamenti della categoria "legs"
class LegsFragment : Fragment() {

    private lateinit var binding: FragmentLegsBinding

    private var workoutIDList = ArrayList<String>()

    var id: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentLegsBinding.inflate(inflater, container, false)

        // il layoutManager è responsabile della misurazione e del posizionamento delle viste degli elementi
        // all'interno della RecyclerView
        binding.recyclerLegs.apply {
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
        binding.recyclerLegs.addOnItemClickListener(object : OnItemClickListener {
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

    // metodo che serve ad ottenere i workout della categoria "legs" contenuti all'interno del database Cloud Firestore
    // ordinati per numero di likes decrescente e per ordine alfabetico
    private fun getListData() {
        FirebaseFirestore.getInstance().collection("workouts")
            .whereEqualTo("category", "legs")
            .orderBy("totalLikes", Query.Direction.DESCENDING)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { documents ->
                // se esistono allenamenti vengono collegati alla RecyclerView tramite l'adapter
                // definito in un'altra classe
                for (document in documents) {
                    val workout = documents.toObjects(Workout::class.java)
                    binding.recyclerLegs.adapter = CLAWorkoutAdapter(requireContext(), workout)
                    val id = document.id
                    workoutIDList.add(id)
                }

            }.addOnFailureListener {
                Toast.makeText(context,
                    "An error occurred: ${it.localizedMessage}",
                    Toast.LENGTH_SHORT).show()
            }
    }

    // metodo che gestisce la barra di ricerca tramite form di inserimento e bottone per effettuare la
    // ricerca all'interno del database
    private fun search() {
        Toast.makeText(context,
            "Ricerca...",
            Toast.LENGTH_SHORT).show()
        val insertName = binding.searchName.text.toString()
        // se il campo è vuoto
        if (insertName == "") {
            // restituisce tutti gli allenamenti della relativa categoria
            getListData()
            // se il campo non è vuoto
        } else {
            // ottiene i workout della categoria "legs" che nel campo "searchKeywords" contengono una stringa
            // uguale a quella inserita dall'utente nella form,
            // ordinati per numero di likes decrescente e per ordine alfabetico
            FirebaseFirestore.getInstance().collection("workouts")
                .whereEqualTo("category", "legs")
                .whereArrayContains("searchKeywords", insertName)
                .orderBy("totalLikes", Query.Direction.DESCENDING)
                .orderBy("name", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener { documents ->
                    // se esistono allenamenti vengono collegati alla RecyclerView tramite l'adapter
                    // definito in un'altra classe
                    for (document in documents) {
                        val workout = documents.toObjects(Workout::class.java)
                        binding.recyclerLegs.adapter =
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


}