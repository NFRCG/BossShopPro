package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;

public final class WorldTimeCondition extends ComparableCondition<Long> {
    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public WorldTimeCondition(final Operator operator, final Long... value) {
        super(operator, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getPlayerValue(final Player player) {
        return player.getWorld().getTime();
    }
}
