package nukleer.mindustry.nohit

import mindustry.*
import mindustry.game.*
import mindustry.gen.*
import mindustry.world.blocks.defense.*

class NoHitMod : mindustry.mod.Mod() {
    val targetTeam = Team.sharded

    fun<T> verifyEntity(x: T?) where T: Teamc, T: Healthc {
        if (x == null || x.team() != targetTeam) return
        val newHealth = kotlin.math.min(x.health(), epsilon)

        // make one-shot
        x.health(newHealth)
        // also set max health to remove annoying
        // block crack texture and unit cell blink
        x.maxHealth(epsilon)

        if (x is Shieldc) {
            // minimal HP of unit's force fields
            val newShield = kotlin.math.min(x.shield(), epsilon)
            x.shield(newShield)
            // shields can partially mitigate damage
            x.armor(0f)
        }
        // make force fields from buildings almost broken
        if (x is ForceProjector.ForceBuild && !x.broken) {
            val block = x.block as ForceProjector
            val newBuildup = (block.shieldHealth + block.phaseShieldBoost * x.phaseHeat).bitDec()
            x.buildup = kotlin.math.max(x.buildup, newBuildup)
        }
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
