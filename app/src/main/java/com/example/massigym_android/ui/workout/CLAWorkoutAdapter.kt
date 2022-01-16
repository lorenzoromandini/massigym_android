package com.example.massigym_android.ui.workout

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.massigym_android.R
import com.example.massigym_android.model.Workout

// classe utilizzata per impostare la recycler view degli allenamenti cardio, legs e arms e gli elementi al loro interno
class CLAWorkoutAdapter(private var context: Context, private var workoutList: List<Workout>) :
    RecyclerView.Adapter<CLAWorkoutAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cla_workout_view_item, parent, false)
        )
    }

    // metodo che serve a visualizzare l'etichetta di ogni allenamento presente nella lista
    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        var workout = workoutList[position]
        holder.workoutName.text = workout.name
        holder.workoutDuration.text = "${workout.duration.toString()} s"

        // verifica se l'utente ha caricato un'immagine per l'allenamento e setta l'immagine presente
        // nella schermata di conseguenza
        if (workout.imageUrl == "") {
            // se l'utente non ha inserito un'immagine viene mostrata un'immagine di default
            holder.workoutImage.setImageResource(R.drawable.workout_image_empty)
        } else {
            // se l'utente ha inserito un'immagine viene ottenuto l'url da Firestore
            // e mostrata nella vista tramite l'utilizzo della libreria Glide
            Glide.with(context)
                .load(workout.imageUrl)
                .into(holder.workoutImage)
        }

        holder.workoutLikes.text = workout.totalLikes.toString()
    }

    // metodo che fornisce il numero di elementi della lista
    override fun getItemCount(): Int {
        return workoutList.size
    }

    // classe che serve a prendere gli elementi presenti nelle etichette di ciascun allenamento
    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var workoutName: TextView = itemView.findViewById(R.id.workout_name)
        var workoutImage: ImageView = itemView.findViewById(R.id.workout_image)
        var workoutDuration: TextView = itemView.findViewById(R.id.workout_time)
        var workoutLikes: TextView = itemView.findViewById(R.id.workout_likes)

    }

}