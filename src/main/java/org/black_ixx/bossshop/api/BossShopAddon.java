package org.black_ixx.bossshop.api;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings({"deprecation", "unused"})
public abstract class BossShopAddon extends JavaPlugin {
    private BossShop bs;
    private boolean disabled = false;

    @Override
    public void onEnable() {
        this.disabled = false;
        this.bs = (BossShop) Bukkit.getPluginManager().getPlugin("BossShopPro");
        if (this.bs != null && this.getWorth(this.bs.getDescription().getVersion()) >= this.getWorth(this.getRequiredBossShopVersion())) {
            this.bs.getAPI().addEnabledAddon(this);
            this.getSLF4JLogger().info("Enabling Addon: {}", this.getAddonName());
            this.enable();
        }
        this.getSLF4JLogger().error("[{}] BSP is outdated or not found! You need version: {} in order to run this addon! Disabling Addon...", this.getAddonName(), this.getRequiredBossShopVersion());
        this.disabled = true;
        Bukkit.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onDisable() {
        if (!this.disabled) {
            this.getSLF4JLogger().info("Disabling Addon: {}", this.getAddonName());
            this.disable();
        }
    }

    /**
     * Called to enable the addon. Can be overwritten.
     */
    protected void enable() {
        this.enableAddon();
    }

    /**
     * Called to disable the addon. Can be overwritten.
     */
    protected void disable() {
        this.disableAddon();
    }

    /**
     * Called to reload the addon. Can be overwritten.
     *
     * @param sender Executor of the command
     */
    public void reload(CommandSender sender) {
        this.bossShopReloaded(sender);
        //TODO: replace when refactoring messaging.
        Component mes = Component.text()
                .append(Component.text("Reloaded BossShopPro Addon: ").color(NamedTextColor.YELLOW))
                .append(Component.text(this.getAddonName()).color(NamedTextColor.GOLD)).build();
        Component message = Component.text(String.format("Reloaded %s Addon %s", "BossShopPro", this.getAddonName()));
        sender.sendMessage(message);
    }

    /**
     * Get an instance of the BossShop class
     *
     * @return instance of class
     */
    public final BossShop getBossShop() {
        return this.bs;
    }

    protected double getWorth(String s) {
        //TODO: rip out
        try {
            if (s == null || s.isEmpty()) {
                return 0;
            }
            double x = 0;
            String[] parts = s.replace(".", ":").split(":");
            x += Integer.parseInt(parts[0].trim());
            if (parts.length == 2) {
                x += 0.1 * Integer.parseInt(parts[1].trim());
            }
            if (parts.length == 3) {
                x += 0.1 * Integer.parseInt(parts[1].trim());
                x += 0.01 * Integer.parseInt(parts[2].trim());
            }
            return x;
        } catch (Exception e) {
            this.getSLF4JLogger().warn("Was not able to get the version of {}", s);
            return 1.00;
        }
    }

    /**
     * Creates store for an addon
     *
     * @param plugin the plugin addon
     * @param name   the name of the addon
     * @return new storage for an addon
     */
    public BSAddonStorage createStorage(Plugin plugin, String name) {
        //TODO: investigate TYPE_LOCAL_FILE var
        return new BSAddonConfig(plugin, name);
    }

    /**
     * Get the name of the addon
     *
     * @return name of addon
     */
    public abstract String getAddonName();

    /**
     * Get the version required for the addon to work
     *
     * @return version required
     */
    public abstract String getRequiredBossShopVersion();

    /**
     * Enables the addon
     */
    public abstract void enableAddon();

    public abstract void bossShopFinishedLoading();

    /**
     * Disables the addon
     */
    public abstract void disableAddon();

    public abstract void bossShopReloaded(CommandSender sender);
}
