package org.black_ixx.bossshop.core.rewards;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

public abstract class BSRewardTypeNumber extends BSRewardType {
    @Override
    public boolean isPlayerDependent(BSBuy buy, ClickType clicktype) {
        return super.isPlayerDependent(buy, clicktype) || (buy.getPriceType(clicktype) == BSPriceType.ItemAll && ClassManager.manager.getSettings().getItemAllShowFinalReward());
    }

    @Override
    public boolean supportsMultipliers() {
        return true;
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        giveReward(p, buy, reward, clickType, 1);
    }

    public abstract boolean isIntegerValue();

    public abstract void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType, int multiplier);
}
