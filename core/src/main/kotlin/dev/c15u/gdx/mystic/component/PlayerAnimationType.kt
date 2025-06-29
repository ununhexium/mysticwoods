package dev.c15u.gdx.mystic.component

enum class PlayerAnimationType {
    IDLE_FRONT,
    IDLE_DIAG,
    IDLE_BACK,
    RUN_FRONT,
    RUN_DIAG,
    RUN_BACK,
    ATTACK_FRONT,
    ATTACK_DIAG,
    ATTACK_BACK,
    DEATH,
    ;

    val atlasKey = this.toString().lowercase()
}
