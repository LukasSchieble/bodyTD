package com.bodyTD.game

interface AttackStrategy {
    fun attack(target: Enemy): Boolean
}

class SingleTargetAttack(private val damage: Int) : AttackStrategy {
    override fun attack(target: Enemy): Boolean {
        return target.takeDamage(damage)
    }
}

class PushBackAttack(private val damage: Int, private val pushForce: Int) : AttackStrategy {
    override fun attack(target: Enemy): Boolean {
        val killed = target.takeDamage(damage)

        // Push enemy back on the path
        if (!killed && target.currentPathIndex > 0) {
            target.currentPathIndex = maxOf(0, target.currentPathIndex - pushForce / 10)

            // Update position to match the path point
            if (target.path.isNotEmpty() && target.currentPathIndex < target.path.size) {
                val newPosition = target.path[target.currentPathIndex]
                target.x = newPosition.first
                target.y = newPosition.second
            }
        }

        return killed
    }
}

class SlowEffectAttack(private val damage: Int, private val slowFactor: Float) : AttackStrategy {
    override fun attack(target: Enemy): Boolean {
        // Appliquer un effet de ralentissement temporaire
        // Dans une implémentation plus complète, cela ajouterait un status effect
        // à l'ennemi qui serait géré par un système de status effects

        // Pour l'instant, nous appliquerons juste les dégâts
        return target.takeDamage(damage)
    }
}