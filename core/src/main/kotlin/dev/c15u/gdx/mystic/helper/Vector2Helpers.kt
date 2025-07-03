package dev.c15u.gdx.mystic.helper

import com.badlogic.gdx.math.Vector2
import ktx.math.vec2

object Vector2Helpers {

    fun Vector2.normalized(): Vector2 {
        val length = this.len()
        return if (length != 0f) {
            vec2(this.x / length, this.y / length)
        } else this
    }

}
