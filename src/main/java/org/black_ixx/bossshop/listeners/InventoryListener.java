package org.black_ixx.bossshop.listeners;

import jakarta.inject.Inject;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.config.DataFactory;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.MessageHandler;
import org.black_ixx.bossshop.managers.misc.StringManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Handles all inventory event listeners.
 */
//TODO: ?????????????
public class InventoryListener implements Listener {
    private final BossShop plugin;
    private final DataFactory factory;
    private final WeakHashMap<Player, Long> clickTimes;
    private final WeakHashMap<Player, Integer> violations;
    private final long delay;
    private final long warnings;
    private final long memory;
    private final List<InventoryAction> actions;
    private final MessageHandler messageHandler;
    private final StringManager stringManager;

    @Inject
    public InventoryListener(final BossShop plugin, final DataFactory factory, final MessageHandler messageHandler, final StringManager stringManager) {
        this.plugin = plugin;
        this.factory = factory;
        this.messageHandler = messageHandler;
        this.stringManager = stringManager;
        this.clickTimes = new WeakHashMap<>();
        this.violations = new WeakHashMap<>();
        this.delay = this.factory.settings().clickDelay();
        this.warnings = this.factory.settings().clickWarnings();
        this.memory = this.factory.settings().forgetClickSpam();
        this.actions = List.of(InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_SLOT,
                InventoryAction.HOTBAR_MOVE_AND_READD,
                InventoryAction.HOTBAR_SWAP,
                InventoryAction.PICKUP_ALL,
                InventoryAction.PICKUP_HALF,
                InventoryAction.PICKUP_ONE,
                InventoryAction.PICKUP_SOME,
                InventoryAction.PLACE_ALL,
                InventoryAction.PLACE_ONE,
                InventoryAction.PLACE_SOME,
                InventoryAction.SWAP_WITH_CURSOR);
    }

    @EventHandler
    public void closeShop(final InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof BSShopHolder holder && e.getPlayer() instanceof Player player) {
            this.messageHandler.sendMessage(this.factory.messages().closeShop(), player, null, player, holder.getShop(), holder, null);
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                if (!this.plugin.getAPI().isValidShop(player.getOpenInventory())) {
                    player.playSound(this.factory.settings().sounds().get("close"));
                }
            });
        }
    }

    /**
     * if player doesnt have perm
     * check map for last time clicked.
     * if the delay + last click time is less than current time, add a violation to the player.
     * if the new violation amount is more than or equal to the configured warning count, kick the player.
     * else, send warning message to player.
     * if last click time and memory is less than current system time, remove the user from the violations map.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void purchase(final InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof BSShopHolder holder) || !(e.getWhoClicked() instanceof Player player)) {
            return;
        }
        ClickType clickType = e.getClick();
        BSBuy buy = holder.getShopItem(e.getRawSlot());
        if (e.getCurrentItem() == null || clickType == ClickType.DOUBLE_CLICK || e.getCursor() == null || e.getSlotType() == SlotType.QUICKBAR || buy == null) {
            return;
        }
        if (!this.actions.contains(e.getAction())) {
            e.setCancelled(true);
            e.setResult(Result.DENY);
        }
        BSShop shop = holder.getShop();
        if (player.hasPermission("BossShop.bypass")) {
            buy.click(player, shop, holder, clickType, e, this.plugin);
            return;
        }
        long click = this.clickTimes.containsKey(player) ? this.clickTimes.get(player) : System.currentTimeMillis();
        if (System.currentTimeMillis() < click + this.delay) {
            int count = this.violations.get(player) == null ? 1 : this.violations.get(player) + 1;
            this.violations.put(player, count);
        }
        if (this.violations.get(player) >= this.warnings) {
            player.kickPlayer(this.messageHandler.get("Main.OffensiveClickSpamKick"));
        } else {
            double timeLeft = this.clickTimes.get(player) + (double) (this.delay - System.currentTimeMillis());
            timeLeft = Math.max(0.1f, timeLeft / 1000);
            this.messageHandler.sendMessageDirect(this.stringManager.transform(this.messageHandler.get("Main.ClickSpamWarning").replace("%time_left%", String.valueOf(timeLeft)), buy, shop, holder, player), player);
        }
        if (click + this.memory < System.currentTimeMillis()) {
            this.violations.put(player, 0);
        }
        buy.click(player, shop, holder, clickType, e, this.plugin);
    }

    @EventHandler
    public void drag(final InventoryDragEvent event) {
        if (!(event.getInventory().getHolder() instanceof BSShopHolder)) {
            return;
        }
        //When there is a good way to detect whether the upper inventory has been affected by the drag event or not then please inbuilt it and only cancel the event in that case
        event.setCancelled(true);
        event.setResult(Result.DENY);
    }

    @EventHandler
    public void quit(final PlayerQuitEvent e) {
        Player player = e.getPlayer();
        this.clickTimes.remove(player);
        this.violations.remove(player);
    }

    @EventHandler
    public void kick(final PlayerKickEvent e) {
        Player player = e.getPlayer();
        this.clickTimes.remove(player);
        this.violations.remove(player);
    }
}
