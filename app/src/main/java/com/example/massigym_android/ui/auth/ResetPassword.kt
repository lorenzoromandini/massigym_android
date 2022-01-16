package com.example.massigym_android.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.util.PatternsCompat
import com.example.massigym_android.R
import com.example.massigym_android.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

// classe che gestisce il Reset della password dell'utente non autenticato (ma registrato)
class ResetPassword : AppCompatActivity() {

    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.toolbarResetPassword.setNavigationOnClickListener { onBackPressed() }

        binding.resetPasswordButton.setOnClickListener {
            resetPassword()
        }
    }

    // metodo che si occupa del Reset della password dell'utente attraverso l'utilizzo delle API di Firebase
    // L'utente deve inserire l'email a cui inviare un messaggio per il reset della password
    private fun resetPassword() {
        val email = binding.resetPasswordEmail.text.toString().trim()
        val emailInput = binding.resetPasswordEmail


        // controllo delle form compilate dall'utente.
        // Se le regole non vengono rispettate ogni campo mostrerà il proprio specifico errore
        // e l'email di reset della password non verrà inviata
        if (email.isEmpty()) {
            emailInput.error = getString(R.string.emailRequired)
            return
        }
        if (!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.error = getString(R.string.emailInvalid)
            return
        }

        // se l'email inserita possiede i requisiti necessari si procede con l'invio della mail per il reset della password
        // tramite la funzionalità predefinita di Firebase "sendPasswordResetEmail"
        // che prende in ingresso l'email dell'utente
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
            // se l'invio della mail va a buon fine l'utente viene reindirizzato alla schermata di Login,
            // mentre nella sua casella di posta elettronica dovrebbe arrivare la mail per il reset
            if (task.isSuccessful) {
                Toast.makeText(this,
                    getString(R.string.sendResetPassword),
                    Toast.LENGTH_LONG).show()
                onBackPressed()
                // se l'invio della mail non va a buon fine viene mostrato un Toast con un messaggio di errore
            } else {
                Toast.makeText(this, getString(R.string.somethingWentWrong), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

}