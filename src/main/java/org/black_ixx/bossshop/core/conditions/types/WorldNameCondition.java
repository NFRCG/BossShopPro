package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;

public final class WorldNameCondition extends ComparableCondition<String> {
    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public WorldNameCondition(final Operator operator, final String value) {
        super(operator, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlayerValue(final Player player) {
        return player.getWorld().getName();
    }
}
