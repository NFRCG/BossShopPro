package org.black_ixx.bossshop.misc;

import org.black_ixx.bossshop.config.data.Formats;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.managers.ClassManager;

import java.math.BigDecimal;

public final class MathTools {
    private MathTools() {
    }

    //TODO: test
    public static String displayNumber(double d, BSPriceType priceType) {
        Formats formats = ClassManager.manager.getFactory().settings().moneyFormat();
        if (priceType.equals(BSPriceType.Points)) {
            formats = ClassManager.manager.getFactory().settings().pointsFormat();
        }
        return formats.checkThreshold(Double.doubleToLongBits(d)).convert(BigDecimal.valueOf(d));
    }

    public static String displayNumber(double d) {
        return displayNumber(d, BSPriceType.Money);
    }
}
