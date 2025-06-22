package dev.c15u.gdx.mystic.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.configureWorld
import dev.c15u.gdx.mystic.component.ImageComponent
import dev.c15u.gdx.mystic.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen : KtxScreen {

    private val texture = Texture("assets/graphics/player.png")
    private val stage = Stage(ExtendViewport(16f, 9f))
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
            it += ImageComponent(Image(texture).apply { setSize(4f, 4f) })
        }

        stage.addActor(
            Image(texture).apply {
                setPosition(1f, 1f)
                setSize(1f, 1f)
                setScaling(Scaling.fill)
            }
        )
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
        stage.disposeSafely()
        texture.disposeSafely()
        world.dispose()
    }
}
