package com.example.massigym_android.ui.workout

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityAddWorkoutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.HashMap

class AddWorkout : AppCompatActivity() {

    private lateinit var binding: ActivityAddWorkoutBinding

    private lateinit var imagePath: Uri
    private lateinit var videoPath: Uri

    private var imageName: String = ""
    private var videoName: String = ""

    private var IMAGE_CODE = 1
    private var VIDEO_CODE = 2

    private lateinit var auth: FirebaseUser

    private lateinit var username: String

    private lateinit var storageRef: StorageReference

    private var favourites: ArrayList<String> = arrayListOf()
    private var likes: ArrayList<String> = arrayListOf()
    private var searchKeyList: ArrayList<String> = arrayListOf()
    private var splitName: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarAddWorkout.setNavigationOnClickListener { onBackPressed() }

        auth = FirebaseAuth.getInstance().currentUser!!

        FirebaseFirestore.getInstance().collection("users").document(auth.email!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result
                    username = user["username"].toString()
                }
            }

        storageRef = FirebaseStorage.getInstance().reference

        binding.titleSelectImage.setText(getString(R.string.noFileSelected))
        binding.titleSelectVideo.setText(getString(R.string.noFileSelected))

        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        binding.selectVideoButton.setOnClickListener {
            selectVideo()
        }

        binding.addWorkoutButton.setOnClickListener {
            addWorkout()
        }

    }

    private fun addWorkout() {

        val name = binding.workoutName.text.toString().trim()
        val nameInput = binding.nameInput
        val descrizione = binding.workoutDescription.text.toString().trim()
        val descrizioneInput = binding.descrizioneInput

        if (name.isEmpty() || name.length < 2 || descrizione.isEmpty() || descrizione.length < 10) {
            if (name.isEmpty()) {
                nameInput.error = getString(R.string.nameRequired)
            }
            if (name.length < 2) {
                nameInput.error = getString(R.string.nameInvalid)
            }
            if (descrizione.isEmpty()) {
                descrizioneInput.error = getString(R.string.descriptionRequired)
            }
            if (descrizione.length < 10) {
                descrizioneInput.error = getString(R.string.descriptionInvalid)
            }


            return
        }

        Toast.makeText(this, getString(R.string.addWorkoutInitialized), Toast.LENGTH_SHORT).show()

        val category = "cardio"
        val duration = 270

        splitName = name.split(" ")

        for (i in 0..splitName.size -1) {
            for (y in 1..splitName[i].length) {
                searchKeyList.add(splitName[i].substring(0, y).toLowerCase())
            }
        }

        val workoutMap: MutableMap<Any, Any> = HashMap()
        workoutMap["userMail"] = auth.email.toString()
        workoutMap["userName"] = username
        workoutMap["category"] = category
        workoutMap["name"] = name
        workoutMap["description"] = descrizione
        workoutMap["duration"] = duration
        workoutMap["imageUrl"] = ""
        workoutMap["videoUrl"] = ""
        workoutMap["favourites"] = favourites
        workoutMap["likes"] = likes
        workoutMap["searchKeywords"] = searchKeyList

        FirebaseFirestore.getInstance()
            .collection("workouts").document().set(workoutMap)

        if (imageName != "") {
            val uploadImage =
                storageRef.child("${category}/${auth.email}_${name}_immagine").putFile(imagePath)
            uploadImage.addOnSuccessListener {
                val downloadURLImage =
                    storageRef.child("${category}/${auth.email}_${name}_immagine").downloadUrl
                downloadURLImage.addOnSuccessListener {
                    FirebaseFirestore.getInstance().collection("workouts").document()
                        .update("imageUrl", it.toString())
                }.addOnFailureListener {
                }

            }.addOnFailureListener {
                Toast.makeText(this,
                    "${getString(R.string.somethingWentWrong)}: ${it.printStackTrace()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (videoName != "") {
            val uploadVideo =
                storageRef.child("${category}/${auth.email}_${name}_video").putFile(videoPath)
            uploadVideo.addOnSuccessListener {
                val downloadURLVideo =
                    storageRef.child("${category}/${auth.email}_${name}_video").downloadUrl
                downloadURLVideo.addOnSuccessListener {
                    FirebaseFirestore.getInstance().collection("workouts").document()
                        .update("videoUrl", it.toString())
                }.addOnFailureListener {
                }

            }.addOnFailureListener {
                Toast.makeText(this,
                    "${getString(R.string.somethingWentWrong)}: ${it.printStackTrace()}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        FirebaseFirestore.getInstance()
            .collection("statistics")
            .document(category)
            .update("totalWorkouts", FieldValue.increment(1))

        onBackPressed()

        Toast.makeText(this, getString(R.string.addWorkoutSuccess), Toast.LENGTH_SHORT).show()

    }

    private fun selectImage() {
        var intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), IMAGE_CODE)
    }

    private fun selectVideo() {
        var intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Seleziona Video"), VIDEO_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imagePath = data.data!!
            imageName = imagePath.toString()
            binding.titleSelectImage.setText(imageName)
        } else
            if (requestCode == VIDEO_CODE && resultCode == Activity.RESULT_OK && data != null) {
                videoPath = data.data!!
                videoName = videoPath.toString()
                binding.titleSelectVideo.setText(videoName)
            }
    }

}