package org.black_ixx.bossshop.managers.item;


import org.black_ixx.bossshop.StringUtil;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.files.ErrorLog;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ItemStackCreator {
    private final ItemStackTranslator itemStackTranslator;
    private final ItemStackChecker itemStackChecker;

    @Inject
    public ItemStackCreator(ItemStackTranslator itemStackTranslator, ItemStackChecker itemStackChecker) {
        this.itemStackTranslator = itemStackTranslator;
        this.itemStackChecker = itemStackChecker;
    }

    public ItemStack createItemStack(List<String> itemData, BSBuy buy, BSShop shop) { //This allows to work with %rewarditem_<id>% and %priceitem_<id>%
        if (shop instanceof BSConfigShop) {
            BSConfigShop cshop = (BSConfigShop) shop;
            List<String> new_list = null;
            for (String line : itemData) {

                String reward_line = this.figureOutVariable(line, "rewarditem", 0);
                if (reward_line != null) {
                    if (new_list == null) {
                        new_list = cloneList(itemData);
                    }
                    int i = StringUtil.getInt(reward_line, -1) - 1;
                    new_list = transform(line, i, new_list, buy, cshop, "Reward");
                }

                String price_line = this.figureOutVariable(line, "priceitem", 0);
                if (price_line != null) {
                    if (new_list == null) {
                        new_list = cloneList(itemData);
                    }
                    int i = StringUtil.getInt(reward_line, -1) - 1;
                    new_list = transform(line, i, new_list, buy, cshop, "Price");
                }

                if (new_list != null) {
                    return createItemStack(new_list);
                }

            }


        }

        return createItemStack(itemData);
    }

    private List<String> transform(String line, int index, List<String> new_list, BSBuy buy, BSConfigShop shop, String path) {
        if (index != -1) {
            new_list.remove(line);

            List<List<String>> list = StringUtil.readStringListList(buy.getConfigurationSection(shop).get(path));
            if (list != null) {
                if (list.size() > index) {
                    for (String entry : list.get(index)) {
                        new_list.add(entry);
                    }
                } else {
                    ErrorLog.warn("Was trying to import the item look for MenuItem of shopitem '" + buy.getName() + "' in shop '" + shop.getShopName() + "' but your " + path + " does not contain a " + index + ". item!");
                }
            } else {
                ErrorLog.warn("Was trying to import the item look for MenuItem of shopitem '" + buy.getName() + "' in shop '" + shop.getShopName() + "' but your " + path + " is not an item list!");
            }
        }
        return new_list;
    }

    private List<String> cloneList(List<String> list) {
        if (list != null & !list.isEmpty()) {
            List<String> clone = new ArrayList<>();
            for (String line : list) {
                clone.add(line);
            }
            return clone;
        }
        return list;
    }

    public ItemStack createItemStack(List<String> itemData) {
        ItemStack i = new ItemStack(Material.STONE);

        itemData = Misc.fixLore(itemData);

        i = ItemDataPart.transformItem(i, itemData);
        this.itemStackTranslator.translateItemStack(null, null, null, i, null);
        return i;
    }

    /**
     * Gives the selected item to the player.
     * If clone_item = false the item is modified (placeholders adapted to player and amount changed).
     *
     * @param p          Player to give the item to.
     * @param buy        Shopitem linked to the item.
     * @param i          Item to add to the player.
     * @param amount     Amount of the item to add to the player.
     * @param clone_item Whether the item selected can be modified or if a clone of the selected item should be used instead, keeping the original item unchanged.
     */
    public void giveItem(Player p, BSBuy buy, ItemStack i, int amount, boolean clone_item) {
        if (clone_item) {
            i = i.clone();
        }

        int to_give = amount;
        int stacksize = this.itemStackChecker.getMaxStackSize(i);

        //First of all translate item
        i = this.itemStackTranslator.translateItemStack(buy, null, null, (clone_item ? i.clone() : i), p);

        while (to_give > 0) {
            i.setAmount(Math.min(stacksize, to_give));
            giveItem(p, i.clone(), stacksize);
            to_give -= i.getAmount();
        }
    }


    /**
     * Gives an unmodified version of the item to the player.
     *
     * @param p         Player to give the item to.
     * @param i         ItemStack to give to the player.
     * @param stacksize Maximum stack size determined using {@link ItemStackChecker#getMaxStackSize(ItemStack)}.
     * @pre The item needs to be translated and adapted to the player already.
     */
    private void giveItem(Player p, ItemStack i, int stacksize) {
        if (i.getAmount() > stacksize) {
            throw new RuntimeException("Can not give an itemstack with a higher amount than the maximum stack size at once to a player.");
        }
        int free = this.itemStackChecker.getAmountOfFreeSpace(p, i);

        if (free < i.getAmount()) { //Not enough space?
            dropItem(p, i, stacksize);
        } else {
            p.getInventory().addItem(i);
        }
    }

    private void dropItem(Player p, ItemStack i, int stacksize) {
        int toTake = i.getAmount();
        int amount;
        i = i.clone();

        while (toTake > 0) {
            amount = Math.min(toTake, stacksize);
            i.setAmount(amount);
            p.getWorld().dropItem(p.getLocation(), i);// Drop Item!
            toTake -= amount;
        }
    }

    private String figureOutVariable(String s, String name, int fromIndex) {
        String symbol = "%";
        String start = symbol + name + "_";
        String complete = getCompleteVariable(s, name, fromIndex);
        if (complete != null) {
            String variable = complete.substring(start.length(), complete.length() - symbol.length());
            return variable;
        }
        return null;
    }

    private String getCompleteVariable(String s, String name, int fromIndex) {
        String symbol = "%";
        String start = symbol + name + "_";
        if (s.contains(start)) {
            int firstStart = s.indexOf(start, fromIndex);
            if (firstStart != -1) {
                int firstEnd = s.indexOf(symbol, firstStart + 1);
                if (firstEnd != -1) {
                    return s.substring(firstStart, firstEnd + 1);
                }
            }
        }
        return null;
    }
}
