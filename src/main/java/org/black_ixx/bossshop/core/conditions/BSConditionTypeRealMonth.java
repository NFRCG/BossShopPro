package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class BSConditionTypeRealMonth extends BSConditionTypeNumber {
    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) {
        //TODO: ???
        int month = Calendar.getInstance().get(Calendar.MONTH);
        return switch (month) {
            case Calendar.JANUARY -> 1;
            case Calendar.FEBRUARY -> 2;
            case Calendar.MARCH -> 3;
            case Calendar.APRIL -> 4;
            case Calendar.MAY -> 5;
            case Calendar.JUNE -> 6;
            case Calendar.JULY -> 7;
            case Calendar.AUGUST -> 8;
            case Calendar.SEPTEMBER -> 9;
            case Calendar.OCTOBER -> 10;
            case Calendar.NOVEMBER -> 11;
            case Calendar.DECEMBER -> 12;
            default -> 0;
        };

    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"realmonth", "month"};
    }

    @Override
    public void enableType() {
    }
}
