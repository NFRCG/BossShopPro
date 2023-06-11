package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BSConditionSet implements BSCondition {

    private List<BSCondition> conditions;


    public BSConditionSet() {
        this.conditions = new ArrayList<>();
    }

    public BSConditionSet(List<BSCondition> conditions) {
        this.conditions = conditions;
    }


    public void addCondition(BSCondition c) {
        this.conditions.add(c);
    }

    @Override
    public boolean meetsCondition(BSShopHolder holder, BSBuy buy, Player p) {
        for (BSCondition c : this.conditions) {
            if (!c.meetsCondition(holder, buy, p)) {
                return false;
            }
        }
        return true;
    }


    public boolean isEmpty() {
        return this.conditions.isEmpty();
    }

    public List<BSCondition> getConditions() {
        return this.conditions;
    }


}
