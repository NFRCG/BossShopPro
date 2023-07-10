package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSMultiplier;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.MessageHandler;
import org.black_ixx.bossshop.managers.item.ItemStackChecker;
import org.black_ixx.bossshop.misc.MathTools;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MultiplierHandler {
    private final ItemStackChecker itemStackChecker;
    private final MessageHandler messageHandler;
    private final Set<BSMultiplier> multipliers;

    @Inject
    public MultiplierHandler(BossShop plugin, ItemStackChecker itemStackChecker, MessageHandler messageHandler) {
        this.itemStackChecker = itemStackChecker;
        this.messageHandler = messageHandler;
        this.multipliers = new HashSet<>();
        if (!plugin.getConfig().getBoolean("MultiplierGroups.Enabled")) {
            return;
        }
        List<String> lines = plugin.getConfig().getStringList("MultiplierGroups.List");
        for (String s : lines) {
            BSMultiplier m = new BSMultiplier(s);
            if (m.isValid()) {
                this.multipliers.add(m);
            }
        }
    }

    public String calculatePriceDisplayWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d, String message) {
        d = calculatePriceWithMultiplier(p, buy, clicktype, d);
        if (buy.getRewardType(clicktype) == BSRewardType.ItemAll) {
            if (p != null) {
                ItemStack i = (ItemStack) buy.getReward(clicktype);
                int count = this.itemStackChecker.getAmountOfFreeSpace(p, i);

                if (count == 0) {
                    return this.messageHandler.get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d)));
                }
                d *= count;
            } else {
                return this.messageHandler.get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d)));
            }
        }
        return message.replace("%number%", MathTools.displayNumber(d));
    }

    public double calculatePriceWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d) {
        return calculatePriceWithMultiplier(p, buy.getPriceType(clicktype), d);
    }

    public double calculatePriceWithMultiplier(Player p, BSPriceType pricetype, double d) { //Used for prices
        for (BSMultiplier m : multipliers) {
            d = m.calculateValue(p, pricetype, d, BSMultiplier.RANGE_PRICE_ONLY);
        }
        return Math.round(d * 100.0) / 100.0;
    }

    public String calculateRewardDisplayWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d, String message) {
        d = calculateRewardWithMultiplier(p, buy, clicktype, d);
        if (buy.getPriceType(clicktype) == BSPriceType.ItemAll) {
            if (p != null) {
                ItemStack i = (ItemStack) buy.getPrice(clicktype);
                int count = this.itemStackChecker.getAmountOfSameItems(p, i, buy);

                if (count == 0) {
                    return this.messageHandler.get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d)));
                }
                d *= count;
            } else {
                return this.messageHandler.get("Display.ItemAllEach").replace("%value%", message.replace("%number%", MathTools.displayNumber(d)));
            }
        }
        return message.replace("%number%", MathTools.displayNumber(d));
    }

    public double calculateRewardWithMultiplier(Player p, BSBuy buy, ClickType clicktype, double d) { //Used for reward; Works the other way around
        return calculateRewardWithMultiplier(p, buy.getRewardType(clicktype), d);
    }

    public double calculateRewardWithMultiplier(Player p, BSRewardType rewardtype, double d) { //Used for reward; Works the other way around
        for (BSMultiplier m : multipliers) {
            d = m.calculateValue(p, BSPriceType.detectType(rewardtype.name()), d, BSMultiplier.RANGE_REWARD_ONLY);
        }
        return Math.round(d * 100.0) / 100.0;
    }

    public boolean hasMultipliers() {
        return !multipliers.isEmpty();
    }
}
