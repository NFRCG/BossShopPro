package org.black_ixx.bossshop.misc.userinput;

import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BSChatUserInput {
    private final BSUserInput input;
    private final long time;

    public BSChatUserInput(final Player p, final BSUserInput input, final long time) {
        this.input = input;
        this.time = System.currentTimeMillis() + time;
        if (ClassManager.manager.getFactory().settings().bungeecord()) {
            ClassManager.manager.getBungeeCordManager().playerInputNotification(p, "start", String.valueOf(this.time));
        }
    }

    /**
     * Check if this.time is up to date
     * @return up to date
     */
    public boolean isUpToDate() {
        return this.time > System.currentTimeMillis();
    }

    /**
     * Input received
     * @param p the player to check
     * @param text string
     */
    public void input(Player p, String text) {
        this.input.receivedInput(p, text);
        if (ClassManager.manager.getFactory().settings().bungeecord()) {
            ClassManager.manager.getBungeeCordManager().playerInputNotification(p, "end", null);
        }
    }
}
