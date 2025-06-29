package dev.c15u.gdx.mystic.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class AnimationComponent(
  var atlasKey: AnimationModel = AnimationModel.SLIME,
  var stateTime: Float = 0f,
  var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
) : Component<AnimationComponent> {
  lateinit var animation: Animation<TextureRegionDrawable>
  var nextAnimation: String? = null

  fun nextAnimation(animationModel: AnimationModel, type: PlayerAnimationType) {
    this.atlasKey = animationModel
    nextAnimation = "${animationModel.atlasKey}/${type.atlasKey}"
  }

  override fun type() = AnimationComponent

  companion object : ComponentType<AnimationComponent>()
}
