package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class LightLevelCondition extends ComparableCondition<Byte> {
    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public LightLevelCondition(final Operator operator, final Byte... value) {
        super(operator, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte getPlayerValue(final Player player) {
        return player.getLocation().getBlock().getLightLevel();
    }
}
