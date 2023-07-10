package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class HealthCondition extends ComparableCondition<Double> {

    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public HealthCondition(final Operator operator, final Double... value) {
        super(operator, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getPlayerValue(final Player player) {
        return player.getHealth();
    }
}
