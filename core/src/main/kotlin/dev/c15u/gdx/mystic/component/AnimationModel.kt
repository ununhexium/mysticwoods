package dev.c15u.gdx.mystic.component

enum class AnimationModel {
    PLAYER,
    SLIME,

    UNDEFINED,
    ;

    val atlasKey: String = this.name.lowercase()
}
