package dev.c15u.gdx.mystic.system

import com.badlogic.gdx.math.MathUtils
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import com.github.quillraven.fleks.World.Companion.inject
import dev.c15u.gdx.mystic.component.ImageComponent
import dev.c15u.gdx.mystic.component.PhysicComponent
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import com.badlogic.gdx.physics.box2d.World as PhWorld

class PhysicsSystem(
    val phWorld: PhWorld = inject()
) : IteratingSystem(
    family = family { all(PhysicComponent, ImageComponent) },
    interval = Fixed(1 / 20f)
) {
    override fun onUpdate() {
        if (phWorld.autoClearForces) {
            log.error { "autoClearForces must be set to false to guarantee correct physics simulation" }
            phWorld.autoClearForces = false
        }
        super.onUpdate()
        phWorld.clearForces()
    }

    override fun onTick() {
        super.onTick()
        phWorld.step(deltaTime, 6, 2)
    }

    override fun onTickEntity(entity: Entity) {
        val physicCmp = entity[PhysicComponent]

        physicCmp.prevPos.set(physicCmp.body.position)

//        log.debug { "Impulse ${physicCmp.impulse}" }

        if (!physicCmp.impulse.isZero) {
            physicCmp.body.applyLinearImpulse(physicCmp.impulse, physicCmp.body.worldCenter, true)
            physicCmp.impulse.setZero()
        }
    }

    override fun onAlphaEntity(entity: Entity, alpha: Float) {
        val physicCmp = entity[PhysicComponent]
        val imageCmp = entity[ImageComponent]

        imageCmp.image.run {
            val (prevX, prevY) = physicCmp.prevPos
            val (bodyX, bodyY) = physicCmp.body.position

            setPosition(
                MathUtils.lerp(prevX, bodyX, alpha) - width * 0.5f,
                MathUtils.lerp(prevY, bodyY, alpha) - height * 0.5f,
            )
        }
    }

    companion object {
        private val log = logger<PhysicsSystem>()
    }
}
