package com.example.massigym_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.massigym_android.databinding.ActivityMainBinding
import com.example.massigym_android.ui.auth.LoginActivity
import com.example.massigym_android.ui.common.BottomNavBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

// entry point dell'applicazione, consistente in uno splash screen e successivamente in
// un reindirizzamento ad una nuova schermata
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        user = FirebaseAuth.getInstance().currentUser

        @Suppress("DEPRECATION")
        // metodo che permette di eseguire i comandi al suo interno dopo un tempo prestabilito
        Handler().postDelayed(
            {
                // se l'utente è autenticato allora verrà reindirizzato alla home page
                // (BottomNavBar è il navigator disposto nella parte inferiore dello schermo)
                if (user != null) {
                    val intent = Intent(this, BottomNavBar::class.java)
                    startActivity(intent)
                    finish()
                    // se l'utente non è autenticato viene portato alla pagina di Login
                } else {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            },
            2000
        )
    }

}