package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

//TODO: maybe rename to List? Set implies unique conditions in the collection.
public class BSConditionSet implements BSCondition {
    private final List<BSCondition> conditions;

    public BSConditionSet() {
        this.conditions = new ArrayList<>();
    }

    public void addCondition(BSCondition c) {
        this.conditions.add(c);
    }

    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy buy, Player p) {
        return this.conditions.stream().allMatch(c -> c.meetsCondition(holder, buy, p));
    }

    public boolean isEmpty() {
        return this.conditions.isEmpty();
    }

    public List<BSCondition> getConditions() {
        return this.conditions;
    }
}
