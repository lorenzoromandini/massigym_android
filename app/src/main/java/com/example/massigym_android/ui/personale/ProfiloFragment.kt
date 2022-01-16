package com.example.massigym_android.ui.personale

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.massigym_android.R
import com.example.massigym_android.databinding.FragmentProfiloBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

// classe che gestisce il profilo dell'utente
class ProfiloFragment : Fragment() {

    private lateinit var binding: FragmentProfiloBinding

    private lateinit var auth: FirebaseUser

    private lateinit var storageRef: StorageReference

    private lateinit var toolbar: Toolbar

    private lateinit var progressDialog: ProgressDialog

    companion object {
        const val REQUEST_FROM_CAMERA = 1
        const val REQUEST_FROM_GALLERY = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentProfiloBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        auth = FirebaseAuth.getInstance().currentUser!!

        storageRef = FirebaseStorage.getInstance().reference

        // mostra username e email dell'utente all'interno della vista
        FirebaseFirestore.getInstance().collection("users").document(auth.email!!)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = task.result
                    checkImage(user)
                    binding.profileUsername.setText(user["username"].toString())
                    binding.profileEmail.setText(auth.email.toString())
                }
            }

        // setta il click dell'icona di logout presente sulla AppBar
        // in alto a destra, richiamando il metodo "logout()"
        binding.toolbarProfilo.menu.getItem(0)
            .setOnMenuItemClickListener {
                logout()
                true
            }

        binding.selectImageButton.setOnClickListener {
            selectImage()
        }

        binding.shootPhotoButton.setOnClickListener {
            shootPhoto()
        }

        // al click dell'opportuno bottone l'utente viene reindirizzato
        // in una nuova schermata in cui può modificare la propria password di accesso
        binding.changePasswordButton.setOnClickListener {
            val intent = Intent(context, ChangePassword::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    // metodo che permette di tornare indietro alla schermata precedente premendo l'apposito pulsante sulla AppBar
    // in alto a sinistra, facendo uso del navigation
    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarProfilo
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    // metodo che si occupa del Logout dell'utente attraverso l'utilizzo delle API di Firebase
    private fun logout() {
        // funzionalità predefinita di Firebase "signOut()" che effettua il logout dell'utente
        FirebaseAuth.getInstance().signOut()
        // una volta effettuato il logout l'utente viene reindirizzato alla schermata di Login e
        // vengono rimosse le activity e i fragment presenti nello stack
        startActivity(Intent(context, LoginActivity::class.java))
        Toast.makeText(context, "Logout effettuato", Toast.LENGTH_SHORT).show()
        getFragmentManager()?.beginTransaction()?.remove(this)?.commitAllowingStateLoss()
        activity?.finish()
    }

    // metodo che verifica se l'utente ha caricato una propria immagine e setta l'immagine presente
    // nella schermata di conseguenza
    private fun checkImage(user: DocumentSnapshot) {
        // se l'utente non ha inserito un'immagine viene mostrata un'immagine di default
        if (user["imageUrl"] == "") {
            binding.profileImage.setImageResource(R.drawable.profile_image_empty)
            // se l'utente ha inserito un'immagine viene ottenuto l'url da Firestore
            // e mostrata nella vista tramite l'utilizzo della libreria Glide
        } else {
            Glide.with(this)
                .load(user["imageUrl"].toString())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .into(binding.profileImage)
        }

    }

    // metodo che permette di caricare l'immagine di profilo dell'utente prima nello Storage di Firebase
    // e successivamente all'interno di Firestore
    private fun uploadImage(context: Context, imageUri: Uri) {
        // alla chiamata del metodo viene mostrata una progressBar con un messaggio
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Attendere, caricamento dell'immagine...")
        progressDialog.show()
        // caricamento dell'immagine all'interno dello Storage di Firebase a partire dall'uri
        val uploadTask = storageRef.child("profileImage/${auth.email}").putFile(imageUri)
        uploadTask.addOnSuccessListener {
            Toast.makeText(context, getString(R.string.profileImageChanged),
                Toast.LENGTH_SHORT
            ).show()
            // ottiene l'url dell'immagine dallo Storage
            val downloadURLTask = storageRef.child("profileImage/${auth.email}").downloadUrl
            downloadURLTask.addOnSuccessListener {
                // caricamento dell'url dell'immagine all'interno di Firestore
                FirebaseFirestore.getInstance().collection("users").document(auth.email!!)
                    .update("imageUrl", it.toString())
                // la progressBar viene rimossa
                progressDialog.dismiss()
            }.addOnFailureListener {
                progressDialog.dismiss()
            }
        }.addOnFailureListener {
            Toast.makeText(context,
                "${getString(R.string.somethingWentWrong)}: ${it.printStackTrace()}",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    // metodo che permette di selezionare l'immagine del profilo dall'archivio del telefono dell'utente
    // tramite la libreria ImagePicker
    private fun selectImage() {
        ImagePicker.with(this).galleryOnly()
            .crop()
            .start(REQUEST_FROM_GALLERY)
    }

    // metodo che permette di scattare la foto del profilo tramite la fotocamera del telefono dell'utente
    // tramite la libreria ImagePicker
    private fun shootPhoto() {
        ImagePicker.with(this).cameraOnly()
            .crop()
            .start(REQUEST_FROM_CAMERA)
    }

    // dopo la selezione o lo scatto dell'immagine viene invocato il metodo "uploadImage" che provvede
    // a caricarla nel database
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_FROM_CAMERA -> {
                    uploadImage(this.requireContext(), data!!.data!!)
                }
                REQUEST_FROM_GALLERY -> {
                    uploadImage(this.requireContext(), data!!.data!!)
                }
            }
        }
    }


}