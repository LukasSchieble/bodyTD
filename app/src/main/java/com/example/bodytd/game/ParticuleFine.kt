package com.bodyTD.game

class ParticuleFine(x: Float, y: Float) : Enemy(
    x = x,
    y = y,
    _health = 30,  // Faible
    speed = 2.0f,  // Moyenne
    reward = 10,   // Faible
    damage = 5
) {
    override fun attack() {
        // Comportement d'attaque spécifique aux particules fines
        // Par exemple, une attaque qui réduit la visibilité ou l'efficacité des tourelles
    }
}