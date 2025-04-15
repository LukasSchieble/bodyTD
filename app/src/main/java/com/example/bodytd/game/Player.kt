package com.bodyTD.game

class Player {
    var money: Int = 150
    var health: Int = 100

    fun addMoney(amount: Int) {
        money += amount
    }

    fun canBuyTurret(cost: Int): Boolean {
        return money >= cost
    }

    fun buyTurret(cost: Int): Boolean {
        if (canBuyTurret(cost)) {
            money -= cost
            return true
        }
        return false
    }

    fun takeDamage(amount: Int) {
        health -= amount
    }

    fun isGameOver(): Boolean {
        return health <= 0
    }
}