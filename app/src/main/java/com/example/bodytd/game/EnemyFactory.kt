package com.bodyTD.game

class EnemyFactory {
    companion object {
        fun createEnemy(type: String, x: Float, y: Float): Enemy {
            return when (type.lowercase()) {
                "bacterie" -> Bacterie(x, y)
                "virus" -> Virus(x, y)
                "particule" -> ParticuleFine(x, y)
                else -> throw IllegalArgumentException("Type d'ennemi inconnu: $type")
            }
        }
    }
}