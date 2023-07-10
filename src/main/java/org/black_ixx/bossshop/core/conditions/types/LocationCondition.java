package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;

public class LocationCondition extends ComparableCondition<Double> {
    private final char pos;

    public LocationCondition(final Operator operator, final char pos, final Double... value) {
        super(operator, value);
        this.pos = pos;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getPlayerValue(final Player player) {
        return switch (this.pos) {
            case 'x', 'X' -> player.getLocation().getX();
            case 'y', 'Y' -> player.getLocation().getY();
            case 'z', 'Z' -> player.getLocation().getZ();
            default -> throw new IllegalArgumentException("Illegal location char passed to condition!");
        };
    }
}
