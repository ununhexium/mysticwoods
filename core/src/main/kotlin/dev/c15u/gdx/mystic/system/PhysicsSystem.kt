package dev.c15u.gdx.mystic.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.Fixed
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import dev.c15u.gdx.mystic.component.ImageComponent
import dev.c15u.gdx.mystic.component.PhysicComponent
import ktx.log.logger
import ktx.math.component1
import ktx.math.component2
import com.badlogic.gdx.physics.box2d.World as PhWorld

class PhysicsSystem(
    // TODO: try inject
    val phWorld: PhWorld
) : IteratingSystem(
    family = family { all(PhysicComponent, ImageComponent) },
    interval = Fixed(1 / 60f)
) {
    override fun onUpdate() {
        if (phWorld.autoClearForces) {
            log.error { "autoClearForces must be set to false to guarantee correct physics simulation" }
            phWorld.autoClearForces = false
        }
    }

    override fun onTick() {
        phWorld.step(world.deltaTime, 6, 2)
    }

    override fun onTickEntity(entity: Entity) {
        val phys = entity[PhysicComponent]
        val imag = entity[ImageComponent]

        val (bodyX, bodyY) = phys.body.position
        imag.image.run {
            setPosition(bodyX - width / 2, bodyY - height / 2)
        }
    }

    companion object {
        private val log = logger<PhysicsSystem>()
    }
}
