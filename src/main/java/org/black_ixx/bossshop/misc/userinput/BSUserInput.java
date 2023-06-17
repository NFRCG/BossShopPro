package org.black_ixx.bossshop.misc.userinput;

import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class BSUserInput {


    /**
     * Get user input from anvil
     * @param p the player to check
     * @param chat_text the chat text
     */
    public void getUserInput(Player p, String chat_text) { //Might not receive input for sure
        ClassManager.manager.getPlayerDataHandler().requestInput(p, new BSChatUserInput(p, this, ClassManager.manager.getFactory().settings().inputTimeout() * 1000L));
        ClassManager.manager.getMessageHandler().sendMessageDirect(ClassManager.manager.getStringManager().transform(chat_text, p), p);
        p.closeInventory();
    }

    public abstract void receivedInput(Player p, String text);


    public boolean supportsAnvils() {
        return false; //Anvils are currently not working & when they are check for server version here
    }

}
