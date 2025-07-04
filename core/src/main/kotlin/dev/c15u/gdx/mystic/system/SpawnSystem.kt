package dev.c15u.gdx.mystic.system

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.MapObject
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType.DynamicBody
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import dev.c15u.gdx.mystic.MysticWoods.Companion.UNIT_SCALE
import dev.c15u.gdx.mystic.component.AnimationComponent
import dev.c15u.gdx.mystic.component.ImageComponent
import dev.c15u.gdx.mystic.component.MoveComponent
import dev.c15u.gdx.mystic.component.PhysicComponent.Companion.physicsComponentFromImage
import dev.c15u.gdx.mystic.component.PlayerComponent
import dev.c15u.gdx.mystic.component.SpawnComponent
import dev.c15u.gdx.mystic.component.SpawnConfig
import dev.c15u.gdx.mystic.event.MapChangeEvent
import dev.c15u.gdx.mystic.resource.AnimationRef
import dev.c15u.gdx.mystic.resource.PlayerAnimation
import dev.c15u.gdx.mystic.resource.SlimeAnimation
import ktx.box2d.box
import ktx.collections.isNotEmpty
import ktx.math.vec2
import ktx.tiled.layer
import ktx.tiled.type
import ktx.tiled.x
import ktx.tiled.y

private val MapObject.worldPosition: Vector2
    get() {
        return vec2(this.x * UNIT_SCALE, this.y * UNIT_SCALE)
    }

class SpawnSystem(
    val atlas: TextureAtlas = inject(),
    val phWorld: World = inject(),
) : EventListener, IteratingSystem(
    family = family { all(SpawnComponent) },
) {
    private val cachedCfgs = mutableMapOf<String, SpawnConfig>()
    private val cachedSizes = mutableMapOf<AnimationRef, Vector2>()

    override fun onTickEntity(entity: Entity) {
        with(entity[SpawnComponent]) {
            val cfg = spawnConfig(type)
            val relativeSize = size(cfg.model)

            world.entity {
                val image = Image().apply {
                    setPosition(location.x, location.y)
                    setScaling(Scaling.fill)
                    setSize(relativeSize.x, relativeSize.y)
                }
                it += ImageComponent(image)
                it += AnimationComponent().also {
                    it.nextAnimation(cfg.model)
                }

                it += physicsComponentFromImage(phWorld, image, DynamicBody) { phCmp, w, h ->
                    box(w, h) {
                        isSensor = false
                        friction = 0f
                    }
                }

                if (cfg.speed > 0f) {
                    it += MoveComponent(cfg.speed, vec2())
                }

                if (type == "Player") {
                    it += PlayerComponent()
                }
            }
        }
        world -= entity
    }

    private fun spawnConfig(type: String): SpawnConfig {
        return cachedCfgs.getOrPut(type) {
            when (type) {
                "Player" -> SpawnConfig(PlayerAnimation.idle_front, 7f)
                "Slime" -> SpawnConfig(SlimeAnimation.idle_front, 5f)
                else -> error("Unknown spawn type: $type")
            }
        }
    }

    private fun size(model: AnimationRef): Vector2 {
        return cachedSizes.getOrPut(model) {
            val regions = atlas.findRegions(model.id)
            require(regions.isNotEmpty()) {
                "No regions for animation of model ${model.id}"
            }

            val firstFrame = regions.first()
            vec2(
                x = firstFrame.originalWidth * UNIT_SCALE,
                y = firstFrame.originalHeight * UNIT_SCALE
            )
        }
    }

    override fun handle(event: Event?): Boolean {
        return when (event) {
            is MapChangeEvent -> {
                val entityLayer = event.map.layer("entities")
                entityLayer.objects.forEach { obj ->
                    val type = obj.type ?: error("No type set on object at (${obj.x} ${obj.y})")
                    world.entity {
                        it += SpawnComponent(type, obj.worldPosition)
                    }
                }
                true
            }

            else -> false
        }
    }


}
