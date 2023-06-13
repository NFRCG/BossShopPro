package org.black_ixx.bossshop.managers.external;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class BungeeCordManager {
    public static final String PLUGIN_CHANNEL = "BossShopPro";
    public static final String PLUGIN_SUBCHANNEL_PLAYERINPUT = "PlayerInput";

    public void sendToServer(String server, Player p, BossShop plugin) {
        sendPluginMessage(p, plugin, "Connect", server);
    }

    public void sendPluginMessage(@NotNull Player p, BossShop plugin, String... args) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Arrays.stream(args).filter(Objects::nonNull).forEach(out::writeUTF);
        p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }

    public void sendShopPluginMessage(Player p, String subchannel, String argumentA, String argumentB, String argumentC) {
        sendPluginMessage(p, ClassManager.manager.getPlugin(), PLUGIN_CHANNEL, subchannel, argumentA, argumentB, argumentC);
    }

    public void executeCommand(Player p, String command) {
        sendPluginMessage(p, ClassManager.manager.getPlugin(), PLUGIN_CHANNEL, "Command", command);
    }

    public void playerInputNotification(Player p, String type, String additionalInfo) {
        sendShopPluginMessage(p, PLUGIN_SUBCHANNEL_PLAYERINPUT, p.getUniqueId().toString(), type, additionalInfo);
    }

}
