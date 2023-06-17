package org.black_ixx.bossshop.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

import static net.kyori.adventure.text.Component.text;

public class SignListener implements Listener {
    private Optional<BSShop> getBossShopSign(final String line) {
        return ClassManager.manager.getShops().getShops().values().stream().filter(x -> x.getSignText().toLowerCase().endsWith(line)).findAny();
    }

    //TODO: nuke when messaging is redone.
    @EventHandler(ignoreCancelled = true)
    public void createSign(final SignChangeEvent e) {
        if (!e.getPlayer().hasPermission("BossShop.createSign")) {
            this.sendNoPermMessage(e.getPlayer());
            e.setCancelled(true);
            return;
        }
        if (this.getBossShopSign(this.parse(e.line(0))).isEmpty()) {
            return;
        }
        for (int i = 0; i <= 3; i++) {
            String text = this.parse(e.line(i));
            if (!text.isEmpty()) {
                e.line(i, text(ClassManager.manager.getStringManager().transform(text)));
            }
        }
    }

    //TODO: signs can have writing on multiple sides.
    @EventHandler
    public void interactSign(final PlayerInteractEvent e) {
        if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK && (e.getClickedBlock() instanceof Sign sign)) {
                Optional<BSShop> shop = this.getBossShopSign(this.parse(sign.line(0)));
                shop.ifPresentOrElse(bsShop -> ClassManager.manager.getShops().openShop(e.getPlayer(), bsShop), () -> this.sendNoPermMessage(e.getPlayer()));
        }
    }

    //TODO: replace with messaging system.
    private String parse(final Component component) {
        return PlainTextComponentSerializer.plainText().serializeOr(component, "");
    }

    private void sendNoPermMessage(final Player player) {
        ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", player);
    }
}
