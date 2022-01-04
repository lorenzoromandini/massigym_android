package com.example.massigym_android.ui.personale

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.massigym_android.R
import com.example.massigym_android.databinding.FragmentProfiloBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class ProfiloFragment : Fragment() {

    private lateinit var binding: FragmentProfiloBinding

    private lateinit var auth: FirebaseUser

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentProfiloBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        auth = FirebaseAuth.getInstance().currentUser!!

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

}