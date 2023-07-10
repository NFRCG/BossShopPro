package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.Condition;
import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class HandItemCondition implements Condition<Player> {
    private final Operator operator;
    private final Material value;

    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    public HandItemCondition(final Operator operator, final Material value) {
        this.operator = operator;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(Player player) {
        return switch (this.operator) {
            case EQUALS -> player.getInventory().getItemInMainHand().getType().equals(this.value);
            case NOT_EQUALS -> !player.getInventory().getItemInMainHand().getType().equals(this.value);
            default -> throw new IllegalArgumentException("Illegal condition set for HandItemCondition");
        };
    }
}
