package com.bodyTD.game

class Bacterie(x: Float, y: Float) : Enemy(
    x = x,
    y = y,
    _health = 100, // Élevé
    speed = 1.0f,  // Faible
    reward = 30,   // Élevé
    damage = 15
) {
    override fun attack() {
        // Comportement d'attaque spécifique aux bactéries
        // Par exemple, une attaque qui cause des dégâts progressifs
    }
}