package org.black_ixx.bossshop.managers.external;

import me.clip.placeholderapi.PlaceholderAPI;
import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIHandler {

    public PlaceholderAPIHandler() {
        Bukkit.getLogger().info("Hooked into PlaceholderAPI");
    }

    public String transformString(String s, Player p) {
        if (containsPlaceholder(s)) {
            s = PlaceholderAPI.setPlaceholders(p, s);
        }
        return s;
    }

    public boolean containsPlaceholder(String s) {
        return PlaceholderAPI.containsPlaceholders(s);
    }


}
