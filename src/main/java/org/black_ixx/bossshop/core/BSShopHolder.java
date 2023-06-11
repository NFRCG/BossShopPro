package org.black_ixx.bossshop.core;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;
import java.util.Map;

public class BSShopHolder implements InventoryHolder {

    private BSShopHolder previousShopHolder;
    private BSShop shop;
    private int page, highestPage;
    private Map<Integer, BSBuy> items;
    public BSShopHolder(BSShop shop, Map<Integer, BSBuy> items) {
        this.shop = shop;
        this.items = items;
    }
    public BSShopHolder(BSShop shop, BSShopHolder previousShopHolder) {
        this(shop);
        this.previousShopHolder = previousShopHolder;
    }
    public BSShopHolder(BSShop shop) {
        this.shop = shop;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }

    public BSBuy getShopItem(int i) {
        return items.get(i);
    }

    public int getSlot(BSBuy buy) {
        for (int slot : items.keySet()) {
            BSBuy value = items.get(slot);
            if (value == buy) {
                return slot;
            }
        }
        return -1;
    }


    public BSShop getShop() {
        return shop;
    }

    public BSShopHolder getPreviousShopHolder() {
        return previousShopHolder;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getHighestPage() {
        return highestPage;
    }

    public void setHighestPage(int page) {
        this.highestPage = page;
    }

    public int getDisplayPage() {
        return page + 1;
    }

    public int getDisplayHighestPage() {
        return highestPage + 1;
    }

    public void setItems(Map<Integer, BSBuy> items, int page, int highestPage) {
        this.items = items;
        this.page = page;
        this.highestPage = highestPage;
    }


}
