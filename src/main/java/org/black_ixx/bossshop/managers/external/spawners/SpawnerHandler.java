package org.black_ixx.bossshop.managers.external.spawners;

import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.Nullable;

//TODO: test
public class SpawnerHandler implements ISpawnerHandler, ISpawnEggHandler {

    public ItemStack transformSpawner(ItemStack i, String entityName) {
        BlockStateMeta meta = (BlockStateMeta) i.getItemMeta();
        CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
        spawner.setSpawnedType(EntityType.valueOf(entityName));
        meta.setBlockState(spawner);
        i.setItemMeta(meta);
        return i;
    }

    public ItemStack transformEgg(ItemStack i, String entityName) {
        i.setType(Material.valueOf(entityName + "_SPAWN_EGG"));
        return i;
    }

    public @Nullable String readSpawner(ItemStack i) {
        BlockStateMeta meta = (BlockStateMeta) i.getItemMeta();
        CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
        return spawner.getSpawnedType().name();
    }

    public String readEgg(ItemStack i) {
        return i.getType().name().replace("_SPAWN_EGG", "");
    }
}
