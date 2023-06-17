package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class AutoRefreshHandler {
    private final BukkitTask task;

    //TODO: ensure task is re-initialized on reload.
    public AutoRefreshHandler(final BossShop plugin, final long speed) {
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, () -> ClassManager.manager.getShops().refreshShops(), speed, speed);
    }

    public void stop() {
        this.task.cancel();
    }
}
