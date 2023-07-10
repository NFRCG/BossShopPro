package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class WorldWeatherCondition extends ComparableCondition<Boolean> {
    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public WorldWeatherCondition(Operator operator, Boolean value) {
        super(operator, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean getPlayerValue(Player player) {
        return player.getWorld().hasStorm();
    }
}
