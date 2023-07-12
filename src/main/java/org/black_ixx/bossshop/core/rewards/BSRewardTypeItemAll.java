package org.black_ixx.bossshop.core.rewards;


import org.black_ixx.bossshop.StringUtil;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.prices.BSPriceTypeNumber;
import org.black_ixx.bossshop.files.ErrorLog;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class BSRewardTypeItemAll extends BSRewardType {
    public Object createObject(Object o, boolean forceState) {
        if (forceState) {
            ItemStack i = StringUtil.readItem(o);
            i.setAmount(1);
            return i;
        } else {
            return StringUtil.readStringList(o);
        }
    }

    public boolean validityCheck(String itemName, Object o) {
        if (o != null) {
            return true;
        }
        ErrorLog.warn("Was not able to create ShopItem " + itemName + "! The reward object needs to be a valid list of ItemData (https://www.spigotmc.org/wiki/bossshoppro-rewardtypes/).");
        return false;
    }

    @Override
    public void enableType() {
    }

    @Override
    public boolean canBuy(Player p, BSBuy buy, boolean messageOnFailure, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;
        if (!ClassManager.manager.getItemStackChecker().hasFreeSpace(p, item)) {
            if (messageOnFailure) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.InventoryFull", p, null, p, buy.getShop(), null, buy);
            }
            return false;
        }
        BSPriceTypeNumber priceType = (BSPriceTypeNumber) buy.getPriceType(clickType);
        int items = this.calculateItems(buy, clickType, p, item);
        return priceType.hasPrice(p, buy, buy.getPrice(clickType), clickType, items, messageOnFailure);
    }

    @Override
    public void giveReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;
        BSPriceTypeNumber priceType = (BSPriceTypeNumber) buy.getPriceType(clickType);
        int items = this.calculateItems(buy, clickType, p, item);
        priceType.takePrice(p, buy, buy.getPrice(clickType), clickType, items);
        ClassManager.manager.getItemStackCreator().giveItem(p, buy, item, items, true);
    }

    @Override
    public String getDisplayReward(Player p, BSBuy buy, Object reward, ClickType clickType) {
        ItemStack item = (ItemStack) reward;
        String itemName = ClassManager.manager.getItemStackTranslator().readMaterial(item);
        return ClassManager.manager.getMessageHandler().get("Display.ItemAllBuy").replace("%item%", itemName);
    }

    @Override
    public String[] createNames() {
        return new String[]{"itemall", "buyall"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @Override
    public boolean overridesPrice() {
        return true;
    }

    @Override
    public String getPriceReturnMessage(Player p, BSBuy buy, Object price, ClickType clickType) {
        BSPriceTypeNumber priceType = (BSPriceTypeNumber) buy.getPriceType(clickType);
        return priceType.getDisplayBalance(p, buy, price, clickType);
    }

    private double getBalance(final Player p, final BSPriceType type) {
        return switch (type.name()) {
            case "MONEY" -> ClassManager.manager.getVaultHandler().getEconomy().getBalance(p);
            case "POINTS" -> ClassManager.manager.getPointsManager().getPoints(p);
            case "EXP" -> p.getExpToLevel();
            default -> 0;
        };
    }

    private int calculateItems(final BSBuy buy, final ClickType clickType, final Player player, final ItemStack item) {
        int possibleSpace = ClassManager.manager.getItemStackChecker().getAmountOfFreeSpace(player, item);
        Object price = buy.getPrice(clickType);
        BSPriceTypeNumber priceType = (BSPriceTypeNumber) buy.getPriceType(clickType);
        int itemRatio = (int) (this.getBalance(player, priceType) / (double) price);
        return Math.max(1, Math.min(possibleSpace, itemRatio));
    }
}
