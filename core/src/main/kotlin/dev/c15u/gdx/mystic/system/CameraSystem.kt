package dev.c15u.gdx.mystic.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import dev.c15u.gdx.mystic.component.ImageComponent
import dev.c15u.gdx.mystic.component.PlayerComponent
import dev.c15u.gdx.mystic.event.MapChangeEvent
import ktx.tiled.height
import ktx.tiled.width

class CameraSystem(
    private val stage: Stage = inject(),
) : EventListener, IteratingSystem(
    family = family { all(ImageComponent, PlayerComponent) }
) {
    private var maxW = 0f
    private var maxH = 0f
    private val camera = stage.camera as OrthographicCamera

    override fun onTickEntity(entity: Entity) {
        val image = entity[ImageComponent].image
        val viewW = camera.viewportWidth * 0.5f
        val viewH = camera.viewportHeight * 0.5f

        camera.position.set(
            image.x.coerceIn(viewW, maxW - viewW),
            image.y.coerceIn(viewH, maxH - viewH),
            camera.position.z
        )
    }

    override fun handle(event: Event?): Boolean {
        if(event is MapChangeEvent){
            maxW = event.map.width.toFloat()
            maxH = event.map.height.toFloat()
            return true
        }

        return false
    }
}
