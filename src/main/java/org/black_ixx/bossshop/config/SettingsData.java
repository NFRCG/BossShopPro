package org.black_ixx.bossshop.config;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.black_ixx.bossshop.config.data.Formats;
import org.black_ixx.bossshop.multiplier.Multiplier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents data from config.yml.
 */
@ConfigSerializable
@SuppressWarnings({"unused", "FieldMayBeFinal", "FieldCanBeLocal"})
//TODO: make user specify the points plugin.
//TODO: serverpinging, multiplier groups
public class SettingsData implements ConfigData {
    private boolean enableSigns = true;
    private String mainShop = "Menu";
    private boolean hideItemsWhenNoPermission = false;
    private boolean sellItemsWithDifferentEnchants = false;
    private boolean enableTransactionLog = false;
    private boolean allowUnsafeEnchantments = false;
    private boolean sellDamagedItems = false;
    private boolean checkStackSize = true;
    private boolean inventoryFullDropItems = true;
    private long clickDelay = 200L;
    private int clickWarnings = 1;
    private long forgetClickSpam = 5000L;
    private int inputTimeout = 45;
    private boolean sellAllPlaceholderShowFinalReward = false;
    private boolean expUseLevels = true;
    private long autoRefreshDelay = 100L;
    private boolean bungeecord = false;
    private boolean velocity = false;
    private boolean asyncActions = false;
    private String pointsPlugin = "auto-detect";
    private Formats moneyFormat = Formats.defaults();
    private Formats pointsFormat = Formats.defaults();
    private Map<String, Multiplier> multipliers;
    private Map<String, Sound> sounds;

    public SettingsData() {
        this.sounds = new HashMap<>();
        this.sounds.put("click", this.createSound("ui.button.click", 1f, 1f));
        this.sounds.put("purchase", this.createSound("entity.player.levelup", 1f, 1.8f));
        this.sounds.put("noPermission", this.createSound("entity.blaze.death", 1f, 1f));
        this.sounds.put("notEnoughMoney", this.createSound("entity.shulker.hurt", 1f, 0.8f));
        this.sounds.put("changeShop", this.createSound("ui.button.click", 0.2f, 1f));
        this.sounds.put("changePage", this.createSound("block.chest.open", 0.2f, 1f));
        this.sounds.put("open", this.createSound("entity.elder_guardian.curse", 1f, 1.8f));
        this.sounds.put("close", this.createSound("entity.elder_guardian.curse", 1f, 1.8f));
    }

    public boolean enableSigns() {
        return this.enableSigns;
    }

    public String mainShop() {
        return this.mainShop;
    }

    public boolean hideItemsWhenNoPermission() {
        return this.hideItemsWhenNoPermission;
    }

    public boolean sellItemsWithDifferentEnchants() {
        return this.sellItemsWithDifferentEnchants;
    }

    public boolean enableTransactionLog() {
        return this.enableTransactionLog;
    }

    public boolean allowUnsafeEnchantments() {
        return this.allowUnsafeEnchantments;
    }

    public boolean sellDamagedItems() {
        return this.sellDamagedItems;
    }

    public boolean checkStackSize() {
        return this.checkStackSize;
    }

    public boolean inventoryFullDropItems() {
        return this.inventoryFullDropItems;
    }

    public long clickDelay() {
        return this.clickDelay;
    }

    public int clickWarnings() {
        return this.clickWarnings;
    }

    public long forgetClickSpam() {
        return this.forgetClickSpam;
    }

    public int inputTimeout() {
        return this.inputTimeout;
    }

    public boolean sellAllPlaceholderShowFinalReward() {
        return this.sellAllPlaceholderShowFinalReward;
    }

    public boolean expUseLevels() {
        return this.expUseLevels;
    }

    public long autoRefreshDelay() {
        return this.autoRefreshDelay;
    }

    public boolean bungeecord() {
        return this.bungeecord;
    }

    public String pointsPlugin() {
        return this.pointsPlugin;
    }

    public Map<String, Sound> sounds() {
        return this.sounds;
    }

    public boolean velocity() {
        return this.velocity;
    }

    public boolean asyncActions() {
        return this.asyncActions;
    }

    public Formats moneyFormat() {
        return this.moneyFormat;
    }

    public Formats pointsFormat() {
        return this.pointsFormat;
    }

    /**
     * Builds a sound for config.
     *
     * @param type   The type of sound.
     * @param volume The volume of the sound.
     * @param pitch  The pitch of the sound.
     * @return The sound.
     */
    private Sound createSound(final String type, final float volume, final float pitch) {
        return Sound.sound(x -> x.volume(volume).pitch(pitch).type(Key.key(type)));
    }
}
