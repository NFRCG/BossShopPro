package org.black_ixx.bossshop;


import org.black_ixx.bossshop.api.BossShopAPI;
import org.black_ixx.bossshop.api.BossShopAddon;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.events.BSReloadedEvent;
import org.black_ixx.bossshop.inbuiltaddons.InbuiltAddonLoader;
import org.black_ixx.bossshop.listeners.InventoryListener;
import org.black_ixx.bossshop.listeners.PlayerListener;
import org.black_ixx.bossshop.listeners.SignListener;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public class BossShop extends JavaPlugin {
    private ClassManager manager;
    private BossShopAPI api;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading data...");
        this.manager = new ClassManager(this);
        this.getServer().getPluginManager().registerEvents(new InventoryListener(this, this.manager.getFactory()), this);
        this.getServer().getPluginManager().registerEvents(new SignListener(), this);
        this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        this.api = new BossShopAPI(this);
        CommandManager commander = new CommandManager();
        Stream.of("bs", "bossshop", "shop").filter(x -> this.getCommand(x) != null).forEach(x -> this.getCommand(x).setExecutor(commander));
        //TODO: fix
        Bukkit.getScheduler().runTaskLaterAsynchronously(this, () -> {
            new InbuiltAddonLoader().load(this);
            this.manager.setupDependentClasses();
        }, 5L);
    }
    @Override
    public void onDisable() {
        this.closeShops();
        this.unloadClasses();
        Bukkit.getLogger().info("Disabling... bye!");
    }

    public ClassManager getClassManager() {
        return this.manager;
    }

    public BossShopAPI getAPI() {
        return this.api;
    }

    public void reloadPlugin(CommandSender sender) {
        this.closeShops();
        this.reloadConfig();

        closeShops();

        reloadConfig();
        manager.getMessageHandler().reloadConfig();

        if (manager.getShops() != null) {
            for (String s : manager.getShops().getShopIds().keySet()) {
                BSShop shop = manager.getShops().getShops().get(s);
                if (shop != null) {
                    shop.reloadShop();
                }
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (api.isValidShop(p.getOpenInventory())) {
                p.closeInventory();
            }
        }

        unloadClasses();

        manager = new ClassManager(this);

        if (api.getEnabledAddons() != null) {
            for (BossShopAddon addon : api.getEnabledAddons()) {
                addon.reload(sender);
            }
        }

        manager.setupDependentClasses();


        BSReloadedEvent event = new BSReloadedEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    private void unloadClasses() {
        Bukkit.getScheduler().cancelTasks(this);
        this.manager.getStorageManager().saveConfig();
        this.manager.getItemDataStorage().saveConfig();
        this.manager.getTransactionLog().saveConfig();
        this.manager.getAutoRefreshHandler().stop();
    }

    private void closeShops() {
        this.manager.getShops().getShops().forEach((k, v) -> v.close());
    }
}
