package com.example.massigym_android

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.massigym_android.databinding.WorkoutViewItemBinding
import com.example.massigym_android.ui.workout.CardioFragment
import com.example.massigym_android.ui.workout.WorkoutDetailsFragment

class WorkoutAdapter(private val context: Context, private val workoutList: List<Workout>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        return WorkoutViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.workout_view_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val workout = workoutList[position]
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

        val workoutName: TextView = itemView.findViewById(R.id.workout_name)
        val workoutImage: ImageView = itemView.findViewById(R.id.workout_image)
        val workoutDuration: TextView = itemView.findViewById(R.id.workout_time)
        }

}