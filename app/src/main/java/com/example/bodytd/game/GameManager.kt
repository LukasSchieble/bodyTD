package com.bodyTD.game

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent

class GameManager private constructor(
    private val context: Context,
    val screenWidth: Int,
    val screenHeight: Int
) {
    companion object {
        private var instance: GameManager? = null

        fun getInstance(context: Context, screenWidth: Int, screenHeight: Int): GameManager {
            if (instance == null) {
                instance = GameManager(context, screenWidth, screenHeight)
            }
            return instance!!
        }
    }

    private val player = Player()
    private lateinit var map: Map
    private lateinit var waveManager: WaveManager
    private val hud = HUD(screenWidth, screenHeight)

    private var gameStarted = false
    private var gameOver = false
    private var gameWon = false
    private var gameTime = 0L
    private var lastUpdateTime = System.currentTimeMillis()

    // Gestion des tourelles
    private var selectedTurretType: String? = null
    private var turretBitmaps = mapOf<String, Bitmap>()
    private var enemyBitmaps = mapOf<String, Bitmap>()

    init {
        loadResources()
        setupGame()
    }

    private fun loadResources() {
        // Charger le fond d'écran
        val backgroundBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.background_poumon)

        // Charger les images des tourelles
        turretBitmaps = mapOf(
            "macrophage" to BitmapFactory.decodeResource(context.resources, R.drawable.macrophage),
            "toux" to BitmapFactory.decodeResource(context.resources, R.drawable.toux),
            "mucus" to BitmapFactory.decodeResource(context.resources, R.drawable.macrophage)
        )

        // Charger les images des ennemis
        enemyBitmaps = mapOf(
            "bacterie" to BitmapFactory.decodeResource(context.resources, R.drawable.bacterie),
            "virus" to BitmapFactory.decodeResource(context.resources, R.drawable.virus),
            "particule" to BitmapFactory.decodeResource(context.resources, R.drawable.particule)
        )

        map = Map(screenWidth, screenHeight, backgroundBitmap)
    }

    private fun setupGame() {
        // Créer le gestionnaire de vagues avec le chemin de la carte
        waveManager = WaveManager(
            map.path.first().first,
            map.path.first().second,
            map.path
        )

        // Initialiser le HUD
        hud.updateDisplay(player.money, player.health, 0, 0)
    }

    fun startGame() {
        gameStarted = true
        gameOver = false
        gameWon = false
        gameTime = 0L
        lastUpdateTime = System.currentTimeMillis()

        // Démarrer la première vague
        waveManager.startNextWave()
    }

    fun update() {
        if (!gameStarted || gameOver) return

        val currentTime = System.currentTimeMillis()
        val deltaTime = currentTime - lastUpdateTime
        lastUpdateTime = currentTime

        gameTime += deltaTime

        // Mise à jour du gestionnaire de vagues
        val waveCompleted = waveManager.update(deltaTime)

        // Si une vague est terminée, en démarrer une nouvelle
        if (waveCompleted) {
            if (waveManager.isGameCompleted()) {
                gameWon = true
                gameOver = true
            } else {
                waveManager.startNextWave()
            }
        }

        // Gérer les attaques des tourelles
        val activeEnemies = waveManager.getActiveEnemies()
        for (turret in map.placedTurrets) {
            for (enemy in activeEnemies) {
                if (turret.isInRange(enemy)) {
                    val killed = turret.attack(enemy)
                    if (killed) {
                        player.addMoney(enemy.calculateReward())
                    }
                }
            }
        }

        // Vérifier les ennemis qui ont atteint la fin du chemin
        val iterator = activeEnemies.iterator()
        while (iterator.hasNext()) {
            val enemy = iterator.next()
            if (enemy.isAtEnd()) {
                player.takeDamage(enemy.damage)
                waveManager.removeEnemy(enemy)

                if (player.isGameOver()) {
                    gameOver = true
                }
            }
        }

        // Mettre à jour l'affichage HUD
        hud.updateDisplay(
            player.money,
            player.health,
            waveManager.getCurrentWave(),
            gameTime
        )
    }

    fun draw(canvas: Canvas) {
        // Dessiner la carte
        val paint = Paint()
        map.draw(canvas, paint)

        // Dessiner les ennemis
        for (enemy in waveManager.getActiveEnemies()) {
            val enemyType = when (enemy) {
                is Bacterie -> "bacterie"
                is Virus -> "virus"
                is ParticuleFine -> "particule"
                else -> "particule"
            }

            // Dessiner un placeholder pour les ennemis
            paint.color = when (enemyType) {
                "bacterie" -> android.graphics.Color.RED
                "virus" -> android.graphics.Color.BLUE
                "particule" -> android.graphics.Color.GRAY
                else -> android.graphics.Color.BLACK
            }

            canvas.drawCircle(enemy.x, enemy.y, 20f, paint)

            // Dessiner la barre de vie
            paint.color = android.graphics.Color.RED
            canvas.drawRect(
                enemy.x - 20f,
                enemy.y - 30f,
                enemy.x + 20f,
                enemy.y - 25f,
                paint
            )

            paint.color = android.graphics.Color.GREEN
            val healthRatio = enemy.getHealth().toFloat() / when (enemyType) {
                "bacterie" -> 100f
                "virus" -> 50f
                "particule" -> 30f
                else -> 50f
            }
            canvas.drawRect(
                enemy.x - 20f,
                enemy.y - 30f,
                enemy.x - 20f + 40f * healthRatio,
                enemy.y - 25f,
                paint
            )
        }

        // Dessiner les tourelles
        for (turret in map.placedTurrets) {
            val turretType = when (turret) {
                is MacrophageTower -> {
                    paint.color = android.graphics.Color.GREEN
                    "macrophage"
                }
                is TouxTower -> {
                    paint.color = android.graphics.Color.BLUE
                    "toux"
                }
                is MucusTower -> {
                    paint.color = android.graphics.Color.YELLOW
                    "mucus"
                }
                else -> {
                    paint.color = android.graphics.Color.DKGRAY
                    "unknown"
                }
            }

            // Dessiner la tourelle
            canvas.drawRect(
                turret.x - 25f,
                turret.y - 25f,
                turret.x + 25f,
                turret.y + 25f,
                paint
            )

            // Dessiner le rayon d'attaque (uniquement si la tourelle est sélectionnée)
            if (isPositionSelected(turret.x, turret.y)) {
                paint.color = android.graphics.Color.argb(50, 255, 255, 255)
                canvas.drawCircle(turret.x, turret.y, turret.range.toFloat(), paint)
            }
        }

        // Dessiner le HUD
        hud.draw(canvas)

        // Dessiner l'indication de tourelle sélectionnée
        if (selectedTurretType != null) {
            hud.drawSelectedTurret(canvas, selectedTurretType!!)
        }

        // Si le jeu est terminé, afficher le message approprié
        if (gameOver) {
            val message = if (gameWon) "VICTOIRE !" else "DÉFAITE !"
            hud.drawGameMessage(canvas, message)
        }
    }

    fun handleTouch(event: MotionEvent): Boolean {
        if (gameOver) {
            // Si le jeu est terminé, recommencer en touchant l'écran
            if (event.action == MotionEvent.ACTION_UP) {
                resetGame()
                return true
            }
        } else if (!gameStarted) {
            // Si le jeu n'a pas commencé, le démarrer en touchant l'écran
            if (event.action == MotionEvent.ACTION_UP) {
                startGame()
                return true
            }
        } else {
            // Gestion des touches pendant le jeu
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    // Check if touch is in the tower selection area
                    if (event.y < 150) {
                        // Top of screen - select tower type
                        if (event.x < screenWidth / 3) {
                            selectTurretType("macrophage")
                        } else if (event.x < 2 * screenWidth / 3) {
                            selectTurretType("toux")
                        } else {
                            selectTurretType("mucus")
                        }
                        return true
                    }
                }

                MotionEvent.ACTION_UP -> {
                    // Vérifier si une tourelle est sélectionnée et placer une tourelle
                    if (selectedTurretType != null) {
                        val position = map.getClosestValidPosition(event.x, event.y)
                        if (position != null) {
                            placeTurret(selectedTurretType!!, position.first, position.second)
                            selectedTurretType = null
                            return true
                        }
                    }
                }
            }
        }

        return false
    }

    private fun selectTurretType(type: String) {
        selectedTurretType = type
    }

    private fun placeTurret(type: String, x: Float, y: Float) {
        val turret = when (type) {
            "macrophage" -> MacrophageTower(x, y)
            "toux" -> TouxTower(x, y)
            "mucus" -> MucusTower(x, y)
            else -> return
        }

        if (player.canBuyTurret(turret.cost)) {
            if (player.buyTurret(turret.cost)) {
                map.addTurret(turret)
            }
        }
    }

    private fun isPositionSelected(x: Float, y: Float): Boolean {
        for (turret in map.placedTurrets) {
            val dx = turret.x - x
            val dy = turret.y - y
            val distanceSquared = dx * dx + dy * dy

            if (distanceSquared < 10 * 10) {
                return true
            }
        }
        return false
    }

    fun resetGame() {
        player.health = 100
        player.money = 150
        map.placedTurrets.clear()
        waveManager.reset()
        selectedTurretType = null
        gameStarted = false
        gameOver = false
        gameWon = false
    }

    fun isGameOver(): Boolean {
        return gameOver
    }

    fun isGameWon(): Boolean {
        return gameWon
    }
}