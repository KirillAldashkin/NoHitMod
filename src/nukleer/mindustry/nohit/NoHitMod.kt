package nukleer.mindustry.nohit

import mindustry.*
import mindustry.game.*
import mindustry.gen.*

class NoHitMod : mindustry.mod.Mod() {
    val forceHealth = 1.4E-45f
    val targetTeam = Team.sharded

    fun<T> verifyEntity(x: T?) where T: Teamc, T: Healthc {
        if (x == null || x.team() != targetTeam) return
        val newHealth = kotlin.math.min(x.health(), forceHealth)

        // make one-shot
        x.health(newHealth)
        if (x is Shieldc) x.shield(0f)
        // also set max health to remove annoying
        // block crack texture and unit cell blink
        x.maxHealth(forceHealth)
    }

    init {
        // this might not be the most performant way, but it deals with
        // any unexpected interaction e.g. some obscure healing from mods
        arc.Events.run(EventType.Trigger.update) {
            Groups.unit.forEach { verifyEntity(it) }
            Vars.world.tiles.forEach { verifyEntity(it.build) }
        }
    }
}
