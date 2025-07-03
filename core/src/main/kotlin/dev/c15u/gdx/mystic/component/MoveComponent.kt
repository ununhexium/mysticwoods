package dev.c15u.gdx.mystic.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class MoveComponent(
    var speed: Float,
    var direction: Vector2
) : Component<MoveComponent> {
    override fun type() = MoveComponent

    companion object : ComponentType<MoveComponent>()
}
