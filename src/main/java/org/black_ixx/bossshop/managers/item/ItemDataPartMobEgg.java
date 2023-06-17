package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class ItemDataPartMobEgg extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        item.setType(Material.valueOf(argument + "_SPAWN_EGG"));
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_MOST_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"monsteregg", "mobegg", "spawnegg"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, BSBuy buy, Player p) {
        return shopItem.equals(playerItem);
    }
}
