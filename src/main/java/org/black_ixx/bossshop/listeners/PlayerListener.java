package org.black_ixx.bossshop.listeners;


import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.features.PlayerDataHandler;
import org.black_ixx.bossshop.misc.userinput.BSChatUserInput;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.stream.Stream;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void shopCommand(final PlayerCommandPreprocessEvent e) {
        Player player = e.getPlayer();
        BSShops shops = ClassManager.manager.getShops();
        if (shops == null) {
            return;
        }
        BSShop shop = shops.getShopByCommand(e.getMessage().substring(1));
        //TODO: probably change this but works for now.
        Stream.of("BossShop.open", "BossShop.open.command", "BossShop.open.command." + shop.getShopName()).filter(player::hasPermission).findAny().ifPresentOrElse(x -> shops.openShop(player, shop), () -> ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", player));
        e.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void chat(final AsyncChatEvent e) {
        PlayerDataHandler handler = ClassManager.manager.getPlayerDataHandler();
        Player player = e.getPlayer();
        BSChatUserInput input = handler.getInputRequest(player);
        if (input != null) {
            if (input.isUpToDate()) {
                input.input(player, PlainTextComponentSerializer.plainText().serialize(e.message()));
            } else {
                handler.removeInputRequest(player);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        ClassManager.manager.getPlayerDataHandler().leftServer(e.getPlayer());
    }

    @EventHandler
    public void onPlayerKick(final PlayerKickEvent e) {
        ClassManager.manager.getPlayerDataHandler().leftServer(e.getPlayer());
    }
}
