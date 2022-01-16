package com.example.massigym_android.ui.workout

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
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
import java.util.*


// classe che gestisce l'inserimento di un nuovo allenamento
class AddWorkout : AppCompatActivity() {

    private lateinit var binding: ActivityAddWorkoutBinding

    private lateinit var imagePath: Uri
    private lateinit var videoPath: Uri

    private var imageName: String = ""
    private var videoName: String = ""

    private var IMAGE_CODE = 1
    private var VIDEO_CODE = 2

    private var REQUEST_CODE = 0

    private val STORAGE_PERMISSION_CODE: Int = 1000

    private var category: String = ""
    private var duration: String = ""

    private lateinit var auth: FirebaseUser

    private lateinit var username: String

    private lateinit var storageRef: StorageReference

    private var favourites: ArrayList<String> = arrayListOf()
    private var likes: ArrayList<String> = arrayListOf()
    private var searchKeyList: ArrayList<String> = arrayListOf()
    private var splitName: List<String> = listOf()

    private lateinit var documentID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddWorkoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarAddWorkout.setNavigationOnClickListener { onBackPressed() }

        auth = FirebaseAuth.getInstance().currentUser!!

        storageRef = FirebaseStorage.getInstance().reference

        documentID = getRandomString(20)

        // ottiene lo username dell'utente autenticato
        FirebaseFirestore.getInstance().collection("users").document(auth.email!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result
                    username = user["username"].toString()
                }
            }

        binding.titleSelectImage.setText(getString(R.string.noFileSelected))
        binding.titleSelectVideo.setText(getString(R.string.noFileSelected))

        // dropdown della categoria
        val categories = resources.getStringArray(R.array.categories)
        val arrayAdapterCategory = ArrayAdapter(this, R.layout.dropdown_item, categories)
        binding.autoCompleteTextViewCategory.setAdapter(arrayAdapterCategory)
        binding.autoCompleteTextViewCategory.setOnItemClickListener { adapterView, view, i, l ->
            category = adapterView?.getItemAtPosition(i).toString()
        }

        // dropdown della durata
        val durations = resources.getStringArray(R.array.durations)
        val arrayAdapterDuration = ArrayAdapter(this, R.layout.dropdown_item, durations)
        binding.autoCompleteTextViewDuration.setAdapter(arrayAdapterDuration)
        binding.autoCompleteTextViewDuration.setOnItemClickListener { adapterView, view, i, l ->
            duration = adapterView?.getItemAtPosition(i).toString()
        }

        // verifica se al click del bottone i permessi sono stati concessi e agisce di conseguenza
        binding.selectImageButton.setOnClickListener {
            REQUEST_CODE = 1
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission((Manifest.permission.READ_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_DENIED) {
                    // permission denied, request it

                    // show popup for runtime permission
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE)
                } else {
                    // permission already granted, perform download
                    selectImage()
                }
            } else {
                // system os is less than marshmallow, runtime permission not required, perform download
                selectImage()
            }

        }

        // verifica se al click del bottone i permessi sono stati concessi e agisce di conseguenza
        binding.selectVideoButton.setOnClickListener {
            REQUEST_CODE = 2
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission((Manifest.permission.READ_EXTERNAL_STORAGE)) == PackageManager.PERMISSION_DENIED) {
                    // permission denied, request it

                    // show popup for runtime permission
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE)
                } else {
                    // permission already granted, perform download
                    selectVideo()
                }
            } else {
                // system os is less than marshmallow, runtime permission not required, perform download
                selectVideo()
            }

        }

        binding.addWorkoutButton.setOnClickListener {
            addWorkout()
        }

    }

    // metodo che si occupa dell'inserimento di un nuovo allenamento attraverso l'utilizzo delle API di Firebase
    // L'utente deve inserire nome, descrizione, categoria, durata, mentre può scegliere se inserire immagine e video
    private fun addWorkout() {

        val name = binding.workoutName.text.toString().trim()
        val nameInput = binding.workoutName
        val descrizione = binding.workoutDescription.text.toString().trim()
        val descrizioneInput = binding.workoutDescription
        val categoryInput = binding.categoryInput
        val durationInput = binding.durationInput

        // controllo delle form compilate dall'utente.
        // Se le regole non vengono rispettate ogni campo mostrerà il proprio specifico errore
        // e l'inserimento non verrà effettuato
        if (name.length < 2 || descrizione.length < 10 ||
            category.isEmpty() || duration.isEmpty()
        ) {
            if (name.length < 2) {
                nameInput.error = getString(R.string.nameInvalid)
            }
            if (descrizione.length < 10) {
                descrizioneInput.error = getString(R.string.descriptionInvalid)
            }
            if (category.isEmpty()) {
                categoryInput.error = "Categoria richiesta"
            }
            if (descrizione.isEmpty()) {
                durationInput.error = "Durata richiesta"
            }
            return
        }

        Toast.makeText(this, getString(R.string.addWorkoutInitialized), Toast.LENGTH_SHORT).show()

        splitName = name.split(" ")

        // divide il nome in stringhe contenenti caratteri consecutivi del nome, e le inserisce all'interno
        // di un array
        for (i in 0..splitName.size - 1) {
            for (y in 1..splitName[i].length) {
                searchKeyList.add(splitName[i].substring(0, y).toLowerCase())
            }
        }

        // inizializza una HashMap e la popola con gli elementi che verranno poi inseriti all'interno del database
        val workoutMap: MutableMap<Any, Any> = HashMap()
        workoutMap["userMail"] = auth.email.toString()
        workoutMap["userName"] = username
        workoutMap["category"] = category
        workoutMap["name"] = name
        workoutMap["description"] = descrizione
        workoutMap["duration"] = duration.toInt()
        workoutMap["imageUrl"] = ""
        workoutMap["videoUrl"] = ""
        workoutMap["favourites"] = favourites
        workoutMap["likes"] = likes
        workoutMap["searchKeywords"] = searchKeyList
        workoutMap["totalLikes"] = 0

        // inserisce il workout all'interno di Cloud Firestore
        FirebaseFirestore.getInstance()
            .collection("workouts").document(documentID).set(workoutMap)

        // se è stata inserita un'immagine
        if (imageName != "") {
            // caricamento dell'immagine all'interno dello Storage di Firebase a partire dall'uri
            val uploadImage =
                storageRef.child("${category}/${auth.email}_${name}_immagine").putFile(imagePath)
            uploadImage.addOnSuccessListener {
                // ottiene l'url dell'immagine dallo Storage
                val downloadURLImage =
                    storageRef.child("${category}/${auth.email}_${name}_immagine").downloadUrl
                downloadURLImage.addOnSuccessListener {
                    // caricamento dell'url dell'immagine all'interno di Firestore
                    FirebaseFirestore.getInstance().collection("workouts").document(documentID)
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

        // se è stata inserita un video
        if (videoName != "") {
            // caricamento del video all'interno dello Storage di Firebase a partire dall'uri
            val uploadVideo =
                storageRef.child("${category}/${auth.email}_${name}_video").putFile(videoPath)
            uploadVideo.addOnSuccessListener {
                // ottiene l'url del video dallo Storage
                val downloadURLVideo =
                    storageRef.child("${category}/${auth.email}_${name}_video").downloadUrl
                downloadURLVideo.addOnSuccessListener {
                    // caricamento dell'url del video all'interno di Firestore
                    FirebaseFirestore.getInstance().collection("workouts").document(documentID)
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

        // incrementa di 1 il numero di workouts di quella categoria nel campo delle statistiche
        FirebaseFirestore.getInstance()
            .collection("statistics")
            .document(category)
            .update("totalWorkouts", FieldValue.increment(1))

        onBackPressed()

        Toast.makeText(this, getString(R.string.addWorkoutSuccess), Toast.LENGTH_SHORT).show()

    }

    // metodo che permette di selezionare l'immagine dell'allenamento dall'archivio del telefono dell'utente
    private fun selectImage() {
        var intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), IMAGE_CODE)
    }

    // metodo che permette di selezionare il video dell'allenamento dall'archivio del telefono dell'utente
    private fun selectVideo() {
        var intent = Intent()
        intent.setType("video/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Seleziona Video"), VIDEO_CODE)
    }

    // ottiene l'uri dell'immagine e/o del video caricati
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

    // metodo che richiama "selectImage" o "selectVideo" una volta che l'utente ha concesso i permessi, altrimenti
    // mostra un Toast con un messaggio
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && REQUEST_CODE == IMAGE_CODE) {
                    // permission from popup was granted
                    selectImage()
                } else if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && REQUEST_CODE == VIDEO_CODE) {
                    // permission from popup was granted
                    selectVideo()
                } else {
                    // permission from popup was denied, show error message
                    Toast.makeText(this, getString(R.string.grantPermissions), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
    }

    // metodo che genera casualmente una stringa della lunghezza scelta
    fun getRandomString(length: Int) : String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

}