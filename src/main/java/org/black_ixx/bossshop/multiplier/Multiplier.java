package org.black_ixx.bossshop.multiplier;

import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record Multiplier(MultiplierType type, String permission, double multiplier) {

    public double apply(final Player player, final double d) {
        if (!this.hasPerm(player)) {
            return d;
        }
        //TODO: implement after conditions/prices/rewards re-work.
        //
        return 1;
    }

    public boolean hasPerm(final Player p) {
        return p.hasPermission(this.permission);
    }
}
