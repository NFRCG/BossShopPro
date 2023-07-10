package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.Condition;
import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

/**
 * Represents a group of conditions.
 * @param <T>
 */
@ConfigSerializable
public abstract class ComparableCondition<T extends Comparable<T>> implements Condition<Player> {
    protected final T value;
    protected final T upperValue;
    protected final Operator operator;

    @SafeVarargs
    ComparableCondition(final Operator operator, final T... value) {
        this.operator = operator;
        this.value = value[0];
        this.upperValue = this.operator == Operator.BETWEEN ? value[1] : null;
    }

    /**
     * Obtains a value from the provided player for comparison.
     *
     * @param player The player.
     */
    public abstract T getPlayerValue(final Player player);

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(final Player player) {
        T playerValue = this.getPlayerValue(player);
        int comparison = playerValue.compareTo(this.value);
        return switch (this.operator) {
            case EQUALS -> comparison == 0;
            case NOT_EQUALS -> comparison != 0;
            case OVER -> comparison > 0;
            case UNDER -> comparison < 0;
            case BETWEEN -> playerValue.compareTo(this.value) >= 0 && playerValue.compareTo(this.upperValue) <= 0;
        };
    }
}
