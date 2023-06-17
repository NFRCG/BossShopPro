package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.config.PageLayoutData;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;

import java.util.ArrayList;
import java.util.List;

public class PageLayoutHandler {
    private final List<BSBuy> items;
    private final int maxRows;
    private final int reservedSlotStart;
    private final boolean showIfMultiplePages;

    public PageLayoutHandler() {
        PageLayoutData data = ClassManager.manager.getFactory().pageLayouts();
        this.maxRows = data.maxRows();
        this.reservedSlotStart = data.reservedSlotsStart();
        this.showIfMultiplePages = data.showIfMultiplePagesOnly();
        this.items = new ArrayList<>();
        //TODO: load items from page layouts config to list.
    }

    public List<BSBuy> getItems() {
        return this.items;
    }

    public int getMaxRows() {
        return this.maxRows;
    }

    public boolean showIfMultiplePagesOnly() {
        return this.showIfMultiplePages;
    }

    /**
     * @return display slot start: Starts with slot 1.
     */
    public int getReservedSlotsStart() {
        return this.reservedSlotStart;
    }

}
