package com.example.massigym_android.ui.workout

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.massigym_android.R
import com.example.massigym_android.databinding.FragmentWorkoutDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class WorkoutDetailsFragment : Fragment() {

    private lateinit var binding: FragmentWorkoutDetailsBinding

    private lateinit var toolbar: Toolbar

    private var id: String? = null

    private lateinit var auth: FirebaseUser

    private var checkFav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = requireArguments().getString("id")
            // id = requireArguments().getString("id");
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentWorkoutDetailsBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        auth = FirebaseAuth.getInstance().currentUser!!

        FirebaseFirestore.getInstance().collection("workouts").document(id!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val workout = task.result
                    checkImage(workout)
                    binding.workoutDescription.setText(workout["description"].toString())
                    binding.workoutAuthor.setText("- ${workout["userName"].toString()}")

                    checkFavourite(workout)
                    if (checkFavourite(workout)) {
                        binding.toolbarWorkoutDetails.menu.getItem(0)
                            .setIcon(R.drawable.ic_favourite)
                        binding.toolbarWorkoutDetails.menu.getItem(0).setOnMenuItemClickListener {
                            removeFavourite()
                            true
                        }
                    } else {
                        binding.toolbarWorkoutDetails.menu.getItem(0)
                            .setIcon(R.drawable.ic_empty_favourite)
                        binding.toolbarWorkoutDetails.menu.getItem(0).setOnMenuItemClickListener {
                            addFavourite()
                            true
                        }
                    }

                    binding.timerButton.setOnClickListener {
                        binding.root.findNavController()
                            .navigate(R.id.from_details_to_timer)
                    }

                    checkVideo(workout)

                    if (workout["imageUrl"] != "") {
                        binding.workoutDetailsVideo.setOnClickListener {
                            binding.root.findNavController()
                                .navigate(R.id.from_details_to_video)
                        }

                        if (workout["userMail"] == auth.email || workout["userMail"] == "admin@gmail.com") {
                            binding.deleteWorkoutButton.setVisibility(View.VISIBLE)
                            binding.deleteWorkoutButton.setOnClickListener {

                            }
                        } else {
                            binding.deleteWorkoutButton.setVisibility(View.GONE)
                        }
                    }
                }
            }


        return binding.root
    }


    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarWorkoutDetails
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    companion object {
        fun newInstance(id: String?): WorkoutDetailsFragment {
            val fragment = WorkoutDetailsFragment()
            val args = Bundle()
            args.putString("id", id)
            fragment.arguments = args
            return fragment
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
            .update("favourites", FieldValue.arrayUnion(auth.email.toString()));
    }

    private fun removeFavourite() {
        checkFav = false;

        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("favourites", FieldValue.arrayRemove(auth.email.toString()));
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


}