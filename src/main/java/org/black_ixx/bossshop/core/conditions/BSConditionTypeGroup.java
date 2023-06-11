package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;

public class BSConditionTypeGroup extends BSConditionTypeMatch {
    @Override
    public boolean matches(Player p, String singleCondition) {
        return ClassManager.manager.getVaultHandler().getPermission().playerInGroup(p, singleCondition);
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"group", "permissionsgroup"};
    }

    @Override
    public void enableType() {
        //TODO: move to constructor?
        ClassManager.manager.getSettings().setVaultEnabled(true);
        ClassManager.manager.getSettings().setPermissionsEnabled(true);
    }
}
