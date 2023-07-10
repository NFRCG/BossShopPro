package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.Condition;
import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class ItemCondition implements Condition<Player> {
    private final Operator operator;
    private final Material value;

    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public ItemCondition(final Operator operator, final Material value) {
        this.operator = operator;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(final Player player) {
        return switch (this.operator) {
            case EQUALS -> player.getInventory().contains(this.value);
            case NOT_EQUALS -> !player.getInventory().contains(this.value);
            default -> throw new IllegalArgumentException("Illegal condition set for ItemCondition");
        };
    }
}
