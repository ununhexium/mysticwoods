package dev.c15u.gdx.mystic.component

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.EntityCreateContext
import com.github.quillraven.fleks.World
import ktx.box2d.BodyDefinition
import ktx.box2d.body
import ktx.math.vec2
import com.badlogic.gdx.physics.box2d.World as PhWorld

class PhysicComponent() : Component<PhysicComponent> {
    val prevPos = vec2()
    val impulse = vec2()
    lateinit var body: Body

    override fun World.onAdd(entity:Entity) {
        entity[PhysicComponent].body.userData = entity
    }

    override fun World.onRemove(entity: Entity) {
        val body = entity.getOrNull(PhysicComponent)?.body ?: return
        body.world.destroyBody(body)
        body.userData = null
    }

    override fun type() = PhysicComponent

    companion object : ComponentType<PhysicComponent>() {
        fun EntityCreateContext.physicsComponentFromImage(
            world: PhWorld,
            image: Image,
            bodyType: BodyType,
            action: BodyDefinition.(PhysicComponent, Float, Float) -> Unit
        ): PhysicComponent {
            val x = image.x
            val y = image.y
            val w = image.width
            val h = image.height


            return PhysicComponent().apply {
                body = world.body(bodyType) {
                    position.set(x + w * 0.5f, y + h * 0.5f)
                    fixedRotation = true
                    allowSleep = false
                    this.action(this@apply, w, h)
                }
            }
        }
    }
}
