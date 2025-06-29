package dev.c15u.gdx.mystic.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World.Companion.family
import dev.c15u.gdx.mystic.component.AnimationComponent
import dev.c15u.gdx.mystic.component.ImageComponent
import ktx.app.gdxError
import ktx.collections.map
import ktx.log.logger

class AnimationSystem(
    private val textureAtlas: TextureAtlas
) : IteratingSystem(
    family = family { all(AnimationComponent, ImageComponent) },
) {

    private val cache = mutableMapOf<String, Animation<TextureRegionDrawable>>()

    override fun onTickEntity(entity: Entity) {
        val anim = entity[AnimationComponent]

        log.info { "Tick anim" }

        val nextAnimation = anim.nextAnimation
        if (nextAnimation == null) {
            anim.stateTime += deltaTime
        } else {
            anim.animation = animation(nextAnimation)
            anim.stateTime = 0f
            anim.nextAnimation = null
        }

        anim.animation.playMode = anim.playMode
        entity[ImageComponent].image.drawable = anim.animation.getKeyFrame(anim.stateTime)
    }

    private fun animation(animationKeyPath: String): Animation<TextureRegionDrawable> {
        return cache.getOrPut(animationKeyPath) {
            log.debug { "New animation created for $animationKeyPath" }
            val regions = textureAtlas.findRegions(animationKeyPath)
            if (regions.isEmpty) {
                gdxError("No texture region for $animationKeyPath")
            }

            Animation(DEFAULT_FRAME_DURATION, regions.map { TextureRegionDrawable(it) })
        }
    }

    companion object {
        val log = logger<AnimationSystem>()
        private val DEFAULT_FRAME_DURATION = 1 / 8f
    }
}
