package org.black_ixx.bossshop.core.conditions.types;

import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.bukkit.entity.Player;

public class PlaceholderCondition extends ComparableCondition<String> {
    private final String placeholder;

    /**
     * Constructs the object.
     *
     * @param operator The operator of the condition.
     * @param value    Expected parsed value of the placeholder.
     */
    public PlaceholderCondition(final Operator operator, final String placeholder, final String value) {
        super(operator, value);
        this.placeholder = placeholder;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPlayerValue(final Player player) {
        return PlaceholderAPI.setPlaceholders(player, this.placeholder);
    }
}
