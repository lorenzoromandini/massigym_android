package com.example.massigym_android

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.example.massigym_android.databinding.FragmentChangePasswordBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class ChangePasswordFragment : Fragment() {

    private lateinit var binding: FragmentChangePasswordBinding

    private lateinit var passwordInput: TextInputEditText
    private lateinit var confermaPasswordInput: TextInputEditText

    private lateinit var auth: FirebaseUser

    private lateinit var toolbar: Toolbar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)

        setupToolbarWithNavigation()

        passwordInput = binding.changeNewPassword
        confermaPasswordInput = binding.changeConfirmPassword

        auth = FirebaseAuth.getInstance().currentUser!!

        binding.changePasswordButton.setOnClickListener {
            try {
                val password = passwordInput.text.toString().trim()

                changePassword(password)

            } catch (e: Exception) {
                if (TextUtils.isEmpty(passwordInput.text.toString().trim()) ||
                    TextUtils.isEmpty(confermaPasswordInput.text.toString().trim())
                ) {
                    if (TextUtils.isEmpty(passwordInput.text.toString().trim())) {
                        passwordInput.error = "Password richiesta"
                    }
                    if (TextUtils.isEmpty(confermaPasswordInput.text.toString().trim())) {
                        confermaPasswordInput.error = "Conferma Password richiesta"
                    }
                    return@setOnClickListener
                } else if (passwordInput.text.toString()
                        .trim() != confermaPasswordInput.text.toString().trim()
                ) {
                    confermaPasswordInput.error =
                        "Password e Conferma Password non coincidono"
                    return@setOnClickListener
                }
            }
        }

        return binding.root

    }

    private fun setupToolbarWithNavigation() {
        toolbar = binding.toolbarChangePassword
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun changePassword(password: String) {
        auth.updatePassword(password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Modifica Password effettuata",
                        Toast.LENGTH_SHORT
                    ).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(context, LoginActivity::class.java))
                } else {
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        context,
                        "La Password non può essere modificata",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}