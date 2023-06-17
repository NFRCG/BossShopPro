package org.black_ixx.bossshop.core.rewards;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.external.BungeeCordManager;
import org.black_ixx.bossshop.managers.misc.InputReader;
import org.black_ixx.bossshop.managers.misc.StringManipulationLib;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class BSRewardTypeBungeeCordCommand extends BSRewardType {


    public Object createObject(Object o, boolean forceState) {
        return InputReader.readStringList(o);
    }

    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ClassManager.manager.getBugFinder().severe("Was not able to create ShopItem " + item_name + "! The reward object needs to be a list of commands (text lines).");
        return false;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean message_if_no_success, Object reward, ClickType clickType) {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        List<String> commands = (List<String>) reward;

        BungeeCordManager bc = ClassManager.manager.getBungeeCordManager();

        if (bc != null) {

            for (String s : commands) {
                bc.executeCommand(p, ClassManager.manager.getStringManager().transform(s, buy, buy.getShop(), null, p));
            }

        } else {
            ClassManager.manager.getBugFinder().severe("Was not able to execute BungeeCord commands: The BungeeCordManager was not enabled properly.");
        }


    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        @SuppressWarnings("unchecked")
        List<String> commands = (List<String>) reward;
        String commands_formatted = StringManipulationLib.formatList(commands);
        return ClassManager.manager.getMessageHandler().get("Display.BungeeCordCommand").replace("%commands%", commands_formatted);
    }

    @Override
    public String[] createNames() {
        return new String[]{"bungeecordcommand", "bungeecordcommands", "bccommand", "bccommands"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

}
