package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class BSConditionTypeRealWeekDay extends BSConditionTypeNumber {
    @Override
    public double getNumber(BSBuy shopitem, BSShopHolder holder, Player p) {
        int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        //TODO: ???
        return switch (weekday) {
            case Calendar.MONDAY -> 1;
            case Calendar.TUESDAY -> 2;
            case Calendar.WEDNESDAY -> 3;
            case Calendar.THURSDAY -> 4;
            case Calendar.FRIDAY -> 5;
            case Calendar.SATURDAY -> 6;
            case Calendar.SUNDAY -> 7;
            default -> 0;
        };
    }

    @Override
    public boolean dependsOnPlayer() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"realweekday", "weekday"};
    }

    @Override
    public void enableType() {
    }
}
