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

//classe che mostra i dettagli dell'allenamento selezionato all'interno della RecyclerView
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

                    // se l'utente ha inserito l'allenamento tra i preferiti, l'icona nella TabBar avrà colore
                    // rosso e ad un successivo click verrà rimossa dai preferiti
                    if (checkFavourite(workout)) {
                        binding.toolbarWorkoutDetails.menu.getItem(0)
                            .setIcon(R.drawable.ic_favourite)
                        binding.toolbarWorkoutDetails.menu.getItem(0).setOnMenuItemClickListener {
                            removeFavourite()
                            onBackPressed()
                            true
                        }
                        // se l'utente non ha inserito l'allenamento tra i preferiti, l'icona nella TabBar avrà colore
                        // bianco e ad un successivo click verrà aggiunto ai preferiti
                    } else {
                        binding.toolbarWorkoutDetails.menu.getItem(0)
                            .setIcon(R.drawable.ic_empty_favourite)
                        binding.toolbarWorkoutDetails.menu.getItem(0).setOnMenuItemClickListener {
                            addFavourite()
                            onBackPressed()
                            true
                        }
                    }

                    // metodo che al click sul bottone reindirizza l'utente alla schermata del timer
                    // di quell'allenamento, passando come parametro alla classe WorkoutTimer
                    // la durata e il nome dell'allenamento
                    binding.timerButton.setOnClickListener {
                        val intent = Intent(this, WorkoutTimer::class.java)
                        val duration = workout["duration"].toString()
                        intent.putExtra("duration", duration)
                        intent.putExtra("name", workout["name"].toString())
                        startActivity(intent)
                    }


                    // se l'utente ha messo like all'allenamento, l'icona avrà colore
                    // rosso e ad un successivo click il like verrà rimosso
                    if (checkLike(workout)) {
                        binding.likeButton.setImageResource(R.drawable.ic_thumb_down)
                        binding.likeButton.setOnClickListener {
                            removeLike(workout)
                            onBackPressed()
                            true
                        }
                        // se l'utente non ha messo like all'allenamento, l'icona avrà colore
                        // verde e ad un successivo click il like verrà aggiunto
                    } else {
                        binding.likeButton.setImageResource(R.drawable.ic_thumb_up_green)
                        binding.likeButton.setOnClickListener {
                            addLike(workout)
                            onBackPressed()
                            true
                        }
                    }

                    // verifica se l'utente ha inserito il video e setta l'immagine da visualizzare di conseguenza
                    checkVideo(workout)

                    // se l'utente ha inserito il video dell'allenamento, l'immagine sarà clickabile e
                    // l'utente verrà reindirizzato alla schermata del video di quell'allenamento,
                    // passando come parametro alla classe WorkoutVideo l'url del video e il nome dell'allenamento
                    if (workout["videoUrl"] != "") {
                        binding.workoutDetailsVideo.setOnClickListener {
                            val intent = Intent(this, WorkoutVideo::class.java)
                            intent.putExtra("videoUrl", workout["videoUrl"].toString())
                            intent.putExtra("name", workout["name"].toString())
                            startActivity(intent)
                        }
                    }

                    // se l'utente che ha creato il video è l'utente autenticato oppure ha come email
                    // "admin@gmail.com", verrà mostrato un bottono con cui l'utente potrà eliminare l'allenamento
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


    // verifica all'interno del db se l'utente ha inserito l'email tra i Preferiti
    private fun checkFavourite(workout: DocumentSnapshot): Boolean {
        for (favourite in workout["favourites"] as ArrayList<String>) {
            if (favourite == auth.email) {
                checkFav = true
            }
        }
        return checkFav
    }

    // metodo che serve a inserire l'allenamento tra i Preferiti
    private fun addFavourite() {
        checkFav = true

        // inserisce in coda all'array "favourites" l'email dell'utente
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("favourites", FieldValue.arrayUnion(auth.email.toString()))

        Toast.makeText(this, getString(R.string.addToFavourites), Toast.LENGTH_SHORT).show()
    }

    // metodo che serve a rimuovere il workout dai preferiti dell'utente
    private fun removeFavourite() {
        checkFav = false

        // rimuove dall'array "favourites" l'email dell'utente
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("favourites", FieldValue.arrayRemove(auth.email.toString()))

        Toast.makeText(this, getString(R.string.removeFromFavourites), Toast.LENGTH_SHORT).show()
    }

    // verifica all'interno del db se l'utente ha messo Mi Piace
    private fun checkLike(workout: DocumentSnapshot): Boolean {
        for (like in workout["likes"] as ArrayList<String>) {
            if (like == auth.email) {
                checkL = true
            }
        }
        return checkL
    }

    // metodo che serve a inserire Mi Piace
    private fun addLike(workout: DocumentSnapshot) {
        checkL = true

        // inserisce in coda all'array "likes" l'email dell'utente
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("likes", FieldValue.arrayUnion(auth.email.toString()))

        // incrementa di 1 il numero di likes di quell'allenamento
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("totalLikes", FieldValue.increment(1))

        // incrementa di 1 il numero di like in quella categoria nel campo delle statistiche
        FirebaseFirestore.getInstance()
            .collection("statistics")
            .document(workout["category"].toString())
            .update("totalLikes", FieldValue.increment(1))

        Toast.makeText(this, getString(R.string.addLike), Toast.LENGTH_SHORT).show()
    }

    // metodo che serve a rimuovere il Mi Piace precedentemente inserito dall'utente
    private fun removeLike(workout: DocumentSnapshot) {
        checkL = false

        // rimuove dall'array "likes" l'email dell'utente
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("likes", FieldValue.arrayRemove(auth.email.toString()))

        // decrementa di 1 il numero di likes di quell'allenamento
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .update("totalLikes", FieldValue.increment(-1))

        // decrementa di 1 il numero di like in quella categoria nel campo delle statistiche
        FirebaseFirestore.getInstance()
            .collection("statistics")
            .document(workout["category"].toString())
            .update("totalLikes", FieldValue.increment(-1))

        Toast.makeText(this, getString(R.string.removeLike), Toast.LENGTH_SHORT).show()
    }

    // metodo che verifica se l'utente ha inserito l'immagine dell'allenamento e setta l'immagine di conseguenza nella vista
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

    // metodo che verifica se l'utente ha inserito il video dell'allenamento e setta l'immagine di conseguenza nella vista
    private fun checkVideo(workout: DocumentSnapshot) {
        if (workout["videoUrl"] == "") {
            binding.workoutDetailsVideo.setImageResource(R.drawable.workout_video_empty)
        } else {
            binding.workoutDetailsVideo.setImageResource(R.drawable.workout_video_exists)
        }
    }

    // metodo per eliminare l'allenamento
    private fun deleteWorkout(workout: DocumentSnapshot) {
        FirebaseFirestore.getInstance()
            .collection("workouts")
            .document(id!!)
            .delete()

        // decrementa di 1 il numero di workouts di quella categoria nel campo delle statistiche
        FirebaseFirestore.getInstance()
            .collection("statistics")
            .document(workout["category"].toString())
            .update("totalWorkouts", FieldValue.increment(-1))

        Toast.makeText(this, getString(R.string.deleteWorkout), Toast.LENGTH_LONG).show()
    }


}