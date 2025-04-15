package com.bodyTD.game

class TouxTower(x: Float, y: Float) : Tourelle(
    x = x,
    y = y,
    cost = 50,
    range = 200,
    attackSpeed = 1.5f,
    strategy = PushBackAttack(10, 30) // Dégâts faibles mais effet de recul
) {
    val pushBackForce: Int = 30

    override fun upgrade() {
        // Améliorer les statistiques de la tour
    }
}