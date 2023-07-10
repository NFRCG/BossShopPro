package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.Condition;
import org.bukkit.entity.Player;

public record SomeCondition(CompositeCondition specification, int threshold) implements Condition<Player> {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(final Player player) {
        return this.specification.conditions().stream().filter(x -> x.isSatisfiedBy(player)).count() >= this.threshold;
    }
}
