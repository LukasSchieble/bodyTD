package com.bodyTD.game

abstract class Enemy(
    var x: Float,
    var y: Float,
    private var _health: Int,
    val speed: Float,
    val reward: Int,
    val damage: Int
) {
    var path: List<Pair<Float, Float>> = listOf()
    var currentPathIndex = 0
    private var isDead = false

    fun move() {
        if (currentPathIndex < path.size) {
            val targetPoint = path[currentPathIndex]
            val dx = targetPoint.first - x
            val dy = targetPoint.second - y
            val distance = Math.sqrt((dx * dx + dy * dy).toDouble()).toFloat()

            if (distance < speed) {
                x = targetPoint.first
                y = targetPoint.second
                currentPathIndex++
            } else {
                val directionX = dx / distance
                val directionY = dy / distance
                x += directionX * speed
                y += directionY * speed
            }
        }
        // Custom movement logic for subclasses
        customMove()
    }

    // For subclasses to override if they need special movement
    open fun customMove() {}

    abstract fun attack()

    fun takeDamage(amount: Int): Boolean {
        _health -= amount
        if (_health <= 0 && !isDead) {
            isDead = true
            return true
        }
        return false
    }

    fun isDead(): Boolean {
        return isDead
    }

    fun getHealth(): Int {
        return _health
    }

    fun calculateReward(): Int {
        return reward
    }

    fun isAtEnd(): Boolean {
        return currentPathIndex >= path.size
    }
}