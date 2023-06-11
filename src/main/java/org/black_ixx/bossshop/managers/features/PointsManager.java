package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.pointsystem.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

public class PointsManager {

    private BSPointsPlugin pa;

    public PointsManager() {
        this(ClassManager.manager.getSettings().getPointsPlugin());
    }


    public PointsManager(PointsPlugin p) {
        if (p == null) {
            this.pa = new BSPointsPluginFailed();
            return;
        }

        if (p != PointsPlugin.NONE) {
            if (Bukkit.getPluginManager().getPlugin(p.getPluginName()) == null) {
                ClassManager.manager.getBugFinder().severe("You defined " + p.getPluginName() + " as Points Plugin... BUT IT WAS NOT FOUND?! Please install it or use an alternative like PlayerPoints (http://dev.bukkit.org/server-mods/playerpoints/). If you want " + "BossShopPro" + " to auto-detect your Points plugin simply set 'PointsPlugin: auto-detect'.");
                this.pa = new BSPointsPluginFailed();
                return;
            }
        }

        switch (p) {
            case PLAYERPOINTS -> this.pa = new BSPointsPluginPlayerPoints();
            case TOKENENCHANT -> this.pa = new BSPointsPluginTokenEnchant();
            case TOKENMANAGER -> this.pa = new BSPointsPluginTokenManager();
            case JOBS -> this.pa = new BSPointsPluginJobs();
            case VOTINGPLUGIN -> this.pa = new BSPointsPluginVotingPlugin();
            case GADGETS_MENU -> this.pa = new BSPointsPluginGadgetsMenu();
            case COINS -> new BSPointsPluginCoins();
            case NONE -> this.pa = new BSPointsPluginNone();
            case CUSTOM -> {
                BSPointsPlugin customPoints = BSPointsAPI.get(p.getCustom());
                if (customPoints != null) {
                    this.pa = customPoints;
                }
            }
        }

        if (this.pa == null) {
            ClassManager.manager.getBugFinder().warn("No PointsPlugin was found... You need one if you want BossShopPro to work with Points! Get PlayerPoints here: http://dev.bukkit.org/server-mods/playerpoints/");
            this.pa = new BSPointsPluginFailed();
        } else {
            BossShop.log("Successfully hooked into Points plugin " + this.pa.getName() + ".");
        }
    }

    public double getPoints(OfflinePlayer player) {
        return pa.getPoints(player);
    }

    public double setPoints(OfflinePlayer player, double points) {
        return pa.setPoints(player, points);
    }

    public double givePoints(OfflinePlayer player, double points) {
        return pa.givePoints(player, points);
    }

    public double takePoints(OfflinePlayer player, double points) {
        return pa.takePoints(player, points);
    }

    public boolean usesDoubleValues() {
        return pa.usesDoubleValues();
    }

    public enum PointsPlugin {
        NONE(new String[]{"none", "nothing"}),
        PLAYERPOINTS(new String[]{"PlayerPoints", "PlayerPoint", "PP"}),
        TOKENENCHANT(new String[]{"TokenEnchant", "TE", "TokenEnchants"}),
        TOKENMANAGER(new String[]{"TokenManager", "TM"}),
        JOBS(new String[]{"Jobs", "JobsReborn"}),
        VOTINGPLUGIN(new String[]{"VotingPlugin", "VP"}),
        GADGETS_MENU(new String[]{"GadgetsMenu"}),
        COINS(new String[] { "Coins"}),
        CUSTOM(new String[0]);

        private String[] nicknames;
        private String custom_name;

        PointsPlugin(String[] nicknames) {
            this.nicknames = nicknames;
        }

        public String getCustom() {
            return this.custom_name;
        }

        public void setCustom(String custom_name) {
            this.custom_name = custom_name;
        }

        public String[] getNicknames() {
            return this.nicknames;
        }

        public String getPluginName() {
            if (getNicknames() == null) {
                return custom_name;
            }
            if (getNicknames().length == 0) {
                return custom_name;
            }
            return getNicknames()[0];
        }
    }

}
