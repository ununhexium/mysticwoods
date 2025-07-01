package dev.c15u.gdx.mystic

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import dev.c15u.gdx.mystic.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class MysticWoods : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        addScreen(GameScreen())
        setScreen<GameScreen>()
    }

    companion object {
        const val UNIT_SCALE = 1 / 16f
    }
}

