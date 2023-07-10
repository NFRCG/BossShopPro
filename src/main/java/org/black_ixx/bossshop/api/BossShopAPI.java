package org.black_ixx.bossshop.api;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSCustomActions;
import org.black_ixx.bossshop.core.BSCustomLink;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.conditions.Condition;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.black_ixx.bossshop.managers.item.ItemDataPart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unused")
public class BossShopAPI {
    private final BossShop plugin;
    private final Set<BossShopAddon> addons;

    public BossShopAPI(BossShop plugin) {
        this.plugin = plugin;
        this.addons = new HashSet<>();
    }

    /**
     * Gets the name of the addon for BossShop
     *
     * @param name the name of the addon
     * @return addon
     */
    public BossShopAddon getAddon(String name) {
        for (BossShopAddon addon : this.addons) {
            if (addon.getAddonName().equalsIgnoreCase(name)) {
                return addon;
            }
        }
        return null;
    }

    /**
     * Check if a shop is valid. For single shop.
     *
     * @param v the inventory to check
     * @return valid shop or not
     */
    public boolean isValidShop(InventoryView v) {
        return v != null && this.isValidShop(v.getTopInventory());
    }

    /**
     * Check if a shop is valid
     *
     * @param i inventory
     * @return valid or not
     */
    public boolean isValidShop(Inventory i) {
        return i != null && (i.getHolder() instanceof BSShopHolder);
    }

    /**
     * Get a BossShop Shop
     *
     * @param name the name of the shop
     * @return shop
     */
    public BSShop getShop(String name) {
        if (this.plugin.getClassManager() == null) {
            return null;
        }
        if (this.plugin.getClassManager().getShops() == null) {
            return null;
        }
        return this.plugin.getClassManager().getShops().getShop(name.toLowerCase());
    }

    /**
     * Open a shop for a player by the name of the shop
     *
     * @param p    the player to open the shop for
     * @param name the name of the shop
     */
    public void openShop(Player p, String name) {
        BSShop shop = this.getShop(name);
        if (shop == null) {
            Bukkit.getLogger().info("[API] Error: Tried to open Shop " + name + " but it was not found...");
            return;
        }
        this.openShop(p, shop);
    }

    /**
     * Opens a shop for a player by the shop instance
     *
     * @param p    the player to open for
     * @param shop the shop to open
     */
    public void openShop(Player p, BSShop shop) {
        this.plugin.getClassManager().getShops().openShop(p, shop);
    }

    /**
     * Updates the inventory for a player
     *
     * @param p player to update for
     */
    public void updateInventory(Player p) {
        this.updateInventory(p, false);
    }

    /**
     * Updates the inventory
     *
     * @param p      the player to update for
     * @param forced should it be forced
     */
    public void updateInventory(Player p, boolean forced) {
        if (this.isValidShop(p.getOpenInventory())) {
            BSShopHolder holder = (BSShopHolder) p.getOpenInventory().getTopInventory().getHolder();
            if (forced) {
                holder.getShop().openInventory(p, holder.getPage(), false);
            } else {
                holder.getShop().updateInventory(p.getOpenInventory().getTopInventory(), holder, p, ClassManager.manager, holder.getPage(), holder.getHighestPage(), false);
            }
        }
    }

    /**
     * Get the managers for the shop
     *
     * @return managers
     */
    public BSShops getShopHandler() {
        return this.plugin.getClassManager().getShops();
    }


    /**
     * Add a shop to the plugin.
     *
     * @param shop the shop to add
     */
    public void addShop(BSShop shop) {
        this.getShopHandler().addShop(shop);
    }

    /**
     * Create the next id of a shop
     *
     * @return the id
     */
    public int createNextShopId() {
        return this.getShopHandler().createId();
    }

    /**
     * Add a new item to a shop
     *
     * @param menuItem the item to add
     * @param shopItem shop item
     * @param shop     the shop to add to
     */
    public void addItemToShop(ItemStack menuItem, BSBuy shopItem, BSShop shop) {
        shop.addShopItem(shopItem, menuItem, ClassManager.manager);
    }

    /**
     * Add items to shop
     *
     * @param shop the shop to add to
     */
    public void finishedAddingItemsToShop(BSShop shop) {
        shop.finishedAddingItems();
    }


    /**
     * Register a condition type
     *
     * @param condition the condition to register.
     */
    public void registerConditionType(Condition<Player> condition) {
        throw new UnsupportedOperationException("Cannot register new conditions currently! This will be available in a future version.");
    }

    /**
     * Register a price type
     *
     * @param type the price type to register
     */
    public void registerPriceType(BSPriceType type) {
        type.register();
    }

    /**
     * Register a reward type
     *
     * @param type the reward type to register
     */
    public void registerRewardType(BSRewardType type) {
        type.register();
    }

    /**
     * Register an item data part
     *
     * @param part part to add
     */
    public void registerItemDataPart(ItemDataPart part) {
        part.register();
    }

    /**
     * Create a new item
     *
     * @param name       the name of the item
     * @param rewardType the reward type of the item
     * @param priceType  the price type of the item
     * @param reward     the reward of the item
     * @param price      the price of the item
     * @param msg        the message of the item
     * @param location   the location of the item
     * @param permission the permission of the item
     * @return created item
     */
    //Create things
    public BSBuy createBSBuy(String name, BSRewardType rewardType, BSPriceType priceType, Object reward, Object price, String msg, int location, String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, name);
    }


    /**
     * Creates a custom item
     *
     * @param name       name of item
     * @param rewardType reward type
     * @param priceType  price type
     * @param reward     reward
     * @param price      price
     * @param msg        msg
     * @param location   location
     * @param permission permission
     * @return custom item
     */
    public BSBuy createBSBuyCustom(String name, BSRewardType rewardType, BSPriceType priceType, BSCustomLink reward, Object price, String msg, int location, String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, name);
    }

    /**
     * Create a buy item
     *
     * @param rewardType reward type
     * @param priceType  price type
     * @param reward     reward
     * @param price      price
     * @param msg        msg
     * @param location   location
     * @param permission permission
     * @return buy item
     */
    public BSBuy createBSBuy(BSRewardType rewardType, BSPriceType priceType, Object reward, Object price, String msg, int location, String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, "");
    }

    public BSBuy createBSBuyCustom(BSRewardType rewardType, BSPriceType priceType, BSCustomLink reward, Object price, String msg, int location, String permission) {
        return new BSBuy(rewardType, priceType, reward, price, msg, location, permission, "");
    }

    public BSCustomLink createBSCustomLink(BSCustomActions actions, int actionId) {
        return new BSCustomLink(actionId, actions);
    }


    /**
     * Get all items in all shops
     *
     * @return Map of all items in all shops.
     */
    public Map<BSShop, List<BSBuy>> getAllShopItems() {
        Map<BSShop, List<BSBuy>> all = new HashMap<>();
        for (int i : this.plugin.getClassManager().getShops().getShops().keySet()) {
            BSShop shop = this.plugin.getClassManager().getShops().getShop(i);
            if (shop == null) {
                continue;
            }
            List<BSBuy> items = new ArrayList<>();
            for (BSBuy buy : shop.getItems()) {
                if (buy == null || buy.getItem() == null) {
                    continue;
                }
                items.add(buy);
            }
            all.put(shop, items);
        }

        return all;
    }

    /**
     * Get all items from config
     *
     * @param option The option (see comment).
     * @return Map of all items in BSConfigShop
     */
    //TODO: find out what "option" represents fully.
    public Map<BSConfigShop, List<BSBuy>> getAllShopItems(String option) {
        Map<BSConfigShop, List<BSBuy>> all = new HashMap<>();
        for (int i : this.plugin.getClassManager().getShops().getShops().keySet()) {
            BSShop shop = this.plugin.getClassManager().getShops().getShop(i);
            if (shop == null | !(shop instanceof BSConfigShop)) {
                continue;
            }
            BSConfigShop sho = (BSConfigShop) shop;
            List<BSBuy> items = new ArrayList<>();
            for (BSBuy buy : shop.getItems()) {
                if (buy == null || buy.getItem() == null) {
                    continue;
                }
                if (!buy.getConfigurationSection(sho).getBoolean(option) && buy.getConfigurationSection(sho).getInt(option) == 0) {
                    continue;
                }
                items.add(buy);
            }
            all.put(sho, items);
        }

        return all;
    }

    /**
     * Add an addon to the plugin. The addon will not be added if it has already been registered.
     *
     * @param addon the addon to add
     */
    protected void addEnabledAddon(BossShopAddon addon) {
        if (Bukkit.getPluginManager().getPlugin(addon.getAddonName()) != null) {
            this.addons.add(addon);
        }
    }

    //TODO: see if we can just change to Set
    public Set<BossShopAddon> getEnabledAddons() {
        return this.addons;
    }
}
