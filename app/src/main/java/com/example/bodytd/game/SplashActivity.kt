@file:Suppress("DEPRECATION")

package com.bodyTD.game

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    private var difficulty = "normal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mettre l'application en plein écran
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Forcer l'orientation paysage
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        // Cacher la barre de navigation
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions

        // Définir le layout
        setContentView(R.layout.activity_splash)

        // Configuration des boutons
        val startButton = findViewById<Button>(R.id.startButton)
        val difficultyGroup = findViewById<RadioGroup>(R.id.difficultyGroup)

        difficultyGroup.setOnCheckedChangeListener { _, checkedId ->
            difficulty = when (checkedId) {
                R.id.easyRadio -> "easy"
                R.id.hardRadio -> "hard"
                else -> "normal"
            }
        }

        startButton.setOnClickListener {
            // Démarrer l'activité principale avec la difficulté sélectionnée
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("DIFFICULTY", difficulty)
            startActivity(intent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        // Cacher à nouveau la barre de navigation en cas de changement
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }
}