package org.black_ixx.bossshop.managers;


import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSCheckStringForFeaturesEvent;
import org.black_ixx.bossshop.events.BSTransformStringEvent;
import org.black_ixx.bossshop.managers.external.VaultHandler;
import org.black_ixx.bossshop.managers.features.MultiplierHandler;
import org.black_ixx.bossshop.managers.features.PlayerDataHandler;
import org.black_ixx.bossshop.managers.features.PointsManager;
import org.black_ixx.bossshop.misc.MathTools;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringManager {
    private static final Pattern hexPattern = Pattern.compile("(#[a-fA-F0-9]{6})");
    private final VaultHandler vaultHandler;
    private final PointsManager pointsManager;
    private final PlayerDataHandler playerDataHandler;
    private final MultiplierHandler multiplierHandler;

    @Inject
    public StringManager(VaultHandler vaultHandler, PointsManager pointsManager, PlayerDataHandler playerDataHandler, MultiplierHandler multiplierHandler) {
        this.vaultHandler = vaultHandler;
        this.pointsManager = pointsManager;
        this.playerDataHandler = playerDataHandler;
        this.multiplierHandler = multiplierHandler;
    }

    /**
     * Transform specific strings from one thing to another
     *
     * @param s input string
     * @return transformed string
     */
    public String transform(String s) {
        if (s == null) {
            return null;
        }
        Matcher matcher = hexPattern.matcher(s);
        while (matcher.find()) {
            String color = s.substring(matcher.start(), matcher.end());
            s = s.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
        }

        s = s.replace("[<3]", "❤");
        s = s.replace("[*]", "★");
        s = s.replace("[**]", "✹");
        s = s.replace("[o]", "●");
        s = s.replace("[v]", "✔");
        s = s.replace("[+]", "♦");
        s = s.replace("[x]", "✦");
        s = s.replace("[%]", "♠");
        s = s.replace("[%%]", "♣");
        s = s.replace("[radioactive]", "☢");
        s = s.replace("[peace]", "☮");
        s = s.replace("[moon]", "☾");
        s = s.replace("[crown]", "♔");
        s = s.replace("[snowman]", "☃");
        s = s.replace("[tools]", "⚒");
        s = s.replace("[swords]", "⚔");
        s = s.replace("[note]", "♩ ");
        s = s.replace("[block]", "█");
        s = s.replace("[triangle]", "▲");
        s = s.replace("[warn]", "⚠");
        s = s.replace("[left]", "←");
        s = s.replace("[right]", "→");
        s = s.replace("[up]", "↑");
        s = s.replace("[down]", "↓");

        s = ChatColor.translateAlternateColorCodes('&', s);

        s = s.replace("[and]", "&");
        s = s.replace("[colon]", ":");
        s = s.replace("[hashtag]", "#");
        return s;
    }


    public String transform(String s, BSBuy item, BSShop shop, BSShopHolder holder, Player target) {
        if (s == null) {
            return null;
        }

        if (s.contains("%")) {
            if (item != null) {
                s = item.transformMessage(s, shop, target);
            }
            if (shop != null) {
                if (shop.getShopName() != null) {
                    s = s.replace("%shop%", shop.getShopName());
                }
                if (shop.getDisplayName() != null) {
                    s = s.replace("%shopdisplayname%", shop.getDisplayName());
                }
            }
            if (holder != null) {
                s = s.replace("%page%", String.valueOf(holder.getDisplayPage()));
                s = s.replace("%maxpage%", String.valueOf(holder.getDisplayHighestPage()));
            }

            BSTransformStringEvent event = new BSTransformStringEvent(s, item, shop, holder, target);
            Bukkit.getPluginManager().callEvent(event);
            s = event.getText();
        }

        return transform(s, target);
    }

    public String transform(String s, Player target) {
        if (s == null) {
            return null;
        }

        if (target != null && s.contains("%")) {
            s = s.replace("%name%", target.getName()).replace("%player%", target.getName()).replace("%target%", target.getName());
            s = s.replace("%displayname%", target.getDisplayName());
            s = s.replace("%uuid%", target.getUniqueId().toString());

            if (s.contains("%balance%") && this.vaultHandler != null) {
                if (this.vaultHandler.getEconomy() != null) {
                    double balance = this.vaultHandler.getEconomy().getBalance(target.getName());
                    s = s.replace("%balance%", MathTools.displayNumber(balance, BSPriceType.Money));
                }
            }
            if (s.contains("%balancepoints%") && this.pointsManager != null) {
                double balance_points = this.pointsManager.getPoints(target);
                s = s.replace("%balancepoints%", MathTools.displayNumber(balance_points, BSPriceType.Points));
            }

            if (s.contains("%world%")) {
                s = s.replace("%world%", target.getWorld().getName());
            }

            if (s.contains("%item_in_hand%")) {
                s = s.replace("%item_in_hand%", Misc.getItemInMainHand(target).getType().name());
            }

            if (s.contains("%input%")) {
                s = s.replace("%input%", this.playerDataHandler.getInput(target));
            }
            s = PlaceholderAPI.setPlaceholders(target, s);
        }

        return transform(s);
    }


    public boolean checkStringForFeatures(BSShop shop, BSBuy buy, String s) { //Returns true if this would make a shop customizable
        if (s.matches(hexPattern.pattern()) || List.of("%balance%", "%balancepoints%", "%name%", "%player%", "%uuid%", "%page%", "%maxpage%", "%world%").contains(s)) {
            return true;
        }
        if (s.contains("{") && s.contains("}")) {
            return true;
        }
        if (s.contains("%input%") && PlaceholderAPI.containsPlaceholders(s)) {
            return true;
        }
        if (s.contains("%reward%") || s.contains("%price%")) {
            if (this.multiplierHandler.hasMultipliers()) {
                return true;
            }
            if (buy.getPriceType(null) == BSPriceType.ItemAll || buy.getRewardType(null) == BSRewardType.ItemAll) {
                return true;
            }
        }
        BSCheckStringForFeaturesEvent event = new BSCheckStringForFeaturesEvent(s, buy, shop);
        Bukkit.getPluginManager().callEvent(event);
        return event.containsFeature();
    }
}
