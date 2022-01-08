package com.example.massigym_android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.massigym_android.model.Workout

class WorkoutAdapter(private var context: Context, private var workoutList: List<Workout>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.workout_view_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        var workout = workoutList[position]
        holder.workoutName.text = workout.name
        holder.workoutDuration.text = workout.duration.toString()

        if (workout.imageUrl == "") {
            holder.workoutImage.setImageResource(R.drawable.workout_image_empty)
        } else {
            Glide.with(context)
                .load(workout.imageUrl)
                .into(holder.workoutImage)
        }
    }

    override fun getItemCount(): Int {
        return workoutList.size
    }

    class WorkoutViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var workoutName: TextView = itemView.findViewById(R.id.workout_name)
        var workoutImage: ImageView = itemView.findViewById(R.id.workout_image)
        var workoutDuration: TextView = itemView.findViewById(R.id.workout_time)

        }

}