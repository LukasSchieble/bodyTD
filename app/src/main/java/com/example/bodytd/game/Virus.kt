package com.bodyTD.game

class Virus(x: Float, y: Float) : Enemy(
    x = x,
    y = y,
    _health = 50,  // Faible
    speed = 3.0f,  // Élevé
    reward = 20,   // Moyen
    damage = 10
) {
    override fun attack() {
        // Comportement d'attaque spécifique aux virus
    }

    override fun customMove() {
        // Les virus se déplacent plus rapidement par moments
        // Cette méthode pourrait implémenter un mouvement en zigzag ou des accélérations
    }

    fun moveFast() {
        // Mouvement rapide temporaire
    }
}