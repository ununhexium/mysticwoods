package dev.c15u.gdx.mystic.component

import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import ktx.math.vec2

data class SpawnComponent(
    var type: String = "",
    val location: Vector2 = vec2(),
) : Component<SpawnComponent> {
    override fun type() = SpawnComponent

    companion object : ComponentType<SpawnComponent>()
}
