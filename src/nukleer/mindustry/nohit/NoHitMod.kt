package nukleer.mindustry.nohit

import mindustry.game.*
import mindustry.gen.*

class NoHitMod : mindustry.mod.Mod() {
    val forceHealth = 1.4E-45f
    val targetTeam = Team.sharded

    fun<T> verifyEntity(x: T?) where T: Teamc, T: Healthc {
        if (x == null || x.team() != targetTeam) return
        val newHealth = kotlin.math.min(x.health(), forceHealth)
        x.health(newHealth)
    }

    init {
        // this might not be the most performant way, but it deals with
        // any unexpected interaction e.g. some obscure healing from mods
        arc.Events.run(EventType.Trigger.update) {
            Groups.unit.forEach { verifyEntity(it) }
            Groups.build.forEach { verifyEntity(it) }
        }
    }
}
