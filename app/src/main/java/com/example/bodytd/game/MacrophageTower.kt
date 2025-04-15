package com.bodyTD.game

class MacrophageTower(x: Float, y: Float) : Tourelle(
    x = x,
    y = y,
    cost = 100,
    range = 150,
    attackSpeed = 1.0f,
    strategy = SingleTargetAttack(30) // Dégâts élevés
) {
    override fun upgrade() {
        // Améliorer les statistiques de la tour
        // Par exemple, augmenter les dégâts ou la portée
    }
}