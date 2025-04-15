package com.bodyTD.game

class WaveManager(
    private val startX: Float,
    private val startY: Float,
    private val path: List<Pair<Float, Float>>
) {
    companion object {
        const val MAX_WAVES = 10
    }

    private var currentWave = 0
    private var enemiesInWave = 0
    private var enemiesSpawned = 0
    private var timeSinceLastSpawn = 0L
    private val spawnInterval = 1500L // 1.5 seconds between enemies
    private val activeEnemies = mutableListOf<Enemy>()

    fun getCurrentWave(): Int {
        return currentWave
    }

    fun getActiveEnemies(): List<Enemy> {
        return activeEnemies
    }

    fun startNextWave() {
        if (currentWave < MAX_WAVES) {
            currentWave++
            enemiesInWave = calculateEnemiesInWave(currentWave)
            enemiesSpawned = 0
            timeSinceLastSpawn = 0
        }
    }

    private fun calculateEnemiesInWave(wave: Int): Int {
        // La difficulté augmente avec le nombre de vagues
        return 5 + wave * 2
    }

    fun update(deltaTime: Long): Boolean {
        // Si aucune vague n'est active, rien à faire
        if (currentWave == 0) return false

        var waveCompleted = false

        // Création des ennemis
        timeSinceLastSpawn += deltaTime
        if (enemiesSpawned < enemiesInWave && timeSinceLastSpawn >= spawnInterval) {
            spawnEnemy()
            timeSinceLastSpawn = 0
        }

        // Gérer les ennemis actifs
        val iterator = activeEnemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()

            if (enemy.isDead()) {
                iterator.remove()
            } else {
                enemy.move()
            }
        }

        // Vérifier si la vague est terminée
        if (enemiesSpawned >= enemiesInWave && activeEnemies.isEmpty()) {
            waveCompleted = true
        }

        return waveCompleted
    }

    private fun spawnEnemy() {
        val enemyType = when {
            currentWave < 3 -> {
                // Vagues initiales: principalement des particules fines et quelques bactéries
                if (Math.random() < 0.7) "particule" else "bacterie"
            }
            currentWave < 7 -> {
                // Vagues moyennes: mix des trois types d'ennemis
                val rand = Math.random()
                when {
                    rand < 0.4 -> "particule"
                    rand < 0.8 -> "bacterie"
                    else -> "virus"
                }
            }
            else -> {
                // Vagues finales: principalement des virus et des bactéries
                if (Math.random() < 0.6) "virus" else "bacterie"
            }
        }

        val enemy = EnemyFactory.createEnemy(enemyType, startX, startY)
        enemy.path = path
        activeEnemies.add(enemy)
        enemiesSpawned++
    }

    fun removeEnemy(enemy: Enemy) {
        activeEnemies.remove(enemy)
    }

    fun isWaveInProgress(): Boolean {
        return currentWave > 0 && (enemiesSpawned < enemiesInWave || activeEnemies.isNotEmpty())
    }

    fun isGameCompleted(): Boolean {
        return currentWave >= MAX_WAVES && !isWaveInProgress()
    }

    fun reset() {
        currentWave = 0
        enemiesInWave = 0
        enemiesSpawned = 0
        activeEnemies.clear()
    }
}