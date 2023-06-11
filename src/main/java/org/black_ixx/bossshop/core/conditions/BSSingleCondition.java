package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

public record BSSingleCondition(BSConditionType type, String conditionType, String condition) implements BSCondition {
    public boolean meetsCondition(BSShopHolder holder, BSBuy buy, Player p) {
        return this.type.meetsCondition(holder, buy, p, this.conditionType, this.condition);
    }
}
