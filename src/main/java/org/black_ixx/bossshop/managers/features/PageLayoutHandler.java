package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.config.DataFactory;
import org.black_ixx.bossshop.config.PageLayoutData;
import org.black_ixx.bossshop.core.BSBuy;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class PageLayoutHandler {
    private final List<BSBuy> items;
    private final int maxRows;
    private final int reservedSlotStart;
    private final boolean showIfMultiplePages;

    @Inject
    public PageLayoutHandler(final DataFactory factory) {
        PageLayoutData data = factory.pageLayouts();
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
