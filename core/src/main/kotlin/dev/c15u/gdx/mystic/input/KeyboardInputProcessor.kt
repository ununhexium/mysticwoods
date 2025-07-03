package dev.c15u.gdx.mystic.input

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector2
import com.github.quillraven.fleks.World
import dev.c15u.gdx.mystic.component.MoveComponent
import dev.c15u.gdx.mystic.component.PlayerComponent
import dev.c15u.gdx.mystic.helper.Vector2Helpers.normalized
import ktx.app.KtxInputAdapter
import ktx.log.logger
import ktx.math.vec2

class KeyboardInputProcessor(
    private val world: World
) : KtxInputAdapter {

    private var direction: Vector2 = vec2()
    private val playerEntities = world.family { all(PlayerComponent) }

    init {
        Gdx.input.inputProcessor = this
    }

    companion object {
        val log = logger<KeyboardInputProcessor>()
    }

    private fun Int.isMovementKey() =
        this == Keys.UP || this == Keys.DOWN || this == Keys.LEFT || this == Keys.RIGHT

    private fun updatePlayerMovement() {
        playerEntities.forEach { entity ->
            entity[MoveComponent].direction = direction.normalized()
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        log.debug { "Key down: $keycode" }
        if (keycode.isMovementKey()) {
            direction = when (keycode) {
                Keys.UP -> vec2(direction.x, 1f)
                Keys.DOWN -> vec2(direction.x, -1f)
                Keys.LEFT -> vec2(-1f, direction.y)
                Keys.RIGHT -> vec2(1f, direction.y)
                else -> error("Unknown keycode: $keycode")
            }
            updatePlayerMovement()
            return true
        }
        return false
    }

    override fun keyUp(keycode: Int): Boolean {
        log.debug { "Key up: $keycode" }
        if (keycode.isMovementKey()) {
            direction = when (keycode) {
                Keys.UP -> vec2(direction.x, 0f)
                Keys.DOWN -> vec2(direction.x, 0f)
                Keys.LEFT -> vec2(0f, direction.y)
                Keys.RIGHT -> vec2(0f, direction.y)
                else -> error("Unknown keycode: $keycode")
            }
            updatePlayerMovement()
            return true
        }
        return false
    }
}
