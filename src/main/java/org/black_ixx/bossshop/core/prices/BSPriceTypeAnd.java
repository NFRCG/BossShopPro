package org.black_ixx.bossshop.core.prices;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.files.ErrorLog;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public class BSPriceTypeAnd extends BSPriceType {

    @Override
    public Object createObject(Object o, boolean forceState) {
        List<BSPricePart> parts = new ArrayList<>();

        ConfigurationSection prices = (ConfigurationSection) o;
        for (int i = 1; prices.contains("PriceType" + i); i++) {
            String priceType = prices.getString("PriceType" + i);
            Object priceObject = prices.get("Price" + i);

            BSPriceType priceT = BSPriceType.detectType(priceType);

            if (priceT == null) {
                ErrorLog.warn("Invalid PriceType '" + priceType + "' inside price list of shopitem with pricetype AND.");
                ErrorLog.warn("Valid PriceTypes:");
                for (BSPriceType type : BSPriceType.values()) {
                    ErrorLog.warn("-" + type.name());
                }
                continue;
            }
            priceT.enableType();

            Object priceO = priceT.createObject(priceObject, true);

            if (!priceT.validityCheck("?", priceO)) {
                ErrorLog.warn("Invalid Price '" + priceO + "' (PriceType= " + priceType + ") inside price list of shopitem with pricetype AND.");
                continue;
            }

            BSPricePart part = new BSPricePart(priceT, priceO);
            parts.add(part);

        }
        return parts;
    }

    @Override
    public boolean validityCheck(String item_name, Object o) {
        if (o != null) {
            return true;
        }
        ErrorLog.warn("Was not able to create ShopItem " + item_name + "! The price object needs to be a list of price-blocks. Every priceblock needs to contain price and pricetype.");
        return false;
    }

    @Override
    public void enableType() {
    }


    @Override
    public String[] createNames() {
        return new String[]{"and"};
    }

    @Override
    public boolean mightNeedShopUpdate() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, boolean messageOnFailure) {
        List<BSPricePart> priceparts = (List<BSPricePart>) price;
        for (BSPricePart part : priceparts) {
            if (!part.getPriceType().hasPrice(p, buy, part.getPrice(), clickType, messageOnFailure)) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String takePrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        String sep = ClassManager.manager.getMessageHandler().get("Main.ListAndSeparator");
        String s = "";
        List<BSPricePart> priceparts = (List<BSPricePart>) price;
        for (int i = 0; i < priceparts.size(); i++) {
            BSPricePart part = priceparts.get(i);
            s += part.getPriceType().takePrice(p, buy, part.getPrice(), clickType) + (i < priceparts.size() - 1 ? sep : "");
        }
        return s;
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType) {
        String sep = ClassManager.manager.getMessageHandler().get("Main.ListAndSeparator");
        String s = "";

        List<BSPricePart> priceparts = (List<BSPricePart>) price;
        for (int i = 0; i < priceparts.size(); i++) {
            BSPricePart part = priceparts.get(i);
            s += part.getPriceType().getDisplayPrice(p, buy, part.getPrice(), clickType) + (i < priceparts.size() - 1 ? sep : "");
        }
        return s;
    }

}
