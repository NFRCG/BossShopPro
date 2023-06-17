package org.black_ixx.bossshop.config.data;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@ConfigSerializable
public record Formats(List<FormatContext> formats, boolean enabled) {
    public static Formats defaults() {
        List<FormatContext> format = new ArrayList<>();
        format.add(new FormatContext("#,##0.00", "trillion", 1_000_000_000_000L));
        format.add(new FormatContext("#,##0.00", "billion", 1_000_000_000L));
        format.add(new FormatContext("#,##0.00", "million", 1_000_000L));
        format.add(new FormatContext("#,##0.00", "k", 1_000L));
        format.add(new FormatContext("#,##0.00", "", 0L));
        return new Formats(format, true);
    }

    public FormatContext checkThreshold(final long value) {
        return this.formats.stream()
                .filter(x -> x.threshold() <= value)
                .max(Comparator.comparingLong(FormatContext::threshold))
                .orElseThrow(() -> new IllegalArgumentException("No format found for value: " + value));
    }
}
