package dev.c15u.gdx.mystic.resource

enum class SlimeAnimation(override val id: String) : AnimationRef {
    death("slime/death"),
    hit_back("slime/hit_back"),
    hit_front("slime/hit_front"),
    hit_right("slime/hit_right"),
    idle_back("slime/idle_back"),
    idle_front("slime/idle_front"),
    move_front("slime/move_front"),
    idle_right("slime/idle_right"),
    jump_back("slime/jump_back"),
    jump_front("slime/jump_front"),
    jump_right("slime/jump_right"),
    move_back("slime/move_back"),
    move_right("slime/move_right"),
}
