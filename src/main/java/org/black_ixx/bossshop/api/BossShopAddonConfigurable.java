package org.black_ixx.bossshop.api;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class BossShopAddonConfigurable extends BossShopAddon {
    private BSAddonConfig config;

    /**
     * Enables the config for the addon
     */
    @Override
    protected void enable() {
        this.config = new BSAddonConfig(this, "config");
        super.enable();
    }

    /**
     * Disables the addon for the config
     */
    @Override
    protected void disable() {
        super.disable();
        if (this.saveConfigOnDisable()) {
            this.config.save();
        }
    }

    /**
     * Reloads thje config for an addon
     * @param sender the execute of the command
     */
    @Override
    public void reload(CommandSender sender) {
        this.config.reload();
        super.reload(sender);
    }


    /**
     * Gets the config for the addon as a FileConfiguration
     * @return config for addon
     */
    @Override
    public @NotNull FileConfiguration getConfig() {
        return this.config.getConfig();
    }

    /**
     * Gets the config for the addon
     * @return config for addon
     */
    public BSAddonConfig getAddonConfig() {
        return this.config;
    }

    /**
     * Reloads the config
     */
    @Override
    public void reloadConfig() {
        this.config.reload();
    }

    /**
     * Saves the config
     */
    @Override
    public void saveConfig() {
        this.config.save();
    }

    /**
     * Determines whether or not to save the config when the addon is disabled
     * @return save or not
     */
    public abstract boolean saveConfigOnDisable();


}
