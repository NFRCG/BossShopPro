package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.api.BossShopAddon;
import org.black_ixx.bossshop.config.DataFactory;
import org.black_ixx.bossshop.config.SettingsData;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSRegisterTypesEvent;
import org.black_ixx.bossshop.managers.external.BungeeCordManager;
import org.black_ixx.bossshop.managers.external.VaultHandler;
import org.black_ixx.bossshop.managers.features.BugFinder;
import org.black_ixx.bossshop.managers.features.MultiplierHandler;
import org.black_ixx.bossshop.managers.features.PageLayoutHandler;
import org.black_ixx.bossshop.managers.features.PlayerDataHandler;
import org.black_ixx.bossshop.managers.features.PointsManager;
import org.black_ixx.bossshop.managers.features.TransactionLog;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.black_ixx.bossshop.managers.item.ItemStackChecker;
import org.black_ixx.bossshop.managers.item.ItemStackCreator;
import org.black_ixx.bossshop.managers.item.ItemStackTranslator;
import org.bukkit.Bukkit;

public class ClassManager {
    public static ClassManager manager;

    private final DataFactory factory;

    private final BuyItemHandler buyItemHandler;
    private final BugFinder bugfinder;
    private final BossShop plugin;
    private final PlayerDataHandler playerdataHandler;
    ///////////////////////////////
    private PointsManager pointsmanager;
    private VaultHandler vaulthandler;
    private BSShops shops;
    private PageLayoutHandler pagelayoutHandler;
    private BungeeCordManager bungeeCordManager;
    private ShopCustomizer customizer;
    private TransactionLog transactionLog;

    public ClassManager(BossShop plugin) {
        this.plugin = plugin;
        //TODO: DI- Temporarily using the monolith to replace usages of removed code.
        this.factory = new DataFactory(plugin.getDataFolder().toPath());
        manager = this;

        //TODO: write code to export default shops.

        BSRewardType.loadTypes();
        BSPriceType.loadTypes();
        //TODO: register conditions here.
        ItemDataPart.loadTypes();

        //////////////// <- Independent Classes

        playerdataHandler = new PlayerDataHandler();
        bugfinder = new BugFinder(plugin);
        buyItemHandler = new BuyItemHandler();

    }

    /**
     * Setup the dependent classes
     */
    public void setupDependentClasses() {
        Bukkit.getPluginManager().callEvent(new BSRegisterTypesEvent());
        SettingsData config = this.factory.settings();
        this.pagelayoutHandler = new PageLayoutHandler(this.factory);
        //TODO: impl
        this.pointsmanager = new PointsManager(null);
        //TODO: fix
        this.shops = new BSShops(plugin);
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            //TODO: replace with error log
            Bukkit.getLogger().warning("Vault was not found! You will not be able to use currency or permission related features!");
        }
        this.vaulthandler = new VaultHandler();
        this.transactionLog = new TransactionLog(this.plugin);
        if (config.bungeecord()) {
            this.bungeeCordManager = new BungeeCordManager();
            Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
            //Bukkit.getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", bungeeCordManager);
        }
        if (this.plugin.getAPI().getEnabledAddons() != null) {
            for (BossShopAddon addon : plugin.getAPI().getEnabledAddons()) {
                addon.bossShopFinishedLoading();
            }
        }
    }

    //TODO: replace with DI
    public DataFactory getFactory() {
        return this.factory;
    }

    public ItemStackChecker getItemStackChecker() {
        return itemstackChecker;
    }

    public StringManager getStringManager() {
        return stringmanager;
    }

    public PointsManager getPointsManager() {
        return pointsmanager;
    }

    public VaultHandler getVaultHandler() {
        return this.vaulthandler;
    }

    public MessageHandler getMessageHandler() {
        return messagehandler;
    }

    public ItemStackCreator getItemStackCreator() {
        return itemstackCreator;
    }

    public ItemStackTranslator getItemStackTranslator() {
        return itemstackTranslator;
    }

    public BuyItemHandler getBuyItemHandler() {
        return buyItemHandler;
    }

    public BugFinder getBugFinder() {
        return bugfinder;
    }

    public BossShop getPlugin() {
        return plugin;
    }

    public BSShops getShops() {
        return shops;
    }

    public PageLayoutHandler getPageLayoutHandler() {
        return pagelayoutHandler;
    }

    public PlayerDataHandler getPlayerDataHandler() {
        return playerdataHandler;
    }

    public BungeeCordManager getBungeeCordManager() {
        if (bungeeCordManager == null) {
            bungeeCordManager = new BungeeCordManager();
        }
        return bungeeCordManager;
    }

    public ShopCustomizer getShopCustomizer() {
        if (customizer == null) {
            customizer = new ShopCustomizer();
        }
        return customizer;
    }

    public TransactionLog getTransactionLog() {
        return transactionLog;
    }

    public MultiplierHandler getMultiplierHandler() {
        return multiplierHandler;
    }
}
