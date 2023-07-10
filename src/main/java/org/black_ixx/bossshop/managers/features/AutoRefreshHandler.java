package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSShops;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;

public class AutoRefreshHandler {
    private final BukkitTask task;

    //TODO: ensure task is re-initialized on reload.
    @Inject
    public AutoRefreshHandler(final BossShop plugin, final BSShops shops, final long speed) {
        this.task = Bukkit.getScheduler().runTaskTimer(plugin, shops::refreshShops, speed, speed);
    }

    public void stop() {
        this.task.cancel();
    }
}
