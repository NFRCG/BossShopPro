package org.black_ixx.bossshop.core.conditions;

import org.black_ixx.bossshop.core.conditions.operator.Operator;
import org.black_ixx.bossshop.core.conditions.types.ExpCondition;
import org.black_ixx.bossshop.core.conditions.types.GroupCondition;
import org.black_ixx.bossshop.core.conditions.types.HandItemCondition;
import org.black_ixx.bossshop.core.conditions.types.HealthCondition;
import org.black_ixx.bossshop.core.conditions.types.HungerCondition;
import org.black_ixx.bossshop.core.conditions.types.ItemCondition;
import org.black_ixx.bossshop.core.conditions.types.LightLevelCondition;
import org.black_ixx.bossshop.core.conditions.types.LocationCondition;
import org.black_ixx.bossshop.core.conditions.types.MoneyCondition;
import org.black_ixx.bossshop.core.conditions.types.PermissionCondition;
import org.black_ixx.bossshop.core.conditions.types.PlaceholderCondition;
import org.black_ixx.bossshop.core.conditions.types.TimeCondition;
import org.black_ixx.bossshop.core.conditions.types.WorldNameCondition;
import org.black_ixx.bossshop.core.conditions.types.WorldTimeCondition;
import org.black_ixx.bossshop.core.conditions.types.WorldWeatherCondition;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;

import java.lang.reflect.Type;

public enum ConditionTypes implements TypeSerializer<Condition<Player>> {
    EXP(ExpCondition.class),
    GROUP(GroupCondition.class),
    HAND_ITEM(HandItemCondition.class),
    HEALTH(HealthCondition.class),
    HUNGER(HungerCondition.class),
    ITEM(ItemCondition.class),
    LIGHT_LEVEL(LightLevelCondition.class),
    LOCATION(LocationCondition.class) {
        @Override
        public Condition<Player> deserialize(Type type, ConfigurationNode node) throws SerializationException {
            char pos = node.key().toString().charAt(node.key().toString().length() - 1);
            Operator operator = node.node("operator").get(Operator.class, Operator.EQUALS);
            double value = node.node("value").getDouble();
            if (operator == Operator.BETWEEN) {
                double upperValue = node.node("upperValue").getDouble();
                return new LocationCondition(operator, pos, value, upperValue);
            }
            return new LocationCondition(operator, pos, value);
        }
    },
    MONEY(MoneyCondition.class),
    PERMISSION(PermissionCondition.class),
    PLACEHOLDER(PlaceholderCondition.class),
    //POINTS(PointsSpec.class),
    //SHOP_PAGE(ShopPageSpec.class),
    TIME(TimeCondition.class),
    WORLD_NAME(WorldNameCondition.class),
    WORLD_TIME(WorldTimeCondition.class),
    WORLD_WEATHER(WorldWeatherCondition.class);

    private final Class<? extends Condition<Player>> clazz;

    ConditionTypes(final Class<? extends Condition<Player>> clazz) {
        this.clazz = clazz;
    }

    //TODO: register to config.
    public static TypeSerializerCollection serializers() {
        TypeSerializerCollection.Builder builder = TypeSerializerCollection.builder();
        for (ConditionTypes type : ConditionTypes.values()) {
            builder = builder.register(type.clazz(), type);
        }
        return builder.build();
    }

    public Class<? extends Condition<Player>> clazz() {
        return this.clazz;
    }

    @Override
    public Condition<Player> deserialize(Type type, ConfigurationNode node) throws SerializationException {
        return node.get(this.clazz());
    }

    @Override
    public void serialize(Type type, @Nullable Condition<Player> obj, ConfigurationNode node) {
        throw new UnsupportedOperationException("Cannot write conditions to file!");
    }
}
