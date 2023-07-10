package org.black_ixx.bossshop;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.black_ixx.bossshop.api.BossShopAPI;
import org.black_ixx.bossshop.api.BossShopAddon;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.events.BSReloadedEvent;
import org.black_ixx.bossshop.listeners.InventoryListener;
import org.black_ixx.bossshop.listeners.PlayerListener;
import org.black_ixx.bossshop.listeners.ShopItemCreationListener;
import org.black_ixx.bossshop.listeners.SignListener;
import org.black_ixx.bossshop.listeners.TypeRegisterListener;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.CommandManager;
import org.black_ixx.bossshop.managers.features.AutoRefreshHandler;
import org.black_ixx.bossshop.managers.features.ItemDataStorage;
import org.black_ixx.bossshop.managers.features.TransactionLog;
import org.black_ixx.bossshop.module.PluginModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Stream;

public class BossShop extends JavaPlugin {
    private Injector injector;
    private ClassManager manager;
    private BossShopAPI api;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Loading data...");
        this.injector = Guice.createInjector(new PluginModule(this));
        this.manager = new ClassManager(this);
        Bukkit.getPluginManager().registerEvents(this.injector.getInstance(InventoryListener.class), this);
        Bukkit.getPluginManager().registerEvents(this.injector.getInstance(SignListener.class), this);
        Bukkit.getPluginManager().registerEvents(this.injector.getInstance(PlayerListener.class), this);
        this.api = new BossShopAPI(this);
        CommandManager commander = this.injector.getInstance(CommandManager.class);
        Stream.of("bs", "bossshop", "shop").filter(x -> this.getCommand(x) != null).forEach(x -> this.getCommand(x).setExecutor(commander));
        Bukkit.getPluginManager().registerEvents(this.injector.getInstance(ShopItemCreationListener.class), this);
        Bukkit.getPluginManager().registerEvents(this.injector.getInstance(TypeRegisterListener.class), this);
        this.manager.setupDependentClasses();
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
        this.manager.getMessageHandler().reloadConfig();
        if (this.manager.getShops() != null) {
            for (String s : this.manager.getShops().getShopIds().keySet()) {
                BSShop shop = this.manager.getShops().getShops().get(s);
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
        this.unloadClasses();
        this.manager = new ClassManager(this);
        if (api.getEnabledAddons() != null) {
            for (BossShopAddon addon : api.getEnabledAddons()) {
                addon.reload(sender);
            }
        }
        this.manager.setupDependentClasses();
        BSReloadedEvent event = new BSReloadedEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    private void unloadClasses() {
        Bukkit.getScheduler().cancelTasks(this);
        this.injector.getInstance(ItemDataStorage.class).saveConfig();
        this.injector.getInstance(TransactionLog.class).saveConfig();
        this.injector.getInstance(AutoRefreshHandler.class).stop();
    }

    private void closeShops() {
        this.injector.getInstance(BSShops.class).getShops().forEach((k, v) -> v.close());
    }
}
