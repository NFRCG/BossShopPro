package org.black_ixx.bossshop.managers;


import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSCreateShopItemEvent;
import org.black_ixx.bossshop.events.BSCreatedShopItemEvent;
import org.black_ixx.bossshop.events.BSLoadShopItemEvent;
import org.black_ixx.bossshop.managers.features.BugFinder;
import org.black_ixx.bossshop.managers.item.ItemStackCreator;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class BuyItemHandler {
    private final BugFinder bugFinder;
    private final ItemStackCreator itemStackCreator;

    @Inject
    public BuyItemHandler(BugFinder bugFinder, ItemStackCreator itemStackCreator) {
        this.bugFinder = bugFinder;
        this.itemStackCreator = itemStackCreator;
    }

    /**
     * Load an item from a config into a shop
     *
     * @param items_section the config section for the item
     * @param shop          the shop to load it into
     * @param name          the name of the config
     * @return shop item
     */
    public BSBuy loadItem(ConfigurationSection items_section, BSShop shop, String name) {
        if (items_section.getConfigurationSection(name) == null) {
            String shopname = shop == null ? "none" : shop.getShopName();
            this.bugFinder.severe("Error when trying to create BuyItem " + name + "! (1) [Shop: " + shopname + "]");
            return null;
        }
        ConfigurationSection c = items_section.getConfigurationSection(name);
        BSLoadShopItemEvent event = new BSLoadShopItemEvent(shop, name, c);
        Bukkit.getPluginManager().callEvent(event); //Allow addons to create a custom BSBuy
        BSBuy buy = event.getCustomShopItem();
        if (buy == null) { //If addons did not create own item create a default one here!
            buy = createBuyItem(shop, name, c);
            if (buy == null) {
                return null;
            }
        }
        if (shop != null) {
            shop.addShopItem(buy, buy.getItem(), ClassManager.manager);
        }
        return buy;
    }

    /**
     * Create item to buy
     *
     * @param shop name of shop to load from
     * @param name name of item
     * @param c    part of config to get from
     * @return shop item
     */
    public BSBuy createBuyItem(BSShop shop, String name, ConfigurationSection c) {
        String stage = "Basic Data";
        String shopname = shop == null ? "none" : shop.getShopName();

        try {
            String priceType = c.getString("PriceType");
            String rewardType = c.getString("RewardType");
            String message = c.getString("Message");
            String permission = c.getString("ExtraPermission");
            if (permission == null || permission == "") {
                permission = null;
            }
            int inventoryLocation = c.getInt("InventoryLocation");

            if (inventoryLocation < 0) {
                this.bugFinder.warn("The InventoryLocation of the shopitem '" + name + "' is '" + inventoryLocation + "'. It has to be either higher than '0' or it has to be '0' if you want to it to automatically pick the next empty slot. [Shop: " + shopname + "]");
            }
            inventoryLocation--;

            stage = "Price- and RewardType Detection";

            BSRewardType rewardT = BSRewardType.detectType(rewardType);
            BSPriceType priceT = BSPriceType.detectType(priceType);

            if (rewardT == null) {
                this.bugFinder.severe("Was not able to create shopitem '" + name + "'! '" + rewardType + "' is not a valid RewardType! [Shop: " + shopname + "]");
                this.bugFinder.severe("Valid RewardTypes:");
                for (BSRewardType type : BSRewardType.values()) {
                    this.bugFinder.severe("-" + type.name());
                }
                return null;
            }

            if (priceT == null) {
                this.bugFinder.severe("Was not able to create shopitem '" + name + "'! '" + priceType + "' is not a valid PriceType! [Shop: " + shopname + "]");
                this.bugFinder.severe("Valid PriceTypes:");
                for (BSPriceType type : BSPriceType.values()) {
                    this.bugFinder.severe("-" + type.name());
                }
                return null;
            }

            stage = "ForceInput Detection";
            BSInputType inputtype = null;
            String inputtypename = c.getString("ForceInput");
            String inputtext = c.getString("ForceInputMessage");
            if (inputtypename != null) {
                for (BSInputType it : BSInputType.values()) {
                    if (it.name().equalsIgnoreCase(inputtypename)) {
                        inputtype = it;
                        break;
                    }
                }
                if (inputtype == null) {
                    this.bugFinder.warn("Invalid ForceInput type: '" + inputtypename + "' of shopitem '" + name + ". [Shop: " + shopname + "]");
                }
            }


            stage = "Price- and RewardType Enabling";
            rewardT.enableType();
            priceT.enableType();


            Object price = c.get("Price");
            Object reward = c.get("Reward");

            stage = "Price- and RewardType Adaption";
            price = priceT.createObject(price, true);
            reward = rewardT.createObject(reward, true);

            if (!priceT.validityCheck(name, price)) {
                return null;
            }
            if (!rewardT.validityCheck(name, reward)) {
                return null;
            }


            stage = "Optional: Conditions";
            //TODO: impl conditions at this stage. null is passed to compile.
            BSCreateShopItemEvent event = new BSCreateShopItemEvent(shop, name, c, rewardT, priceT, reward, price, message, inventoryLocation, permission, null, inputtype, inputtext);
            Bukkit.getPluginManager().callEvent(event); //Allow addons to create a custom BSBuy

            BSBuy buy = event.getCustomShopItem();
            if (buy == null) { //If addons did not create own item create a default one here!
                buy = new BSBuy(rewardT, priceT, reward, price, message, inventoryLocation, permission, name, null, inputtype, inputtext);
            }
            buy.setShop(shop);


            stage = "MenuItem creation";
            if (c.getStringList("MenuItem") == null) {
                this.bugFinder.severe("Error when trying to create shopitem " + name + "! MenuItem is not existing?! [Shop: " + shopname + "]");
                return null;
            }

            ItemStack i = this.itemStackCreator.createItemStack(c.getStringList("MenuItem"), buy, shop);
            buy.setItem(i, false);

            //TODO: figure out what this does
            //ClassManager.manager.getSettings().update(buy); //TODO: Not tested if inheritance works fine yet. Order of methods matters!

            Bukkit.getPluginManager().callEvent(new BSCreatedShopItemEvent(shop, buy, c));
            return buy;

        } catch (Exception e) {
            this.bugFinder.severe("Was not able to create BuyItem " + name + "! Error at Stage '" + stage + "'. [Shop: " + shopname + "]");
            e.printStackTrace();
            this.bugFinder.severe("Probably caused by Config Mistakes.");
            this.bugFinder.severe("For more help please send me a PM at Spigot.");
            return null;
        }
    }
}
