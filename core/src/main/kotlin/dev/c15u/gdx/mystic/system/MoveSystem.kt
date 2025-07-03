package dev.c15u.gdx.mystic.system

import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import dev.c15u.gdx.mystic.component.MoveComponent
import dev.c15u.gdx.mystic.component.PhysicComponent
import ktx.math.minus
import ktx.math.times

class MoveSystem(

) : IteratingSystem(
    family = family { all(MoveComponent, PhysicComponent) },
) {
    companion object {
        val EPSILON = 0.00001f
    }

    override fun onTickEntity(entity: Entity) {
        val moveCmp = entity[MoveComponent]
        val physicCmp = entity[PhysicComponent]

        val mass = physicCmp.body.mass
        val vel = physicCmp.body.linearVelocity

        if (moveCmp.direction.len() < EPSILON) {
            physicCmp.impulse.set(vel * -mass)
            return
        } else {
            physicCmp.impulse.set((moveCmp.direction * moveCmp.speed - vel) * mass)
        }

    }
}
