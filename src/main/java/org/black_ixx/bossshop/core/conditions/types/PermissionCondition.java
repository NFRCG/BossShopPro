package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class PermissionCondition extends ComparableCondition<Boolean> {
    private final String perm;

    public PermissionCondition(final Operator operator, final String perm, final Boolean value) {
        super(operator, value);
        this.perm = perm;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getPlayerValue(final Player player) {
        return player.hasPermission(this.perm);
    }
}
