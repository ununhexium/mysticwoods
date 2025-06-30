package dev.c15u.gdx.mystic.system

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import com.github.quillraven.fleks.collection.compareEntity
import dev.c15u.gdx.mystic.component.ImageComponent
import dev.c15u.gdx.mystic.event.MapChangeEvent
import ktx.graphics.use
import ktx.tiled.forEachLayer

class RenderSystem(
    private val stage: Stage = inject()
) : EventListener, IteratingSystem(
    family = family { all(ImageComponent) },
    comparator = compareEntity { e1, e2 -> e1[ImageComponent].compareTo(e2[ImageComponent]) }
) {
    private val bgLayer = mutableListOf<TiledMapTileLayer>()
    private val fgLayer = mutableListOf<TiledMapTileLayer>()
    private val layers = listOf(bgLayer, fgLayer)

    private val mapRenderer = OrthogonalTiledMapRenderer(null, 1 / 16f, stage.batch)
    private val orthoCam = stage.camera as OrthographicCamera

    override fun onTick() {
        super.onTick()

        with(stage) {
            viewport.apply()

            mapRenderer.setView(orthoCam)

            layers.forEach { layer ->
                if (layer.isNotEmpty()) {
                    stage.batch.use(orthoCam.combined) {
                        bgLayer.forEach(mapRenderer::renderTileLayer)
                    }
                }
            }

            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {
        entity[ImageComponent].image.toFront()
    }

    override fun handle(event: Event): Boolean {
        return when (event) {
            is MapChangeEvent -> {
                layers.forEach { it.clear() }

                event.map.forEachLayer<TiledMapTileLayer> { layer ->
                    if (layer.name == "fg") {
                        fgLayer.add(layer)
                    } else {
                        bgLayer.add(layer)
                    }
                }

                true
            }

            else -> return false
        }
    }
}
