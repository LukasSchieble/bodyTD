package com.bodyTD.game

abstract class Tourelle(
    val x: Float,
    val y: Float,
    val cost: Int,
    val range: Int,
    val attackSpeed: Float,
    var strategy: AttackStrategy
) {
    var lastAttackTime: Long = 0
    val attackCooldown = (1000 / attackSpeed).toLong() // En millisecondes

    open fun attack(enemy: Enemy): Boolean {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastAttackTime >= attackCooldown) {
            lastAttackTime = currentTime
            return strategy.attack(enemy)
        }
        return false
    }

    fun isInRange(enemy: Enemy): Boolean {
        val dx = enemy.x - x
        val dy = enemy.y - y
        val distanceSquared = dx * dx + dy * dy
        return distanceSquared <= range * range
    }

    abstract fun upgrade()
}