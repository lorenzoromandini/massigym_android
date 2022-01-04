package com.example.massigym_android.ui.personale

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
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


class ProfiloFragment : Fragment() {

    private lateinit var binding: FragmentProfiloBinding

    private lateinit var auth: FirebaseUser

    private lateinit var storageRef: StorageReference

    private lateinit var toolbar: Toolbar

    private lateinit var progressDialog: ProgressDialog

    private lateinit var selectImageButton: Button

    private lateinit var shootImageButton: Button

    companion object {
        const val REQUEST_FROM_CAMERA = 1
        const val REQUEST_FROM_GALLERY = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentProfiloBinding.inflate(inflater, container, false)

        selectImageButton = binding.selectImageButton
        shootImageButton = binding.shootPhotoButton

        setupToolbarWithNavigation()

        auth = FirebaseAuth.getInstance().currentUser!!

        storageRef = FirebaseStorage.getInstance().reference

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

        val logoutMenu = binding.toolbarProfilo.menu.getItem(0)
        logoutMenu.setOnMenuItemClickListener {
            logout()
            true
        }

        selectImageButton.setOnClickListener {
            selectImage()
        }

        shootImageButton.setOnClickListener {
            shootPhoto()
        }

        binding.changePasswordButton.setOnClickListener {
            binding.root.findNavController()
                .navigate(R.id.from_profilo_to_changePassword)
        }


        return binding.root
    }

    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarProfilo
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(context, LoginActivity::class.java))
        Toast.makeText(context, "Logout effettuato", Toast.LENGTH_SHORT).show()
    }

    private fun checkImage(user: DocumentSnapshot) {
        if (user["imageUrl"] == "") {
            binding.profileImage.setImageResource(R.drawable.profile_image_empty)
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

    private fun uploadImage(context: Context, imageUri: Uri) {
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Please wait, image being uploading...")
        progressDialog.show()
        val uploadTask = storageRef.child("profileImage/${auth.email}").putFile(imageUri)
        uploadTask.addOnSuccessListener {
            Log.e(TAG, "Image upload successfully")
            val downloadURLTask = storageRef.child("profileImage/${auth.email}").downloadUrl
            downloadURLTask.addOnSuccessListener {
                Log.e(TAG, "Image Path: $it")
                FirebaseFirestore.getInstance().collection("users").document(auth.email!!).update("imageUrl", it.toString())
                progressDialog.dismiss()
            }.addOnFailureListener {
                progressDialog.dismiss()
            }
        }.addOnFailureListener {
            Log.e(TAG, "Image upload failed ${it.printStackTrace()}")
        }

    }

    private fun selectImage() {
        ImagePicker.with(this).galleryOnly()
            .crop()
            .start(REQUEST_FROM_GALLERY)
    }

    private fun shootPhoto() {
        ImagePicker.with(this).cameraOnly()
            .crop()
            .start(REQUEST_FROM_CAMERA)
    }

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