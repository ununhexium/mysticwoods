package dev.c15u.gdx.mystic.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import dev.c15u.gdx.mystic.resource.AnimationRef
import ktx.math.vec2

data class SpawnConfig(
    val model: AnimationRef,
    val speed: Float = 0f,
)

data class SpawnComponent(
    var type: String = "",
    val location: Vector2 = vec2(),
) : Component<SpawnComponent> {
    override fun type() = SpawnComponent

    companion object : ComponentType<SpawnComponent>()
}
