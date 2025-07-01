package dev.c15u.gdx.mystic.screen

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.configureWorld
import dev.c15u.gdx.mystic.event.MapChangeEvent
import dev.c15u.gdx.mystic.system.AnimationSystem
import dev.c15u.gdx.mystic.system.EntitySpawnSystem
import dev.c15u.gdx.mystic.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

private fun Stage.fire(event: Event): Boolean {
    return this.root.fire(event)
}

class GameScreen : KtxScreen {

    private val disposables = ArrayList<Disposable>()

    private val textureAtlas = TextureAtlas("assets/graphics/gameObjects.atlas").alsoDispose()
    private val stage = Stage(ExtendViewport(16f, 9f)).alsoDispose()
    private var currentMap: TiledMap? = null

    private val world = configureWorld {
        injectables {
            add(stage)
            add(textureAtlas)
        }

        systems {
            add(EntitySpawnSystem(stage, textureAtlas))
            add(AnimationSystem(textureAtlas))
            add(RenderSystem(stage))
        }
    }

    companion object {
        val log = logger<GameScreen>()
    }

    override fun show() {

        world.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }
        currentMap = TmxMapLoader().load("maps/map1.tmx")
        currentMap?.let {
            stage.fire(MapChangeEvent(it))
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        with(stage) {
            world.update(delta)
            act(delta)
            draw()
        }
    }

    override fun dispose() {
        disposables.forEach { it.disposeSafely() }
        currentMap.disposeSafely()
        world.dispose()
    }

    private fun <T : Disposable> T.alsoDispose(): T {
        disposables.add(this)
        return this
    }

}
