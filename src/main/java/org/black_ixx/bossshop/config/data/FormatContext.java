package org.black_ixx.bossshop.config.data;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.math.BigDecimal;
import java.text.DecimalFormat;

@ConfigSerializable
public record FormatContext(String pattern, String label, long threshold) {
    public DecimalFormat toDecimalFormat() {
        return new DecimalFormat(this.pattern);
    }

    public String convert(final BigDecimal number) {
        return this.toDecimalFormat().format(number) + this.label;
    }
}
