package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.Condition;
import org.black_ixx.bossshop.core.conditions.operator.CompositeOperator;
import org.bukkit.entity.Player;

import java.util.List;

public record CompositeCondition(CompositeOperator operator, List<Condition<Player>> conditions) implements Condition<Player> {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(Player player) {
        return switch (this.operator) {
            case ALL -> this.conditions.stream().allMatch(x -> x.isSatisfiedBy(player));
            case ANY -> this.conditions.stream().anyMatch(x -> x.isSatisfiedBy(player));
            case NONE -> this.conditions.stream().noneMatch(x -> x.isSatisfiedBy(player));
            default -> throw new IllegalArgumentException("SomeCondition used incorrectly!");
        };
    }
}
