package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

//TODO: determine what level of precision is needed.
@ConfigSerializable
public final class TimeCondition extends ComparableCondition<Double> {
    private final ChronoField type;

    TimeCondition(final Operator operator, final ChronoField type, final Double... value) {
        super(operator, value);
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getPlayerValue(Player player) {
        return (double) LocalDateTime.now().get(this.type);
    }
}
