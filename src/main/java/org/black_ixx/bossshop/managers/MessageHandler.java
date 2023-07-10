package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageHandler {
    private final BossShop plugin;
    private final String fileName = "messages.yml";
    private final File file;
    private FileConfiguration config;
    private final StringManager stringManager;

    @Inject
    public MessageHandler(final BossShop plugin, final StringManager stringManager) {
        this.plugin = plugin;
        this.stringManager = stringManager;
        this.file = new File(plugin.getDataFolder().getAbsolutePath(), this.fileName);
        this.config = YamlConfiguration.loadConfiguration(this.file);
    }

    /**
     * Get the config file
     * @return config
     */
    public FileConfiguration getConfig() {
        if (this.config == null)
            reloadConfig();

        return this.config;
    }

    /**
     * Reload the config file
     */
    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        InputStream defConfigStream = this.plugin.getResource(this.fileName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            this.config.setDefaults(defConfig);
        }
    }

    /**
     * Send message from config to player
     * @param node path of message
     * @param sender sender to send to
     */
    public void sendMessage(String node, CommandSender sender) {
        sendMessage(node, sender, null, null, null, null, null);
    }

    /**
     * Send message from config to player
     * @param node path of message
     * @param sender sender to send to
     * @param offline_target offline target
     */
    public void sendMessage(String node, CommandSender sender, String offline_target) {
        sendMessage(node, sender, offline_target, null, null, null, null);
    }

    /**
     * Send message from config to player
     * @param node path of message
     * @param sender sender to send to
     * @param target player target
     */
    public void sendMessage(String node, CommandSender sender, Player target) {
        sendMessage(node, sender, null, target, null, null, null);
    }

    /**
     * Send a message to a player
     * @param node the path of message
     * @param sender the sender to send to
     * @param offline_target offline target
     * @param target player target
     * @param shop shop to send to
     * @param holder the holder of the shop
     * @param item the item in the shop
     */
    public void sendMessage(String node, CommandSender sender, String offline_target, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        if (sender != null) {

            if (node == null || node == "") {
                return;
            }

            String message = get(node, target, shop, holder, item);

            if (message == null || message.isEmpty() || message.length() < 2) {
                return;
            }

            if (offline_target != null) {
                message = message.replace("%player%", offline_target).replace("%name%", offline_target).replace("%target%", offline_target);
            }

            sendMessageDirect(message, sender);
        }
    }

    /**
     * Send message directly to CommandSender
     * @param message the message to sender
     * @param sender the sender to send to
     */
    public void sendMessageDirect(String message, CommandSender sender) {
        if (sender != null) {

            if (message == null || message.length() < 2) {
                return;
            }

            String colors = "";
            for (String line : message.split("\\\\n")) {
                sender.sendMessage(colors + line);
                colors = ChatColor.getLastColors(line);
            }
        }
    }

    /**
     * Get a string from the config
     * @param node path of node
     * @return string
     */
    public String get(String node) {
        return get(node, null, null, null, null);
    }

    private String get(String node, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        return replace(this.config.getString(node, node), target, shop, holder, item);
    }

    private String replace(String message, Player target, BSShop shop, BSShopHolder holder, BSBuy item) {
        return this.stringManager.transform(message, item, shop, holder, target);
    }


}