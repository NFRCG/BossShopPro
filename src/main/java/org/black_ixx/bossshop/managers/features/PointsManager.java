package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.pointsystem.BSPointsAPI;
import org.black_ixx.bossshop.pointsystem.BSPointsPlugin;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginCoins;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginFailed;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginGadgetsMenu;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginJobs;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginNone;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginPlayerPoints;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginTokenEnchant;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginTokenManager;
import org.black_ixx.bossshop.pointsystem.BSPointsPluginVotingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PointsManager {
    private final BSPointsPlugin pa;

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
        this.pa = switch (p) {
            case PLAYERPOINTS -> new BSPointsPluginPlayerPoints();
            case TOKENENCHANT -> new BSPointsPluginTokenEnchant();
            case TOKENMANAGER -> new BSPointsPluginTokenManager();
            case JOBS -> new BSPointsPluginJobs();
            case VOTINGPLUGIN -> new BSPointsPluginVotingPlugin();
            case GADGETS_MENU -> new BSPointsPluginGadgetsMenu();
            case COINS -> new BSPointsPluginCoins();
            case NONE -> new BSPointsPluginNone();
            case CUSTOM -> BSPointsAPI.get(p.getCustom()) != null ? BSPointsAPI.get(p.getCustom()) : new BSPointsPluginFailed();
        };
        Bukkit.getLogger().info("Successfully hooked into Points plugin " + this.pa.getName() + ".");
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

}
