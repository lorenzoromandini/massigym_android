package com.example.massigym_android.ui.personale

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityChangePasswordBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// classe che gestisce la Modifica della password da parte dell'utente autenticato
class ChangePassword : AppCompatActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarChangePassword.setNavigationOnClickListener { onBackPressed() }

        user = FirebaseAuth.getInstance().currentUser!!

        binding.changePasswordButton.setOnClickListener {
            changePassword()
        }

    }

    // metodo che si occupa della Modifica della password dell'utente attraverso l'utilizzo delle API di Firebase
    // L'utente deve inserire la nuova password e confermarla
    private fun changePassword() {

        val password = binding.changeNewPassword.text.toString().trim()
        val passwordInput = binding.changeNewPassword
        val confermaPassword = binding.changeConfirmPassword.text.toString().trim()
        val confermaPasswordInput = binding.changeConfirmPassword

        // controllo delle form compilate dall'utente.
        // Se le regole non vengono rispettate ogni campo mostrerà il proprio specifico errore
        // e la password non verrà modificata
        if (password.length < 6 || confermaPassword.isEmpty() || password != confermaPassword
        ) {
            if (password.length < 6) {
                passwordInput.error = getString(R.string.passwordInvalid)
            }
            if (confermaPassword.isEmpty()) {
                confermaPasswordInput.error = getString(R.string.passwordConfirmRequired)
            }
            if (password != confermaPassword) {
                confermaPasswordInput.error = getString(R.string.passwordNotEquals)
            }
            return
        }

        // se le password inserite rispettano i vincoli si procede con la modifica della password
        // tramite la funzionalità predefinita di Firebase "updatePassword"
        // che prende in ingresso la nuova password
        user.updatePassword(password)
            .addOnCompleteListener { task ->
                // se la Modifica della password va a buon fine l'utente viene sloggato
                // e successivamente reindirizzato alla schermata di Login
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        getString(R.string.passwordChangedSuccessfully),
                        Toast.LENGTH_LONG
                    ).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    // se la Modifica della password non va a buon fine viene mostrato un Toast con un messaggio di errore
                } else {
                    task.exception!!.printStackTrace()
                    Toast.makeText(
                        this,
                        getString(R.string.somethingWentWrong),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}