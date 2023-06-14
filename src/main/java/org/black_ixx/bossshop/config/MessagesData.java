package org.black_ixx.bossshop.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
@SuppressWarnings({"unused", "FieldMayBeFinal", "FieldCanBeLocal"})
public class MessagesData implements ConfigData {
    private Messages messages = new Messages();
    private Economy economy = new Economy();
    private NotEnough notEnough = new NotEnough();
    private Enchantment enchantment = new Enchantment();
    private Display display = new Display();
    private Time time = new Time();

    public Messages messages() {
        return this.messages;
    }

    public Economy economy() {
        return this.economy;
    }

    public NotEnough notEnough() {
        return this.notEnough;
    }

    public Enchantment enchantment() {
        return this.enchantment;
    }

    public Display display() {
        return this.display;
    }

    public Time time() {
        return this.time;
    }

    @ConfigSerializable
    public static class Messages {
        private String noPermission = "&cYou are not allowed to do this!";
        private String alreadyBought = "&cYou already purchased that!";
        private String shopNotExisting = "&cThat shop is not existing...";
        private String shopItemNotExisting = "&cThat shop item is not existing...";
        private String openShop = "";
        private String openShopOtherPlayer = "&6Opening Shop &c%shop% &6for %player%&6.";
        private String closeShopOtherPlayer = "&6Closed inventory of %player%&6.";
        private String closeShop = "";
        private String inventoryFull = "&cYour inventory does not have enough free space left!";
        private String playerNotFound = "&cPlayer %name% not found!";
        private String clickSpamWarning = "&cPlease wait for an other %time_left% seconds until you can use this again.";
        private String offensiveClickSpamWarning = "&4Warning: Please do not click that fast.";
        private String offensiveClickSpamKick = "&4Kicked for clickspam.";
        private String needItemInHand = "&cYou need an item in your main hand in order to be able to execute this command.";
        private String printedItemInfo = "&6All item information has successfully been saved here: &7''/plugins/BossShopPro/ItemDataStorage.yml''&6.";
        private String listAndSeparator = " & ";
        private String listOrSeparator = " or ";

        public String noPermission() {
            return this.noPermission;
        }

        public String alreadyBought() {
            return this.alreadyBought;
        }

        public String shopNotExisting() {
            return this.shopNotExisting;
        }

        public String shopItemNotExisting() {
            return this.shopItemNotExisting;
        }

        public String openShop() {
            return this.openShop;
        }

        public String openShopOtherPlayer() {
            return this.openShopOtherPlayer;
        }

        public String closeShopOtherPlayer() {
            return this.closeShopOtherPlayer;
        }

        public String closeShop() {
            return this.closeShop;
        }

        public String inventoryFull() {
            return this.inventoryFull;
        }

        public String playerNotFound() {
            return this.playerNotFound;
        }

        public String clickSpamWarning() {
            return this.clickSpamWarning;
        }

        public String offensiveClickSpamWarning() {
            return this.offensiveClickSpamWarning;
        }

        public String offensiveClickSpamKick() {
            return this.offensiveClickSpamKick;
        }

        public String needItemInHand() {
            return this.needItemInHand;
        }

        public String printedItemInfo() {
            return this.printedItemInfo;
        }

        public String listAndSeparator() {
            return this.listAndSeparator;
        }

        public String listOrSeparator() {
            return this.listOrSeparator;
        }
    }

    @ConfigSerializable
    public static class Economy {
        private String noAccount = "&cYou don't have an economy account!";

        public String noAccount() {
            return this.noAccount;
        }
    }

    @ConfigSerializable
    public static class NotEnough {
        private String money = "&cYou don't have enough Money!";
        private String item = "&cYou don't have enough Items!";
        private String points = "&cYou don't have enough Points!";
        private String exp = "&cYou don't have enough EXP!";
        private String or = "&cYou don't have enough resources!";

        public String money() {
            return this.money;
        }

        public String item() {
            return this.item;
        }

        public String points() {
            return this.points;
        }

        public String exp() {
            return this.exp;
        }

        public String or() {
            return this.or;
        }
    }

    @ConfigSerializable
    public static class Enchantment {
        private String invalid = "&cYou can't enchant the item in your main hand with this enchantment!";
        private String emptyHand = "&cYou need to have an item in your hand!";

        public String invalid() {
            return this.invalid;
        }

        public String emptyHand() {
            return this.emptyHand;
        }
    }

    @ConfigSerializable
    public static class Display {
        private String exp = "%levels% Exp Levels";
        private String money = "%money% Money";
        private String points = "%points% Points";
        private String item = "%items%";
        private String itemAll = "all %item%";
        private String itemAllBuy = "a filled inventory of %item%";
        private String itemAllEach = "%value% each";
        private String nothing = "nothing";
        private String bungeeCordServer = "Server teleport to %server%";
        private String bungeeCordCommand = "BungeeCord commands: %commands%";
        private String close = "Close shop";
        private String command = "Console commands: %commands%";
        private String custom = "Unknown";
        private String enchantment = "Enchantment: %type% level %level%";
        private String permission = "Permissions: %permissions%";
        private String playerCommand = "Commands: %commands%";
        private String playerCommandOp = "Commands: %commands%";
        private String shop = "Open shop %shop%";
        private String page = "Open shop page %page%";

        public String exp() {
            return this.exp;
        }

        public String money() {
            return this.money;
        }

        public String points() {
            return this.points;
        }

        public String item() {
            return this.item;
        }

        public String itemAll() {
            return this.itemAll;
        }

        public String itemAllBuy() {
            return this.itemAllBuy;
        }

        public String itemAllEach() {
            return this.itemAllEach;
        }

        public String nothing() {
            return this.nothing;
        }

        public String bungeeCordServer() {
            return this.bungeeCordServer;
        }

        public String bungeeCordCommand() {
            return this.bungeeCordCommand;
        }

        public String close() {
            return this.close;
        }

        public String command() {
            return this.command;
        }

        public String custom() {
            return this.custom;
        }

        public String enchantment() {
            return this.enchantment;
        }

        public String permission() {
            return this.permission;
        }

        public String playerCommand() {
            return this.playerCommand;
        }

        public String playerCommandOp() {
            return this.playerCommandOp;
        }

        public String shop() {
            return this.shop;
        }

        public String page() {
            return this.page;
        }
    }

    @ConfigSerializable
    public static class Time {
        private String seconds = "%time% seconds";
        private String minutes = "%time% minutes";
        private String hours = "%time% hours";
        private String days = "%time% days";
        private String weeks = "%time% weeks";

        public String seconds() {
            return this.seconds;
        }

        public String minutes() {
            return this.minutes;
        }

        public String hours() {
            return this.hours;
        }

        public String days() {
            return this.days;
        }

        public String weeks() {
            return this.weeks;
        }
    }
}
