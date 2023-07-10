package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class HungerCondition extends ComparableCondition<Integer> {
    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public HungerCondition(final Operator operator, final Integer... value) {
        super(operator, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer getPlayerValue(final Player player) {
        return player.getFoodLevel();
    }
}
