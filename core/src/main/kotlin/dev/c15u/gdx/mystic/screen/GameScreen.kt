package dev.c15u.gdx.mystic.screen

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Disposable
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.configureWorld
import dev.c15u.gdx.mystic.component.ImageComponent
import dev.c15u.gdx.mystic.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen : KtxScreen {

    private val disposables = ArrayList<Disposable>()

    private val textureAtlas = TextureAtlas("assets/graphics/gameObjects.atlas").alsoDispose()
    private val stage = Stage(ExtendViewport(16f, 9f)).alsoDispose()
    private val world = configureWorld {
        injectables {
            add(stage)
        }

        onAddEntity(ImageComponent.onAdd)


        systems {
            add(RenderSystem(stage))
        }
    }

    companion object {
        val log = logger<GameScreen>()
    }

    override fun show() {
        world.entity {
            it += ImageComponent(
                Image(TextureRegion(textureAtlas.findRegion("player"), 0, 0, 48, 48)).apply {
                    setSize(4f, 4f)
                }
            )
        }
        world.entity {
            it += ImageComponent(
                Image(TextureRegion(textureAtlas.findRegion("slime"), 0, 0, 32, 32)).apply {
                    setSize(4f, 4f)
                    setPosition(12f, 1f)
                }
            )
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        with(stage) {
            act(delta)
            draw()
        }
    }

    override fun dispose() {
        disposables.forEach { it.disposeSafely() }
        world.dispose()
    }

    private fun <T : Disposable> T.alsoDispose(): T {
        disposables.add(this)
        return this
    }

}
