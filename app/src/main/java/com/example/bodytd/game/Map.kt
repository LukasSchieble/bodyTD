package com.bodyTD.game

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF

class Map(
    val width: Int,
    val height: Int,
    val backgroundImage: Bitmap?
) {
    val path: List<Pair<Float, Float>> = createPath()
    val turretPositions: MutableList<Pair<Float, Float>> = createTurretPositions()
    val placedTurrets: MutableList<Tourelle> = mutableListOf()

    private fun createPath(): List<Pair<Float, Float>> {
        // Définir le chemin que les ennemis suivront
        // Du bas à gauche au haut à droite avec une légère sinuosité
        return listOf(
            Pair(0f, height * 0.8f),              // Départ en bas à gauche
            Pair(width * 0.2f, height * 0.8f),    // Premier point
            Pair(width * 0.3f, height * 0.6f),    // Sinuosité vers le haut
            Pair(width * 0.5f, height * 0.5f),    // Milieu
            Pair(width * 0.7f, height * 0.4f),    // Sinuosité vers le haut
            Pair(width * 0.8f, height * 0.2f),    // Avant-dernier point
            Pair(width * 1.0f, height * 0.2f)     // Fin en haut à droite
        )
    }

    private fun createTurretPositions(): MutableList<Pair<Float, Float>> {
        // Créer des positions prédéfinies pour les tourelles
        // Généralement à côté du chemin des ennemis
        return mutableListOf(
            Pair(width * 0.15f, height * 0.7f),
            Pair(width * 0.25f, height * 0.9f),
            Pair(width * 0.35f, height * 0.5f),
            Pair(width * 0.5f, height * 0.4f),
            Pair(width * 0.5f, height * 0.6f),
            Pair(width * 0.65f, height * 0.3f),
            Pair(width * 0.75f, height * 0.5f),
            Pair(width * 0.85f, height * 0.3f)
        )
    }

    fun draw(canvas: Canvas, paint: Paint) {
        // Dessiner l'arrière-plan
        backgroundImage?.let {
            canvas.drawBitmap(it, null, RectF(0f, 0f, width.toFloat(), height.toFloat()), paint)
        }

        // Dessiner le chemin (à des fins de débogage, pourrait être invisible dans le jeu final)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f

        // Dessiner les emplacements de tourelles disponibles
        paint.style = Paint.Style.FILL
        for (position in turretPositions) {
            canvas.drawCircle(position.first, position.second, 20f, paint)
        }
    }

    fun isPlacementValid(position: Pair<Float, Float>): Boolean {
        // Vérifier si la position est dans la liste des positions de tourelles valides
        // et qu'aucune tourelle n'y est déjà placée

        for (validPos in turretPositions) {
            val dx = position.first - validPos.first
            val dy = position.second - validPos.second
            val distanceSquared = dx * dx + dy * dy

            if (distanceSquared < 30 * 30) {
                // Vérifier qu'aucune tourelle n'est déjà à cette position
                for (turret in placedTurrets) {
                    val tdx = turret.x - validPos.first
                    val tdy = turret.y - validPos.second
                    val turretDistanceSquared = tdx * tdx + tdy * tdy

                    if (turretDistanceSquared < 10 * 10) {
                        return false
                    }
                }
                return true
            }
        }

        return false
    }

    fun getClosestValidPosition(touchX: Float, touchY: Float): Pair<Float, Float>? {
        var closestPos: Pair<Float, Float>? = null
        var minDistance = Float.MAX_VALUE

        for (position in turretPositions) {
            val dx = touchX - position.first
            val dy = touchY - position.second
            val distanceSquared = dx * dx + dy * dy

            if (distanceSquared < minDistance) {
                // Vérifier qu'aucune tourelle n'est déjà à cette position
                var positionOccupied = false
                for (turret in placedTurrets) {
                    val tdx = turret.x - position.first
                    val tdy = turret.y - position.second
                    val turretDistanceSquared = tdx * tdx + tdy * tdy

                    if (turretDistanceSquared < 10 * 10) {
                        positionOccupied = true
                        break
                    }
                }

                if (!positionOccupied) {
                    minDistance = distanceSquared
                    closestPos = position
                }
            }
        }

        return closestPos
    }

    fun addTurret(turret: Tourelle) {
        placedTurrets.add(turret)
    }
}