package org.black_ixx.bossshop.core.prices;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;

public abstract class BSPriceType {
    public static BSPriceType Item;
    public static BSPriceType ItemAll;
    public static BSPriceType Money;
    public static BSPriceType Nothing;
    public static BSPriceType Points;
    public static BSPriceType Exp;

    private static List<BSPriceType> types;
    private String[] names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();
        Item = registerType(new BSPriceTypeItem());
        ItemAll = registerType(new BSPriceTypeItemAll());
        Money = registerType(new BSPriceTypeMoney());
        Nothing = registerType(new BSPriceTypeNothing());
        Points = registerType(new BSPriceTypePoints());
        Exp = registerType(new BSPriceTypeExp());
    }

    public static BSPriceType registerType(BSPriceType type) {
        types.add(type);
        return type;
    }

    public static BSPriceType detectType(String s) {
        for (BSPriceType type : types) {
            if (type.isType(s)) {
                return type;
            }
        }
        return BSPriceType.Nothing;
    }

    public static List<BSPriceType> values() {
        return types;
    }

    public boolean isType(String s) {
        if (this.names != null) {
            for (String name : this.names) {
                if (name.equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void register() {
        BSPriceType.registerType(this);
    }

    public String name() {
        return this.names[0].toUpperCase();
    }

    public void updateNames() {
        this.names = createNames();
    }


    public abstract Object createObject(Object o, boolean forceState); //Used to transform the config input into a functional object

    public abstract boolean validityCheck(String itemName, Object o); //Used to check if the object is valid

    public abstract void enableType(); //Here you can register classes that the type depends on

    public abstract boolean hasPrice(Player p, BSBuy buy, Object price, ClickType clickType, boolean messageOnFailure);

    public abstract String takePrice(Player p, BSBuy buy, Object price, ClickType clickType);

    public abstract String getDisplayPrice(Player p, BSBuy buy, Object price, ClickType clickType);

    public abstract String[] createNames();

    public abstract boolean mightNeedShopUpdate();


    public boolean isPlayerDependent(BSBuy buy, ClickType clicktype) {
        return supportsMultipliers() && ClassManager.manager.getMultiplierHandler().hasMultipliers();
    }

    public boolean overridesReward() {
        return false; //Can be overwritten
    }

    public boolean supportsMultipliers() {
        return false; //can be overwritten
    }


}
