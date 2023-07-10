package org.black_ixx.bossshop.core.conditions;

import org.bukkit.entity.Player;

/**
 * Represents a condition which must be passed in order for some process to proceed.
 *
 * @param <T> The viewer. A Bukkit player.
 */
public interface Condition<T extends Player> {
    /**
     * Determines if the provided Player satisfies the bounds of the specification.
     *
     * @param t The player.
     * @return if the specification is satisfied.
     */
    boolean isSatisfiedBy(T t);
}
