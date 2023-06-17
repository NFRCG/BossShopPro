package org.black_ixx.bossshop.managers.external;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.black_ixx.bossshop.misc.NoEconomy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//TODO: replace with error log.
public class VaultHandler {
    private final Permission perms;
    private final Economy economy;

    public VaultHandler() {
        RegisteredServiceProvider<Economy> ecoProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        RegisteredServiceProvider<Permission> permProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        this.economy = ecoProvider == null ? new NoEconomy() : ecoProvider.getProvider();
        if (permProvider == null) {
            this.perms = null;
            Bukkit.getLogger().warning("No permissions plugin was found!");
            return;
        }
        this.perms = permProvider.getProvider();
    }

    /**
     * Gets the Vault Permission API, or null if a permissions plugin is missing.
     *
     * @return the API.
     */
    public @Nullable Permission getPermission() {
        return this.perms;
    }

    /**
     * Gets the Vault Economy API.
     *
     * @return the API.
     */
    public @NotNull Economy getEconomy() {
        return this.economy;
    }
}
