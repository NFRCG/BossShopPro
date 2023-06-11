package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class BSConditionType {
    public static BSConditionType SERVERPINGING;
    public static BSConditionType MONEY;
    public static BSConditionType POINTS;
    public static BSConditionType GROUP;
    public static BSConditionType HEALTH;
    public static BSConditionType HUNGER;
    public static BSConditionType PERMISSION;
    public static BSConditionType TIME;
    public static BSConditionType ITEM;
    public static BSConditionType HANDITEM;
    public static BSConditionType EXP;
    public static BSConditionType SHOPPAGE;
    public static BSConditionType REALYEAR;
    public static BSConditionType REALMONTH;
    public static BSConditionType REALWEEK;
    public static BSConditionType REALMONTHDAY;
    public static BSConditionType REALWEEKDAY;
    public static BSConditionType REALHOUR;
    public static BSConditionType REALMINUTE;
    public static BSConditionType REALSECOND;
    public static BSConditionType REALMILLISECOND;
    public static BSConditionType LIGHTLEVEL;
    public static BSConditionType LOCATIONX;
    public static BSConditionType LOCATIONY;
    public static BSConditionType LOCATIONZ;
    public static BSConditionType WORLD;
    public static BSConditionType WEATHER;
    public static BSConditionType PLACEHOLDERNUMBER;
    public static BSConditionType PLACEHOLDERMATCH;
    private static List<BSConditionType> types;
    private final String[] names = createNames();

    public static void loadTypes() {
        types = new ArrayList<>();
        SERVERPINGING = registerType(new BSConditionTypeServerpinging());
        MONEY = registerType(new BSConditionTypeMoney());
        POINTS = registerType(new BSConditionTypePoints());
        GROUP = registerType(new BSConditionTypeGroup());
        HEALTH = registerType(new BSConditionTypeHealth());
        HUNGER = registerType(new BSConditionTypeHunger());
        PERMISSION = registerType(new BSConditionTypePermission());
        TIME = registerType(new BSConditionTypeTime());
        ITEM = registerType(new BSConditionTypeItem());
        HANDITEM = registerType(new BSConditionTypeHandItem());
        EXP = registerType(new BSConditionTypeExp());
        SHOPPAGE = registerType(new BSConditionTypeShopPage());
        REALYEAR = registerType(new BSConditionTypeRealYear());
        REALMONTH = registerType(new BSConditionTypeRealMonth());
        REALWEEK = registerType(new BSConditionTypeRealWeek());
        REALMONTHDAY = registerType(new BSConditionTypeRealMonthDay());
        REALWEEKDAY = registerType(new BSConditionTypeRealWeekDay());
        REALHOUR = registerType(new BSConditionTypeRealHour());
        REALMINUTE = registerType(new BSConditionTypeRealMinute());
        REALSECOND = registerType(new BSConditionTypeRealSecond());
        REALMILLISECOND = registerType(new BSConditionTypeRealMillisecond());
        LIGHTLEVEL = registerType(new BSConditionTypeLightlevel());
        LOCATIONX = registerType(new BSConditionTypeLocationX());
        LOCATIONY = registerType(new BSConditionTypeLocationY());
        LOCATIONZ = registerType(new BSConditionTypeLocationZ());
        WORLD = registerType(new BSConditionTypeWorld());
        WEATHER = registerType(new BSConditionTypeWeather());
        PLACEHOLDERNUMBER = registerType(new BSConditionTypePlaceholderNumber());
        PLACEHOLDERMATCH = registerType(new BSConditionTypePlaceholderMatch());
    }

    public static BSConditionType registerType(BSConditionType type) {
        types.add(type);
        return type;
    }

    public static BSConditionType detectType(String s) {
        for (BSConditionType type : types) {
            if (type.isType(s)) {
                return type;
            }
        }
        return null;
    }

    public static List<BSConditionType> values() {
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

    public String[] getNames() {
        return this.names;
    }

    public void register() {
        BSConditionType.registerType(this);
    }

    public String name() {
        return this.names[0].toUpperCase();
    }

    public abstract void enableType(); //Here you can register classes that the type depends on

    public abstract boolean meetsCondition(BSShopHolder holder, BSBuy shopitem, Player p, String conditiontype, String condition);

    @Deprecated
    public abstract String[] createNames();

    public abstract String[] showStructure();

    public abstract boolean dependsOnPlayer();


}
