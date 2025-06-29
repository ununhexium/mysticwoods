package dev.c15u.gdx.mystic.resource

interface AnimationRef {
    val id: String

    companion object {
        val NONE = object : AnimationRef {
            override val id: String
                get() = ""
        }
    }
}
