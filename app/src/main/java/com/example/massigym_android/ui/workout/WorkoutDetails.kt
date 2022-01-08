package com.example.massigym_android.ui.workout

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityWorkoutDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class WorkoutDetails : AppCompatActivity() {

    private lateinit var binding: ActivityWorkoutDetailsBinding

    private var id: String? = null

    private lateinit var auth: FirebaseUser

    private var checkFav = false

    private var checkL = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkoutDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        id = intent.getStringExtra("id")

        binding.toolbarWorkoutDetails.setNavigationOnClickListener { onBackPressed() }

        binding.deleteWorkoutButton.setVisibility(View.GONE)

        auth = FirebaseAuth.getInstance().currentUser!!

        FirebaseFirestore.getInstance().collection("workouts").document(id!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val workout = task.result
                    binding.toolbarWorkoutDetails.setTitle(workout["name"].toString())

                    checkImage(workout)

                    binding.workoutDescription.setText(workout["description"].toString())
                    binding.workoutAuthor.setText("- ${workout["userName"].toString()}")

                    checkFavourite(workout)

                    if (checkFavourite(workout)) {
                        binding.toolbarWorkoutDetails.menu.getItem(0)
                            .setIcon(R.drawable.ic_favourite)
                        binding.toolbarWorkoutDetails.menu.getItem(0).setOnMenuItemClickListener {
                            removeFavourite()
                            onBackPressed()
                            true
                        }
                    } else {
                        binding.toolbarWorkoutDetails.menu.getItem(0)
                            .setIcon(R.drawable.ic_empty_favourite)
                        binding.toolbarWorkoutDetails.menu.getItem(0).setOnMenuItemClickListener {
                            addFavourite()
                            onBackPressed()
                            true
                        }
                    }


                    binding.timerButton.setOnClickListener {
                        val intent = Intent(this, WorkoutTimer::class.java)
                        val duration = workout["duration"].toString()
                        intent.putExtra("duration", duration)
                        intent.putExtra("name", workout["name"].toString())
                        startActivity(intent)
                    }


                    checkLike(workout)

                    if (checkLike(workout)) {
                        binding.likeButton.setImageResource(R.drawable.ic_thumb_down)
                        binding.likeButton.setOnClickListener {
                            removeLike()
                            onBackPressed()
                            true
                        }
                    } else {
                        binding.likeButton.setImageResource(R.drawable.ic_thumb_up_green)
                        binding.likeButton.setOnClickListener {
                            addLike()
                            onBackPressed()
                            true
                        }
                    }

                    checkVideo(workout)

                    if (workout["imageUrl"] != "") {
                        binding.workoutDetailsVideo.setOnClickListener {
                            val intent = Intent(this, WorkoutVideo::class.java)
                            intent.putExtra("videoUrl", workout["videoUrl"].toString())
                            intent.putExtra("name", workout["name"].toString())
                            startActivity(intent)
                        }
                    }

                    if (workout["userMail"] == auth.email || workout["userMail"] == "admin@gmail.com") {
                        binding.deleteWorkoutButton.setVisibility(View.VISIBLE)
                        binding.deleteWorkoutButton.setOnClickListener {
                            deleteWorkout(workout)
                            onBackPressed()
                        }
                    } else {
                        binding.deleteWorkoutButton.setVisibility(View.GONE)
                    }
                }
            }
    }


    private fun checkFavourite(workout: DocumentSnapshot): Boolean {
        for (favourite in workout["favourites"] as ArrayList<String>) {
            if (favourite == auth.email) {
                checkFav = true;
            }
        }
        return checkFav;
    }

    private fun addFavourite() {
        checkFav = true;

        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("favourites", FieldValue.arrayUnion(auth.email.toString()))

        Toast.makeText(this, "Aggiunto ai Preferiti", Toast.LENGTH_SHORT).show()
    }

    private fun removeFavourite() {
        checkFav = false;

        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("favourites", FieldValue.arrayRemove(auth.email.toString()));

        Toast.makeText(this, "Rimosso dai Preferiti", Toast.LENGTH_SHORT).show()
    }

    private fun checkLike(workout: DocumentSnapshot): Boolean {
        for (like in workout["likes"] as ArrayList<String>) {
            if (like == auth.email) {
                checkL = true;
            }
        }
        return checkL;
    }

    private fun addLike() {
        checkL = true;

        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("likes", FieldValue.arrayUnion(auth.email.toString()))

        Toast.makeText(this, "Mi Piace aggiunto", Toast.LENGTH_SHORT).show()
    }

    private fun removeLike() {
        checkL = false;

        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("likes", FieldValue.arrayRemove(auth.email.toString()))

        Toast.makeText(this, "Mi Piace rimosso", Toast.LENGTH_SHORT).show()
    }

    private fun checkImage(workout: DocumentSnapshot) {
        if (workout["imageUrl"] == "") {
            binding.workoutDetailsImage.setImageResource(R.drawable.workout_image_empty)
        } else {
            Glide.with(this)
                .load(workout["imageUrl"].toString())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding.workoutDetailsImage)
        }
    }

    private fun checkVideo(workout: DocumentSnapshot) {
        if (workout["videoUrl"] == "") {
            binding.workoutDetailsVideo.setImageResource(R.drawable.workout_video_empty)
        } else {
            binding.workoutDetailsVideo.setImageResource(R.drawable.workout_video_exists)
        }
    }

    private fun deleteWorkout(workout: DocumentSnapshot) {
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .delete()

        Toast.makeText(this, "Workout eliminato", Toast.LENGTH_LONG).show()
    }


}