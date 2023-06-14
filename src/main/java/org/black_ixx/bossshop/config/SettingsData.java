package org.black_ixx.bossshop.config;

import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents data from config.yml
 */
@ConfigSerializable
@SuppressWarnings({"unused", "FieldMayBeFinal", "FieldCanBeLocal"})
public class SettingsData implements ConfigData {
    private boolean enableSigns = true;
    private String mainShop = "Menu";
    private boolean hideItemsWhenNoPermission = false;
    private boolean sellItemsWithDifferentEnchants = false;
    private boolean enableTransactionLog = false;
    //SearchSubfoldersForShops is always enabled. Will be a requirement.
    private boolean allowUnsafeEnchantments = false;
    private boolean sellDamagedItems = false;
    private boolean checkStackSize = true;
    private boolean inventoryFullDropItems = true;
    private long clickDelay = 200L;
    private int clickWarnings = 1;
    private long forgetClickSpam = 5000L;
    //MaxLineLength is removed. Onus is on the user to figure this out.
    private int inputTimeout = 45;
    private boolean sellAllPlaceholderShowFinalReward = false;
    private boolean expUseLevels = true;
    private long autoRefreshDelay = 100L;
    //TODO: display options, serverpinging, multiplier groups
    private boolean bungeecord = false;
    //TODO: make user specify the points plugin.
    private String pointsPlugin = "auto-detect";
    private Map<String, SoundSetting> sounds;

    public SettingsData() {
        //TODO: Adventure Sound Interface - Sound Serializer for configurate is provided.
        this.sounds = new HashMap<>();
        this.sounds.put("click", new SoundSetting(Sound.UI_BUTTON_CLICK.key(), 1f, 1f));
        this.sounds.put("purchase", new SoundSetting(Sound.ENTITY_PLAYER_LEVELUP.key(), 1f, 1.8f));
        this.sounds.put("noPermission", new SoundSetting(Sound.ENTITY_BLAZE_DEATH.key(), 1f, 1f));
        this.sounds.put("notEnoughMoney", new SoundSetting(Sound.ENTITY_SHULKER_HURT.key(), 1f, 0.8f));
        this.sounds.put("changeShop", new SoundSetting(Sound.UI_BUTTON_CLICK.key(), 0.2f, 1f));
        this.sounds.put("changePage", new SoundSetting(Sound.BLOCK_CHEST_OPEN.key(), 0.2f, 1f));
        this.sounds.put("open", new SoundSetting(Sound.ENTITY_ELDER_GUARDIAN_CURSE.key(), 1f, 1.8f));
        this.sounds.put("close", new SoundSetting(Sound.ENTITY_ELDER_GUARDIAN_CURSE.key(), 1f, 1.8f));
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

    public Map<String, SoundSetting> sounds() {
        return this.sounds;
    }

    @ConfigSerializable
    private record SoundSetting(Key type, float volume, float pitch) {
        public net.kyori.adventure.sound.Sound toSound() {
            return net.kyori.adventure.sound.Sound.sound(this.type, SoundCategory.AMBIENT, this.volume, this.pitch);
        }
    }
}
