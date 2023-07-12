package org.black_ixx.bossshop.managers;

import org.black_ixx.bossshop.config.DataFactory;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.events.BSDisplayItemEvent;
import org.black_ixx.bossshop.files.ErrorLog;
import org.black_ixx.bossshop.managers.features.PageLayoutHandler;
import org.black_ixx.bossshop.managers.item.ItemStackChecker;
import org.black_ixx.bossshop.managers.item.ItemStackTranslator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


public class ShopCustomizer {
    private final ItemStackChecker itemStackChecker;
    private final PageLayoutHandler pageLayoutHandler;
    private final ItemStackTranslator itemStackTranslator;
    private final DataFactory factory;

    public ShopCustomizer(ItemStackChecker itemStackChecker, PageLayoutHandler pageLayoutHandler, ItemStackTranslator itemStackTranslator, DataFactory factory) {
        this.itemStackChecker = itemStackChecker;
        this.pageLayoutHandler = pageLayoutHandler;
        this.itemStackTranslator = itemStackTranslator;
        this.factory = factory;
    }

    /**
     * Create a new inventory
     *
     * @param shop          the shop to create from
     * @param items         the items in the shop
     * @param p             the player opening the shop
     * @param m             the class manager
     * @param page          the page of the shop
     * @param highestPage   the highest page for the shop
     * @param oldshopholder the holder of the shop
     * @return inventory
     */
    public Inventory createInventory(BSShop shop, Set<BSBuy> items, Player p, ClassManager m, int page, int highestPage, BSShopHolder oldshopholder) {
        BSShopHolder holder = new BSShopHolder(shop, oldshopholder);
        holder.setPage(page);
        holder.setHighestPage(highestPage);
        Inventory inventory = Bukkit.createInventory(holder, shop.getInventorySize(), shop.getValidDisplayName(p, holder));

        return createInventory(shop, items, p, m, inventory, holder, page);
    }


    /**
     * Create a new inventory
     *
     * @param shop      the shop to create from
     * @param items     the items for the inventory
     * @param p         the player opening
     * @param m         the class manager
     * @param inventory the inventory being opened
     * @param page      the page of the shop
     * @return inventory
     */
    public Inventory createInventory(BSShop shop, Set<BSBuy> items, Player p, ClassManager m, Inventory inventory, int page) {
        return inventory.getHolder() instanceof BSShopHolder holder ? createInventory(shop, items, p, m, inventory, holder, page) : inventory;
    }

    /**
     * Create a new inventory
     *
     * @param shop      the shop to open as
     * @param items     the items in the shop
     * @param p         the player opening
     * @param m         the class manager
     * @param inventory the inventory to open as
     * @param holder    the holder of the inventory
     * @param page      the page of the inventory
     * @return inventory
     */
    public Inventory createInventory(BSShop shop, Set<BSBuy> items, Player p, ClassManager m, Inventory inventory, BSShopHolder holder, int page) {
        holder.setPage(page);
        Map<Integer, BSBuy> everything = new LinkedHashMap<>();
//        //Possibility for other plugins to change the layout
//        BSChoosePageLayoutEvent event = new BSChoosePageLayoutEvent(shop, shop.getShopName(), layout);
//        Bukkit.getPluginManager().callEvent(event);
//        layout = event.getLayout();
        int max_rows = this.pageLayoutHandler.getMaxRows(); //Default: 6
        int items_per_page = this.pageLayoutHandler.getReservedSlotsStart() - 1; //Default: 45
        int items_per_page_one_page = BSShop.ROW_ITEMS * max_rows; //Default: 56
        int page_slot_start = items_per_page * page;
        int page_slot_end = items_per_page * (page + 1) - 1;

        //1. Add items to (empty) everything HashMap to determine the locations
        for (BSBuy buy : items) {
            if (buy != null) {

                if (!showItem(shop, buy, p, inventory, everything)) {
                    continue;
                }

                int slot = getSlot(everything, buy);
                everything.put(slot, buy);
            }

        }

        //2. Calculate highest slot
        int highest_slot = 0;
        for (int slot : everything.keySet()) {
            highest_slot = Math.max(highest_slot, slot);
        }


        boolean full = false; //full other second page anyways

        //3. Determine whether pagelayout is needed
        if (page == 0 && highest_slot < items_per_page_one_page && this.pageLayoutHandler.showIfMultiplePagesOnly()) {
            page_slot_end = items_per_page_one_page;
        } else {
            full = true;
        }


        //4. Adding all actual items only
        HashMap<Integer, BSBuy> locs = new LinkedHashMap<>();

        for (int slot : everything.keySet()) {
            if (slot < page_slot_start || slot > page_slot_end) { //Do not show items of other pages
                continue;
            }

            BSBuy buy = everything.get(slot);
            int real_slot = slot - page_slot_start;
            locs.put(real_slot, buy);
            addItem(inventory, buy.getItem(), real_slot, shop.isDisplaying(), p, buy, shop, holder);
        }

        int highestPage = highest_slot / items_per_page; //in case the layout is active
        holder.setItems(locs, page, full ? highestPage : 0);

        //Add items here because pages get updated at the end
        if (full) {
            for (BSBuy buy : this.pageLayoutHandler.getItems()) {
                if (!showItem(shop, buy, p, inventory, locs)) {
                    continue;
                }
                if (buy.getInventoryLocation() < 0 || buy.getInventoryLocation() >= items_per_page_one_page) {
                    ErrorLog.warn("Unable to add pagelayout item '" + buy.getName() + "': Inventory location needs to be between 1 and " + items_per_page_one_page + " with 'MaxRows' set to '" + this.pageLayoutHandler.getMaxRows() + "'.");
                    continue;
                }
                locs.put(buy.getInventoryLocation(), buy);

                buy.updateShop(shop, buy.getItem(), m, false);
                addItem(inventory, buy.getItem(), buy.getInventoryLocation(), shop.isDisplaying(), p, buy, shop, holder);
            }
        }
        //Deleting old items from inventory (there is no inventory.clear at start because that would make the inventory blink on every refresh)
        for (int i = 0; i < inventory.getSize(); i++) {
            if (!locs.containsKey(i)) {
                inventory.setItem(i, null);
            }
        }
        return inventory;
    }


    /**
     * Add an item to an inventory
     *
     * @param inventory  the inventory to add to
     * @param item       the item to add
     * @param slot       the slot of the item
     * @param displaying display the item or not
     * @param p          the player opening inventory
     * @param buy        shop item
     * @param shop       the shop added to
     * @param holder     the holder of the shop
     */
    private void addItem(Inventory inventory, ItemStack item, int slot, boolean displaying, Player p, BSBuy buy, BSShop shop, BSShopHolder holder) {
        if (displaying && p != null) {
            if (item.hasItemMeta()) {
                if (!buy.isItemFix()) {
                    item = this.itemStackTranslator.translateItemStack(buy, shop, holder, item.clone(), p);
                }
            }
        }

        ItemStack current = inventory.getItem(slot);
        if (current != null) {
            if (current.getType() == Material.PLAYER_HEAD) {
                if (this.itemStackChecker.isEqualShopItemAdvanced(item, current, true, p)) {
                    return;
                }
            }
        }
        inventory.setItem(slot, item);
    }


    /**
     * Show the item in an inventory
     *
     * @param shop        the shop to get from
     * @param buy         the shop item
     * @param p           the player opening the shop
     * @param inventory   the invetory of the shop
     * @param filled_locs the filled locations of the shop
     * @return show or not
     */
    public boolean showItem(BSShop shop, BSBuy buy, Player p, Inventory inventory, Map<Integer, BSBuy> filled_locs) {
        if (filled_locs.containsKey(buy.getInventoryLocation())) {
            if (filled_locs.get(buy.getInventoryLocation()) == buy) {
                //Same item checking for being added again. Probably for an item refresh? Allow!
            } else {
                return false; //Different items do not have space here
            }
        }

        if (p != null) {
            if (this.factory.settings().hideItemsWhenNoPermission() && !buy.hasPermission(p, false, null)) {
                return false;
            }

            if (!buy.meetsCondition(p)) {
                return false;
            }

            BSDisplayItemEvent event = new BSDisplayItemEvent(p, shop, buy, inventory);
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get a slot position
     *
     * @param everything all items loaded in the inventory
     * @param buy        the item being checked
     * @return slot of item
     */
    public int getSlot(Map<Integer, BSBuy> everything, BSBuy buy) {
        if (buy.getInventoryLocation() == -1) {
            return getFirstFreeSlot(everything);
        }
        return buy.getInventoryLocation();
    }

    /**
     * Get the first free slot in an inventory
     *
     * @param everything the items loaded in the inventory
     * @return first free slot
     */
    public int getFirstFreeSlot(Map<Integer, BSBuy> everything) {
        for (int i = 0; i < 5000; i++) {
            if (!everything.containsKey(i)) {
                return i;
            }
        }
        return -1;
    }
}
