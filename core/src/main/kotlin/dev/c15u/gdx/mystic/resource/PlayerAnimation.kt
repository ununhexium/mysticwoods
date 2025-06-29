package dev.c15u.gdx.mystic.resource

enum class PlayerAnimation(override val id: String) : AnimationRef {
    attack_back("player/attack_back"),
    attack_diag("player/attack_diag"),
    attack_front("player/attack_front"),
    death("player/death"),
    idle_back("player/idle_back"),
    idle_front("player/idle_front"),
    idle_right("player/idle_right"),
    walk_back("player/walk_back"),
    walk_front("player/walk_front"),
    walk_right("player/walk_right"),
}
