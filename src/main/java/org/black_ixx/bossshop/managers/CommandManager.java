package org.black_ixx.bossshop.managers;


import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.config.DataFactory;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.managers.features.ItemDataStorage;
import org.black_ixx.bossshop.managers.features.PlayerDataHandler;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.List;

public class CommandManager implements CommandExecutor {
    private final BossShop plugin;
    private final MessageHandler messageHandler;
    private final ItemDataStorage itemDataStorage;
    private final PlayerDataHandler playerDataHandler;
    private final BSShops shops;
    private final DataFactory factory;

    @Inject
    public CommandManager(BossShop plugin, MessageHandler messageHandler, ItemDataStorage itemDataStorage, PlayerDataHandler playerDataHandler, final BSShops shops, DataFactory factory) {
        this.plugin = plugin;
        this.messageHandler = messageHandler;
        this.itemDataStorage = itemDataStorage;
        this.playerDataHandler = playerDataHandler;
        this.shops = shops;
        this.factory = factory;
    }

    //TODO: nuke
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("bossshop") || cmd.getName().equalsIgnoreCase("shop") || cmd.getName().equalsIgnoreCase("bs")) {

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {

                    if (sender.hasPermission("BossShop.reload")) {

                        sender.sendMessage(ChatColor.YELLOW + "Starting BossShop reload...");
                        this.plugin.reloadPlugin(sender);
                        sender.sendMessage(ChatColor.YELLOW + "Done!");

                    } else {
                        this.messageHandler.sendMessage("Main.NoPermission", sender);
                        return false;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("read")) {
                    if (sender instanceof Player) {
                        if (sender.hasPermission("BossShop.read")) {
                            Player p = (Player) sender;
                            ItemStack item = Misc.getItemInMainHand(p);
                            if (item == null || item.getType() == Material.AIR) {
                                this.messageHandler.sendMessage("Main.NeedItemInHand", sender);
                                return false;
                            }
                            List<String> itemdata = ItemDataPart.readItem(item);
                            this.itemDataStorage.addItemData(p.getName(), itemdata);
                            this.messageHandler.sendMessage("Main.PrintedItemInfo", sender);
                            for (String line : itemdata) {
                                sender.sendMessage("- " + line);
                            }
                            return true;
                        } else {
                            this.messageHandler.sendMessage("Main.NoPermission", sender);
                            return false;
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("simulate")) {
                    if (sender.hasPermission("BossShop.simulate")) {
                        if (args.length == 4) {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                this.messageHandler.sendMessage("Main.PlayerNotFound", sender, args[1]);
                                return false;
                            }

                            BSShop shop = this.shops.getShop(args[2]);
                            if (shop == null) {
                                this.messageHandler.sendMessage("Main.ShopNotExisting", sender, null, p, null, null, null);
                                return false;
                            }

                            BSBuy buy = shop.getItem(args[3]);
                            if (buy == null) {
                                this.messageHandler.sendMessage("Main.ShopItemNotExisting", sender, null, p, shop, null, null);
                                return false;
                            }

                            buy.click(p, shop, null, null, null, this.plugin);
                            return true;
                        }
                        sendCommandList(sender);
                        return false;
                    }
                }

                if (args[0].equalsIgnoreCase("close")) {
                    if (sender.hasPermission("BossShop.close")) {
                        Player p = null;
                        String name = sender instanceof Player ? sender.getName() : "CONSOLE";

                        if (sender instanceof Player) {
                            p = (Player) sender;
                        }
                        if (args.length >= 2) {
                            name = args[1];
                            p = Bukkit.getPlayer(name);
                        }

                        if (p == null) {
                            this.messageHandler.sendMessage("Main.PlayerNotFound", sender, name);
                            return false;
                        }

                        p.closeInventory();
                        if (p != sender) {
                            this.messageHandler.sendMessage("Main.CloseShopOtherPlayer", sender, p);
                        }

                    } else {
                        this.messageHandler.sendMessage("Main.NoPermission", sender);
                        return false;
                    }
                    return true;
                }

                if (args.length >= 3 && args[0].equalsIgnoreCase("open")) {

                    if (args.length < 2) {
                        sendCommandList(sender);
                        return false;
                    }

                    String shopname = args[1].toLowerCase();
                    BSShop shop = this.shops.getShop(shopname);
                    String name = args[2];
                    Player p = Bukkit.getPlayerExact(name);
                    String argument = args.length > 3 ? args[3] : null;

                    if (p == null) {
                        p = Bukkit.getPlayer(name);
                    }

                    if (p == null) {
                        this.messageHandler.sendMessage("Main.PlayerNotFound", sender, name, null, shop, null, null);
                        return false;
                    }

                    if (shop == null) {
                        this.messageHandler.sendMessage("Main.ShopNotExisting", sender, null, p, shop, null, null);
                        return false;
                    }

                    playerCommandOpenShop(sender, p, shopname, argument);
                    if (p != sender) {
                        this.messageHandler.sendMessage("Main.OpenShopOtherPlayer", sender, null, p, shop, null, null);
                    }

                    return true;
                }

            }

            if (sender instanceof Player) {
                Player p = (Player) sender;

                String shop = this.factory.settings().mainShop();
                if (args.length != 0) {
                    shop = args[0].toLowerCase();
                }
                String argument = args.length > 1 ? args[1] : null;
                playerCommandOpenShop(sender, p, shop, argument);
                return true;
            }
            sendCommandList(sender);
            return false;
        }

        return false;
    }


    private boolean playerCommandOpenShop(CommandSender sender, Player target, String shop, String argument) {
        if (sender == target) {
            if (!(sender.hasPermission("BossShop.open") || sender.hasPermission("BossShop.open.command") || sender.hasPermission("BossShop.open.command." + shop))) {
                this.messageHandler.sendMessage("Main.NoPermission", sender);
                return false;
            }
        } else {
            if (!sender.hasPermission("BossShop.open.other")) {
                this.messageHandler.sendMessage("Main.NoPermission", sender);
                return false;
            }
        }
        if (argument != null) {
            this.playerDataHandler.enteredInput(target, argument);
        }
        if (this.shops == null) {
            return false;
        }
        this.shops.openShop(target, shop);
        return true;
    }

    private void sendCommandList(CommandSender s) {
        s.sendMessage(ChatColor.RED + "/BossShop - Opens  main shop");
        s.sendMessage(ChatColor.RED + "/BossShop <shop> [input] - Opens named shop");
        s.sendMessage(ChatColor.RED + "/BossShop open <Shop> <Player> [input] - Opens named shop for the named player");
        s.sendMessage(ChatColor.RED + "/BossShop close [Player] - Closes inventory of the named player");
        s.sendMessage(ChatColor.RED + "/BossShop simulate <player> <shop> <shopitem> - Simulates click");
        s.sendMessage(ChatColor.RED + "/BossShop reload - Reloads the Plugin");
        if (s instanceof Player) {
            s.sendMessage(ChatColor.RED + "/BossShop read - Prints out itemdata of item in main hand");
        }
    }

}
