package nukleer.mindustry.nohit

import arc.Core
import mindustry.Vars
import mindustry.game.*
import mindustry.gen.*
import mindustry.graphics.BlockRenderer

class NoHitMod : mindustry.mod.Mod() {
    val forceHealth = 1.4E-45f
    val targetTeam = Team.sharded

    fun<T> verifyEntity(x: T?) where T: Teamc, T: Healthc {
        if (x == null || x.team() != targetTeam) return
        val newHealth = kotlin.math.min(x.health(), forceHealth)
        x.health(newHealth)
    }

    init {
        // remove cracks texture since all blocks
        // are "severely damaged" with this mod
        arc.Events.on(EventType.ClientLoadEvent::class.java) {
            val emptySprite = Core.atlas.find("nohit-challenge-mod-empty")
            if (emptySprite.width != 1) throw Exception("'empty' not found")
            for (size in 1..BlockRenderer.maxCrackSize) {
                for (i in 0 until BlockRenderer.crackRegions) {
                    Vars.renderer.blocks.cracks[size - 1][i] = emptySprite
                }
            }
        }

        // this might not be the most performant way, but it deals with
        // any unexpected interaction e.g. some obscure healing from mods
        arc.Events.run(EventType.Trigger.update) {
            Groups.unit.forEach { verifyEntity(it) }
            Vars.world.tiles.forEach { verifyEntity(it.build) }
        }
    }
}
