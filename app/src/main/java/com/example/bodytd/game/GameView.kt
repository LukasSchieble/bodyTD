package com.bodyTD.game

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView

class GameView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : SurfaceView(context, attrs, defStyleAttr), SurfaceHolder.Callback {

    private var gameThread: GameThread? = null
    private lateinit var gameManager: GameManager

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameManager = GameManager.getInstance(context, width, height)
        gameThread = GameThread(holder, this)
        gameThread?.setRunning(true)
        gameThread?.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Gérer les changements de surface si nécessaire
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameThread?.setRunning(false)
        while (retry) {
            try {
                gameThread?.join()
                retry = false
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gameManager.handleTouch(event) || super.onTouchEvent(event)
    }

    fun update() {
        gameManager.update()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        gameManager.draw(canvas)
    }

    private class GameThread(
        private val surfaceHolder: SurfaceHolder,
        private val gameView: GameView
    ) : Thread() {
        private var running = false
        private val targetFPS = 60
        private val targetTime = (1000 / targetFPS).toLong()

        fun setRunning(running: Boolean) {
            this.running = running
        }

        override fun run() {
            var startTime: Long
            var timeMillis: Long
            var waitTime: Long

            while (running) {
                startTime = System.currentTimeMillis()
                var canvas: Canvas? = null

                try {
                    canvas = surfaceHolder.lockCanvas()
                    synchronized(surfaceHolder) {
                        gameView.update()
                        if (canvas != null) {
                            gameView.draw(canvas)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    try {
                        canvas?.let { surfaceHolder.unlockCanvasAndPost(it) }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                timeMillis = System.currentTimeMillis() - startTime
                waitTime = targetTime - timeMillis

                try {
                    if (waitTime > 0) {
                        sleep(waitTime)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}