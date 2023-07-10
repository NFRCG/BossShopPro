package org.black_ixx.bossshop.core.conditions.types;

import net.milkbowl.vault.permission.Permission;
import org.black_ixx.bossshop.core.conditions.Condition;
import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.black_ixx.bossshop.managers.external.VaultHandler;
import org.bukkit.entity.Player;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import javax.inject.Inject;

//TODO: Guice configurate support in order to build this object.
@ConfigSerializable
public final class GroupCondition implements Condition<Player> {
    private final VaultHandler handler;
    private final Operator operator;
    private final String value;

    /**
     * Constructs the object.
     *
     * @param handler  VaultHandler used to check player groups.
     * @param operator The operator of the condition.
     * @param value    The value to compare against.
     */
    @Inject
    public GroupCondition(final VaultHandler handler, final Operator operator, final String value) {
        this.handler = handler;
        this.operator = operator;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSatisfiedBy(final Player player) {
        Permission perm = this.handler.getPermission();
        return switch (this.operator) {
            case OVER, UNDER, BETWEEN -> throw new IllegalArgumentException("Over/Under not valid for group condition!");
            case EQUALS -> perm.playerInGroup(player, this.value);
            case NOT_EQUALS -> !perm.playerInGroup(player, this.value);
        };
    }
}
