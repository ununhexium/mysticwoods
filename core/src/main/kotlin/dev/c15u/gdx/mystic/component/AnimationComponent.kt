package dev.c15u.gdx.mystic.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType
import dev.c15u.gdx.mystic.resource.AnimationRef

data class AnimationComponent(
  var atlasKey: AnimationRef = AnimationRef.NONE,
  var stateTime: Float = 0f,
  var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) : Component<AnimationComponent> {
  lateinit var animation: Animation<TextureRegionDrawable>
  var nextAnimation: String? = null

  fun nextAnimation(animationRef: AnimationRef) {
    this.atlasKey = animationRef
    nextAnimation = animationRef.id
  }

  override fun type() = AnimationComponent

  companion object : ComponentType<AnimationComponent>()
}
