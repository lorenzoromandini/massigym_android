package com.example.massigym_android.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.example.massigym_android.R
import com.example.massigym_android.ui.common.BottomNavBar
import com.example.massigym_android.databinding.ActivityLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

// classe che gestisce il Login dell'utente e il passaggio alle schermate di Registrazione
// e di Reset della password
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.goToRegister.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    // metodo che si occupa del Login dell'utente attraverso l'utilizzo delle API di Firebase.
    // L'utente deve inserire email e password
    fun login(view: View) {

        val email = binding.loginEmail.text.toString().trim()
        val emailInput = binding.loginEmail
        val password = binding.loginPassword.text.toString().trim()
        val passwordInput = binding.loginPassword

        // controllo delle form compilate dall'utente.
        // Se le regole non vengono rispettate ogni campo mostrerà il proprio specifico errore
        // e il login non verrà effettuato
        if (email.isEmpty() || !PatternsCompat.EMAIL_ADDRESS.matcher(email)
                .matches() || password.length < 6
        ) {

            if (email.isEmpty()) {
                emailInput.error = getString(R.string.emailRequired)
            }
            if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
                emailInput.error = getString(R.string.emailInvalid)
            }
            if (password.length < 6) {
                passwordInput.error = getString(R.string.passwordInvalid)
            }
            return
        }

        // se l'inserimento di email e password è corretto si procede con il Login
        // tramite la funzionalità predefinita di Firebase "signInWithEmailAndPassword"
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(OnCompleteListener<AuthResult> { task ->
                // se il Login va a buon fine l'utente viene indirizzato alla Home Page
                if (task.isSuccessful) {
                    val intent = Intent(this, BottomNavBar::class.java)
                    startActivity(intent)
                    Toast.makeText(this, getString(R.string.loginDone), Toast.LENGTH_SHORT)
                        .show()
                    finish()
                    // se il Login non va a buon fine viene mostrato un Toast con un messaggio di errore
                } else {
                    Toast.makeText(baseContext,
                        getString(R.string.somethingWentWrong),
                        Toast.LENGTH_LONG).show()
                }
            })
    }

    // metodo che reindirizza l'utente alla schermata di Reset della password
    // al click del testo "Password dimenticata ?"
    fun goToResetPassword(view: View) {
        val intent = Intent(this, ResetPassword::class.java)
        startActivity(intent)
    }

}