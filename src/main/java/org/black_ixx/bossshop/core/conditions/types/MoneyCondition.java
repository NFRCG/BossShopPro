package org.black_ixx.bossshop.core.conditions.types;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.black_ixx.bossshop.managers.external.VaultHandler;
import org.bukkit.entity.Player;

import javax.inject.Inject;

//TODO: Guice configurate support in order to build this object.
public final class MoneyCondition extends ComparableCondition<Double> {
    private final VaultHandler handler;

    @Inject
    public MoneyCondition(final VaultHandler handler, final Operator operator, final Double... value) {
        super(operator, value);
        this.handler = handler;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getPlayerValue(final Player player) {
        return this.handler.getEconomy().getBalance(player);
    }
}
