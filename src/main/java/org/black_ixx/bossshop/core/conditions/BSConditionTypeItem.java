package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class BSConditionTypeItem extends BSConditionTypeMatch {
    @Override
    public boolean matches(Player p, String single_condition) {
        return p.getInventory().contains(Material.valueOf(single_condition));
    }

    @Override
    public boolean dependsOnPlayer() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"item", "inventoryitem", "hasitem", "material"};
    }

    @Override
    public void enableType() {
    }
}
