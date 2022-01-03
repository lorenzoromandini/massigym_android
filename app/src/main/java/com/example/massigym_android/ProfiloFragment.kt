package com.example.massigym_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.massigym_android.databinding.FragmentProfiloBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfiloFragment : Fragment() {

    private lateinit var binding: FragmentProfiloBinding

    private lateinit var auth: FirebaseUser

    private lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding = FragmentProfiloBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

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

}