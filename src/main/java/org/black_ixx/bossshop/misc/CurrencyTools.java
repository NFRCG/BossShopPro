package org.black_ixx.bossshop.misc;

import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;


public class CurrencyTools {
    /**
     * Take currency from a player
     * @param p player to modify
     * @param currency the currency to take
     * @param cost the amount to take
     * @return the price to take from user
     */
    public static String takePrice(Player p, BSCurrency currency, double cost) {
        switch (currency) {
            case EXP -> {
                p.setLevel(p.getLevel() - (int) cost);
                int balance_exp = p.getLevel();
                return ClassManager.manager.getMessageHandler().get("Display.Exp").replace("%levels%", MathTools.displayNumber(balance_exp, BSPriceType.Exp));
            }
            case MONEY -> {
                if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p.getName())) {
                    ClassManager.manager.getBugFinder().severe("Unable to take money! No economy account existing! (" + p.getName() + ", " + cost + ")");
                    return "";
                }
                ClassManager.manager.getVaultHandler().getEconomy().withdrawPlayer(p.getName(), cost);
                double balance = ClassManager.manager.getVaultHandler().getEconomy().getBalance(p.getName());
                return ClassManager.manager.getMessageHandler().get("Display.Money").replace("%money%", MathTools.displayNumber(balance, BSPriceType.Money));
            }
            case POINTS -> {
                double balance_points = ClassManager.manager.getPointsManager().takePoints(p, cost);
                return ClassManager.manager.getMessageHandler().get("Display.Points").replace("%points%", MathTools.displayNumber(balance_points, BSPriceType.Points));
            }
        }

        return "";
    }

    /**
     * Give a currency reward to a player
     * @param p the player to modify
     * @param currency the currency to give
     * @param reward the amount to give
     */
    public static void giveReward(Player p, BSCurrency currency, double reward) {
        switch (currency) {
            case EXP -> p.setLevel(p.getLevel() + (int) reward);
            case MONEY -> {
                if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p)) {
                    ClassManager.manager.getMessageHandler().sendMessage("Economy.NoAccount", p);
                    ClassManager.manager.getBugFinder().severe("Unable to give " + p.getName() + " his/her money: He/She does not have an economy account.");
                    return;
                }
                ClassManager.manager.getVaultHandler().getEconomy().depositPlayer(p, reward);
            }
            case POINTS -> ClassManager.manager.getPointsManager().givePoints(p, reward);
        }
    }

    /**
     * Get the display price for something
     * @param currency the currency to get
     * @param price the price to check
     * @return display price
     */
    public static String getDisplayPrice(BSCurrency currency, double price) {
        return switch (currency) {
            case EXP -> ClassManager.manager.getMessageHandler().get("Display.Exp").replace("%levels%", MathTools.displayNumber((int) price, currency.getPriceType()));
            case MONEY -> ClassManager.manager.getMessageHandler().get("Display.Money").replace("%money%", MathTools.displayNumber(price, currency.getPriceType()));
            case POINTS -> ClassManager.manager.getMessageHandler().get("Display.Points").replace("%points%", MathTools.displayNumber(price, currency.getPriceType()));
        };
    }


    public enum BSCurrency {
        MONEY {
            @Override
            public BSPriceType getPriceType() {
                return BSPriceType.Money;
            }

            @Override
            public BSRewardType getRewardType() {
                return BSRewardType.Money;
            }

            @Override
            public double getBalance(Player p) {
                if (!ClassManager.manager.getVaultHandler().getEconomy().hasAccount(p)) {
                    ClassManager.manager.getBugFinder().severe("Unable to read balance! No economy account existing! (" + p.getName() + ")");
                    return -1;
                }
                return ClassManager.manager.getVaultHandler().getEconomy().getBalance(p);
            }
        },
        EXP {
            @Override
            public BSPriceType getPriceType() {
                return BSPriceType.Exp;
            }

            @Override
            public BSRewardType getRewardType() {
                return BSRewardType.Exp;
            }

            @Override
            public double getBalance(Player p) {
                return p.getExpToLevel();
            }
        },
        POINTS {
            @Override
            public BSPriceType getPriceType() {
                return BSPriceType.Points;
            }

            @Override
            public BSRewardType getRewardType() {
                return BSRewardType.Points;
            }

            @Override
            public double getBalance(Player p) {
                return ClassManager.manager.getPointsManager().getPoints(p);
            }
        };

        public static BSCurrency detectCurrency(String name) {
            for (BSCurrency currency : BSCurrency.values()) {
                if (currency.name().equalsIgnoreCase(name)) {
                    return currency;
                }
            }
            return null;
        }

        public abstract BSPriceType getPriceType();

        public abstract BSRewardType getRewardType();

        public abstract double getBalance(Player p);
    }


}
