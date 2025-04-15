package com.bodyTD.game

class MucusTower(x: Float, y: Float) : Tourelle(
    x = x,
    y = y,
    cost = 75,
    range = 120,
    attackSpeed = 0.7f,
    strategy = SlowEffectAttack(15, 0.5f) // Dégâts moyens avec effet de ralentissement
) {
    val slowEffect: Float = 0.5f

    override fun upgrade() {
        // Améliorer les statistiques de la tour
    }
}