@file:Suppress("DEPRECATION")

package com.bodyTD.game

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var gameView: GameView

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

        // Créer la vue du jeu
        gameView = GameView(this)
        setContentView(gameView)

        // S'assurer que la vue ait le focus
        gameView.isFocusable = true
        gameView.isFocusableInTouchMode = true
        gameView.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        // Cacher à nouveau la barre de navigation en cas de changement
        val decorView = window.decorView
        val uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        decorView.systemUiVisibility = uiOptions
    }

    override fun onPause() {
        super.onPause()
        // Gérer la mise en pause du jeu si nécessaire
    }
}