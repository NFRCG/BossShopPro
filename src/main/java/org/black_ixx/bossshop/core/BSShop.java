package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.events.BSChoosePageLayoutEvent;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.features.PageLayoutHandler;
import org.black_ixx.bossshop.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class BSShop {
    public static final int ROWS_LIMIT_CURRENT = ClassManager.manager.getPageLayoutHandler().getMaxRows(); //By default 6
    public static final int ROWS_LIMIT_TOTAL = 6;
    public static final int ROW_ITEMS = 9;

    //////////////////////////// <- Variables

    private String shopName = "BossShop";
    private String signText = "[BossShop]";

    private String displayname;
    private String[] commands;

    private boolean customizable = !ClassManager.manager.getPageLayoutHandler().showIfMultiplePagesOnly(); //Automatically customizable when there special PageLayout components are shown
    private boolean displaying = false; //When displaying custom variables

    private int inventorySize = 9;
    private int manualInventoryRows;
    private int id = 0;

    private int highestPage; //Might not be correct but is used in case of a fix inventory having multiple pages


    private Set<BSBuy> items = new LinkedHashSet<>();

    //////////////////////////// <- Constructor

    public BSShop(int id, String shopName, String signText, BossShop plugin, String displayname, int manualInventoryRows, String[] commands) {
        this.id = id;
        this.shopName = shopName;
        this.signText = signText;
        this.manualInventoryRows = manualInventoryRows;
        setCommands(commands);

        setDisplayName(displayname);
    }

    public BSShop(int id) {
        this.id = id;
    }

    //////////////////////////// <- Methods to get main Variables

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String name) {
        shopName = name;
    }

    public String getDisplayName() {
        return displayname;
    }

    public void setDisplayName(String displayname) {
        if (displayname != null) {
            this.displayname = ClassManager.manager.getStringManager().transform(displayname, null, this, null, null);
            if (ClassManager.manager.getStringManager().checkStringForFeatures(this, null, null, this.displayname)) {
                customizable = true;
                displaying = true;
            }
        } else {
            this.displayname = shopName;
        }
    }

    public String getValidDisplayName(Player p, BSShopHolder holder) {
        String displayname = this.displayname;
        displayname = ClassManager.manager.getStringManager().transform(displayname, null, this, holder, p);
        return displayname.length() > 32 ? displayname.substring(0, 32) : displayname;
    }

    public String getSignText() {
        return signText;
    }

    public void setSignText(String text) {
        signText = text;
    }

    public String[] getCommands() {
        return commands;
    }

    public void setCommands(String[] commands) {
        this.commands = commands;
    }

    /**
     * Checks whether anything within the shop is player-dependent.
     *
     * @return true if shop contains anything player-dependent, like placeholders, conditions, multiple pages and more.
     */
    public boolean isCustomizable() {
        return customizable;
    }


    //////////////////////////// <- Methods to set main Variables

    public void setCustomizable(boolean b) {
        customizable = b;
    }

    /**
     * Checks whether the shop contains player-dependent placeholders.
     *
     * @return true if shop contains player-dependent placeholders.
     */
    public boolean isDisplaying() {
        return displaying;
    }

    public void setDisplaying(boolean b) {
        displaying = b;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public int getShopId() {
        return id;
    }

    public int getManualInventoryRows() {
        return manualInventoryRows;
    }

    public void setManualInventoryRows(int i) {
        this.manualInventoryRows = i;
    }

    //////////////////////////// <- Methods to get Items

    public Set<BSBuy> getItems() {
        return items;
    }

    public BSBuy getItem(String name) {
        for (BSBuy buy : items) {
            if (buy.getName().equalsIgnoreCase(name)) {
                return buy;
            }
        }
        for (BSBuy buy : items) {
            if (buy.getName().toLowerCase().startsWith(name.toLowerCase())) {
                return buy;
            }
        }
        return null;
    }


    //////////////////////////// <- Other Methods

    public void addShopItem(BSBuy buy, ItemStack menuItem, ClassManager manager) {
        buy.updateShop(this, menuItem, manager, true);
    }

    public void removeShopItem(BSBuy buy) {
        items.remove(buy);
    }


    public Inventory createInventory(Player p, ClassManager manager, int page, int highestPage, BSShopHolder oldshopholder) {
        return manager.getShopCustomizer().createInventory(this, items, p, manager, page, highestPage, oldshopholder);

    }

    public void updateInventory(Inventory i, BSShopHolder holder, Player p, ClassManager manager, int page, int highestPage, boolean autoRefresh) {
        if (holder.getPage() != page) {
            p.playSound(ClassManager.manager.getFactory().settings().sounds().get("changePage"));
        }
        holder.setPage(page);
        holder.setHighestPage(highestPage);
        if (ClassManager.manager.getStringManager().checkStringForFeatures(this, null, null, getDisplayName()) & !getValidDisplayName(p, holder).equals(p.getOpenInventory().getTitle()) & !autoRefresh) { //Title is customizable as well but shall only be updated through main thread to prevent errors
            Inventory created = manager.getShopCustomizer().createInventory(this, items, p, manager, page, highestPage, holder.getPreviousShopHolder());
            p.openInventory(created);
            return;
        }
        Inventory inventory = manager.getShopCustomizer().createInventory(this, items, p, manager, i, page, highestPage);
        if (inventory != i) {
            p.openInventory(inventory);
        }
    }

    public void loadInventorySize() {
        PageLayoutHandler layout = ClassManager.manager.getPageLayoutHandler();
        BSChoosePageLayoutEvent event = new BSChoosePageLayoutEvent(this, getShopName(), layout);
        Bukkit.getPluginManager().callEvent(event);
        layout = event.getLayout();

        if (!layout.showIfMultiplePagesOnly()) {
            inventorySize = ROW_ITEMS * layout.getMaxRows();
            return;
        }
        Set<Integer> usedSlots = new HashSet<>();
        int highest = 0;
        int uniqueSlots = 0;
        for (BSBuy b : items) {
            if (b != null) {
                if (b.getInventoryLocation() == -1) { //If picking the next slot -> increasing slot number
                    uniqueSlots++;
                } else if (!usedSlots.contains(b.getInventoryLocation())) { //if choosing specific slot -> store all different slots and add them in the end
                    usedSlots.add(b.getInventoryLocation());
                }
                if (b.getInventoryLocation() > highest) {
                    highest = b.getInventoryLocation();
                }
            }
        }
        uniqueSlots += usedSlots.size();
        inventorySize = getInventorySize(Math.max(highest, uniqueSlots - 1)); //Use either the highest slot or the number of different possible slots in order to make sure the inventory is big enough
    }

    @Deprecated
    public int getInventorySize(int i) { //i as highest slot
        i++;
        int rest = i % ROW_ITEMS;
        if (rest > 0) {
            i += ROW_ITEMS - i % ROW_ITEMS;
        }

        int maxSlotsPerPage = ROWS_LIMIT_CURRENT * ROW_ITEMS;

        if (!ClassManager.manager.getPageLayoutHandler().showIfMultiplePagesOnly() || i > maxSlotsPerPage) {
            highestPage = i / (ClassManager.manager.getPageLayoutHandler().getReservedSlotsStart() - 1); //Not tested yet!
        } else {
            highestPage = 0;
        }

        return Math.min(maxSlotsPerPage, Math.max(i, ROW_ITEMS * manualInventoryRows));
    }

    public void openInventory(Player p) {
        openInventory(p, 0, true);
    }

    public void openInventory(Player p, boolean rememberCurrentShop) {
        openInventory(p, 0, rememberCurrentShop);
    }

    public void openInventory(Player p, int page, boolean rememberCurrentShop) {
        BSShopHolder oldshopholder = null;

        if (rememberCurrentShop) {
            InventoryView openinventory = p.getOpenInventory();
            if (openinventory != null) {
                if (openinventory.getTopInventory().getHolder() instanceof BSShopHolder) {
                    oldshopholder = (BSShopHolder) openinventory.getTopInventory().getHolder();
                }
            }
        }

        ClassManager.manager.getMessageHandler().sendMessage("Main.OpenShop", p, null, p, this, null, null);
        if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
            p.playSound(ClassManager.manager.getFactory().settings().sounds().get("changeShop"));
        } else {
            p.playSound(ClassManager.manager.getFactory().settings().sounds().get("open"));
        }
        p.openInventory(createInventory(p, ClassManager.manager, page, highestPage, oldshopholder));
        ClassManager.manager.getPlayerDataHandler().openedShop(p, this);//TODO: only store previous shop, not current shop somehow.
    }

    public void close() {
        for (Player p : Bukkit.getOnlinePlayers()) { //NEW!
            if (ClassManager.manager.getPlugin().getAPI().isValidShop(p.getOpenInventory())) {
                BSShopHolder holder = ((BSShopHolder) p.getOpenInventory().getTopInventory().getHolder());
                if (holder.getShop() == this) {
                    p.closeInventory();
                }
            }
        }
    }

    public void finishedAddingItems() {
        loadInventorySize();
    }

    public abstract void reloadShop();
}
