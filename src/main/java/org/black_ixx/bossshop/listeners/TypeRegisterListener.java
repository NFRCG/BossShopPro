package org.black_ixx.bossshop.listeners;

import org.black_ixx.bossshop.events.BSRegisterTypesEvent;
import org.black_ixx.bossshop.core.prices.BSPriceTypeAnd;
import org.black_ixx.bossshop.core.prices.BSPriceTypeOr;
import org.black_ixx.bossshop.core.rewards.BSRewardTypeAnd;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TypeRegisterListener implements Listener {
    @EventHandler
    public void onCreate(final BSRegisterTypesEvent event) {
        new BSPriceTypeAnd().register();
        new BSPriceTypeOr().register();
        new BSRewardTypeAnd().register();
    }

}
