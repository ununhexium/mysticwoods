package dev.c15u.gdx.mystic

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class Foo {

    @Test
    fun `rename`() {
        val wd = "/home/uuh/dev/ununhexium/libgdx/mysticwoods/assets_raw/sprites/characters/player"

        val inputIndexes = 0..5
        val outputIndexes = 0..5
        val name = "idle_h_"

        (inputIndexes.zip(outputIndexes)).forEach { (i, o) ->
            Files.copy(
                Paths.get(wd, "frame_" + "%02d".format(i)),
                Paths.get(wd, "renamed", name + "%02d".format(o))
            )
        }
    }
}
