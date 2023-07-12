package org.black_ixx.bossshop.module;

import com.google.inject.AbstractModule;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.config.DataFactory;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.managers.BuyItemHandler;
import org.black_ixx.bossshop.managers.MessageHandler;
import org.black_ixx.bossshop.managers.ShopCustomizer;
import org.black_ixx.bossshop.managers.external.BungeeCordManager;
import org.black_ixx.bossshop.managers.external.VaultHandler;
import org.black_ixx.bossshop.managers.features.AutoRefreshHandler;
import org.black_ixx.bossshop.managers.features.ItemDataStorage;
import org.black_ixx.bossshop.managers.features.MultiplierHandler;
import org.black_ixx.bossshop.managers.features.PageLayoutHandler;
import org.black_ixx.bossshop.managers.features.PlayerDataHandler;
import org.black_ixx.bossshop.managers.features.PointsManager;
import org.black_ixx.bossshop.managers.features.TransactionLog;
import org.black_ixx.bossshop.managers.item.ItemStackChecker;
import org.black_ixx.bossshop.managers.item.ItemStackCreator;
import org.black_ixx.bossshop.managers.item.ItemStackTranslator;
import org.black_ixx.bossshop.managers.StringManager;

import java.nio.file.Path;

public class PluginModule extends AbstractModule {
    private final BossShop plugin;

    public PluginModule(final BossShop plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        this.bind(BossShop.class).toInstance(this.plugin);
        this.bind(Path.class).toInstance(this.plugin.getDataFolder().toPath());
        this.bind(ItemStackChecker.class).asEagerSingleton();
        this.bind(StringManager.class).asEagerSingleton();
        this.bind(DataFactory.class).asEagerSingleton();
        this.bind(MessageHandler.class).asEagerSingleton();
        this.bind(ItemStackCreator.class).asEagerSingleton();
        this.bind(ItemStackTranslator.class).asEagerSingleton();
        this.bind(BuyItemHandler.class).asEagerSingleton();
        this.bind(MultiplierHandler.class).asEagerSingleton();
        this.bind(ItemDataStorage.class).asEagerSingleton();
        this.bind(PlayerDataHandler.class).asEagerSingleton();
        this.bind(PointsManager.class).asEagerSingleton();
        this.bind(VaultHandler.class).asEagerSingleton();
        this.bind(BSShops.class).asEagerSingleton();
        this.bind(PageLayoutHandler.class).asEagerSingleton();
        this.bind(BungeeCordManager.class).asEagerSingleton();
        this.bind(ShopCustomizer.class).asEagerSingleton();
        this.bind(TransactionLog.class).asEagerSingleton();
        //TODO: check if delay is greater than 0 before init.
        this.bind(AutoRefreshHandler.class).asEagerSingleton();
    }
}
