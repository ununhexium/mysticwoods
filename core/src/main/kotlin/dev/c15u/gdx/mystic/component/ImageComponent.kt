package dev.c15u.gdx.mystic.component

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class ImageComponent(
    val image: Image
) : Component<ImageComponent>, Comparable<ImageComponent> {

    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(this.image.y)
        return if (yDiff != 0) yDiff else other.image.x.compareTo(this.image.x)
    }

    override fun type() = ImageComponent

    companion object : ComponentType<ImageComponent>()
}
