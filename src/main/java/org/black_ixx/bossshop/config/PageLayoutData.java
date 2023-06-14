package org.black_ixx.bossshop.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents data from pagelayout.yml
 */
@ConfigSerializable
@SuppressWarnings({"unused", "FieldMayBeFinal", "FieldCanBeLocal"})
public class PageLayoutData implements ConfigData {
    private boolean showIfMultiplePagesOnly = true;
    private int maxRows = 6;
    private int reservedSlotsStart = 46;
    //TODO: replace with serializable shop item (obviously).
    private Items items = new Items();

    public boolean showIfMultiplePagesOnly() {
        return this.showIfMultiplePagesOnly;
    }

    public int maxRows() {
        return this.maxRows;
    }

    public int reservedSlotsStart() {
        return this.reservedSlotsStart;
    }

    public PreviousItem previous() {
        return this.items.previous();
    }

    public MenuItem menu() {
        return this.items.menu();
    }

    public NextItem next() {
        return this.items.next();
    }

    @ConfigSerializable
    private static class Items {
        private PreviousItem previous = new PreviousItem();
        private MenuItem menu = new MenuItem();
        private NextItem next = new NextItem();

        public PreviousItem previous() {
            return this.previous;
        }

        public MenuItem menu() {
            return this.menu;
        }

        public NextItem next() {
            return this.next;
        }
    }

    @ConfigSerializable
    private static class PreviousItem {
        private String rewardType = "page";
        private String reward = "previous";
        private String priceType = "nothing";
        private Map<String, String> item;
        private String message = "";
        private String inventoryLocation = "46";
        private String extraPermission = "";
        private Map<String, String> condition = new HashMap<>();

        PreviousItem() {
            this.item = new HashMap<>();
            this.item.put("type", "ARROW");
            this.item.put("amount", "1");
            this.item.put("name", "&f&lPrevious");
            this.item.put("lore", "&7Go back to the previous page.");
            this.condition.put("type", "page");
            this.condition.put("over", "1");
        }

        public String rewardType() {
            return this.rewardType;
        }

        public String reward() {
            return this.reward;
        }

        public String priceType() {
            return this.priceType;
        }

        public Map<String, String> item() {
            return this.item;
        }

        public String message() {
            return this.message;
        }

        public String inventoryLocation() {
            return this.inventoryLocation;
        }

        public String extraPermission() {
            return this.extraPermission;
        }

        public Map<String, String> condition() {
            return this.condition;
        }
    }

    @ConfigSerializable
    private static class MenuItem {
        private String rewardType = "shop";
        private String reward = "menu";
        private String priceType = "nothing";
        private Map<String, String> item;
        private String message = "";
        private String inventoryLocation = "50";
        private String extraPermission = "";

        MenuItem() {
            this.item = new HashMap<>();
            this.item.put("type", "CHEST");
            this.item.put("amount", "1");
            this.item.put("name", "&9&l&nMenu");
            this.item.put("lore", "&7Go back to the main Menu.");
        }

        public String rewardType() {
            return this.rewardType;
        }

        public String reward() {
            return this.reward;
        }

        public String priceType() {
            return this.priceType;
        }

        public Map<String, String> item() {
            return this.item;
        }

        public String message() {
            return this.message;
        }

        public String inventoryLocation() {
            return this.inventoryLocation;
        }

        public String extraPermission() {
            return this.extraPermission;
        }
    }

    @ConfigSerializable
    private static class NextItem {
        private String rewardType = "page";
        private String reward = "next";
        private String priceType = "nothing";
        private Map<String, String> item;
        private String message = "";
        private String inventoryLocation = "54";
        private String extraPermission = "";
        private Map<String, String> condition = new HashMap<>();

        NextItem() {
            this.item = new HashMap<>();
            this.item.put("type", "ARROW");
            this.item.put("amount", "1");
            this.item.put("name", "&f&lNext");
            this.item.put("lore", "&7Open the next page.");
            this.condition.put("type", "page");
            this.condition.put("under", "%maxpage%");
        }

        public String rewardType() {
            return this.rewardType;
        }

        public String reward() {
            return this.reward;
        }

        public String priceType() {
            return this.priceType;
        }

        public Map<String, String> item() {
            return this.item;
        }

        public String message() {
            return this.message;
        }

        public String inventoryLocation() {
            return this.inventoryLocation;
        }

        public String extraPermission() {
            return this.extraPermission;
        }

        public Map<String, String> condition() {
            return this.condition;
        }
    }
}
