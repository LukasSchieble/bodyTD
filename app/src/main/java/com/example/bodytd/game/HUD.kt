package com.bodyTD.game

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class HUD(
    private val screenWidth: Int,
    private val screenHeight: Int
) {
    private var money: Int = 0
    private var waveNumber: Int = 0
    private var gameTime: Long = 0
    private var health: Int = 0

    private val textPaint = Paint().apply {
        textSize = 40f
        color = android.graphics.Color.WHITE
        isFakeBoldText = true
        setShadowLayer(3f, 2f, 2f, android.graphics.Color.BLACK)
    }

    private val bgPaint = Paint().apply {
        color = android.graphics.Color.argb(150, 0, 0, 0)
        style = Paint.Style.FILL
    }

    fun updateDisplay(money: Int, health: Int, waveNumber: Int, gameTime: Long) {
        this.money = money
        this.health = health
        this.waveNumber = waveNumber
        this.gameTime = gameTime
    }

    fun draw(canvas: Canvas) {
        // Draw HUD background
        val hudHeight = 60f
        canvas.drawRect(0f, 0f, screenWidth.toFloat(), hudHeight, bgPaint)

        // Draw money
        textPaint.color = android.graphics.Color.YELLOW
        canvas.drawText("$: $money", 20f, 45f, textPaint)

        // Draw health
        textPaint.color = android.graphics.Color.GREEN
        canvas.drawText("♥: $health", 200f, 45f, textPaint)

        // Draw wave
        textPaint.color = android.graphics.Color.CYAN
        canvas.drawText("Vague: $waveNumber", 350f, 45f, textPaint)

        // Draw time
        textPaint.color = android.graphics.Color.WHITE
        val minutes = (gameTime / 60000).toInt()
        val seconds = ((gameTime % 60000) / 1000).toInt()
        canvas.drawText("Temps: ${String.format("%02d:%02d", minutes, seconds)}", 550f, 45f, textPaint)
    }

    // Méthode pour dessiner un bouton sélectionné
    fun drawSelectedTurret(canvas: Canvas, turretType: String) {
        val bgRect = Rect(screenWidth - 180, screenHeight - 100, screenWidth - 20, screenHeight - 20)
        canvas.drawRect(bgRect, bgPaint)

        textPaint.color = android.graphics.Color.WHITE
        textPaint.textSize = 30f
        canvas.drawText("Sélection: $turretType", screenWidth - 170f, screenHeight - 50f, textPaint)
    }

    // Méthode pour dessiner un message en jeu (victoire, défaite, etc.)
    fun drawGameMessage(canvas: Canvas, message: String) {
        val bgRect = Rect(
            screenWidth / 4,
            screenHeight / 3,
            3 * screenWidth / 4,
            2 * screenHeight / 3
        )
        canvas.drawRect(bgRect, bgPaint)

        textPaint.color = android.graphics.Color.WHITE
        textPaint.textSize = 60f

        // Centrer le texte
        val bounds = Rect()
        textPaint.getTextBounds(message, 0, message.length, bounds)
        val x = (screenWidth - bounds.width()) / 2f
        val y = screenHeight / 2f

        canvas.drawText(message, x, y, textPaint)
    }
}