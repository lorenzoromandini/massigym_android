package com.example.massigym_android

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.massigym_android.databinding.FragmentPersonaleBinding
import com.example.massigym_android.databinding.FragmentProfiloBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class ProfiloFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val binding: FragmentProfiloBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profilo, container, false)







        val logoutMenu = binding.toolbarProfilo.menu.getItem(0)
        logoutMenu.setOnMenuItemClickListener {
            logout()
            true
        }

        return binding.root
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(context, LoginActivity::class.java))
        Toast.makeText(context, "Logout effettuato", Toast.LENGTH_SHORT).show()
    }

}