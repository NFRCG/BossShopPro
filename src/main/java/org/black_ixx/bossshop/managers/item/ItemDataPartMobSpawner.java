package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ItemDataPartMobSpawner extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String used_name, String argument) {
        BlockStateMeta meta = (BlockStateMeta) item.getItemMeta();
        CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
        spawner.setSpawnedType(EntityType.valueOf(argument));
        meta.setBlockState(spawner);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_MOST_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"mobspawner", "monsterspawner", "spawner"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        if (i.getType() == Material.SPAWNER) {
            output.add("mobspawner:" + this.readSpawner(i));
        }
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shop_item, ItemStack player_item, BSBuy buy, Player p) {
        if (shop_item.getType() == Material.SPAWNER) {
            if (player_item.getType() != Material.SPAWNER) {
                return false;
            }
            String spawners = this.readSpawner(shop_item);
            String spawnerp = this.readSpawner(player_item);
            return spawners.equalsIgnoreCase(spawnerp);
        }
        return true;
    }

    private @Nullable String readSpawner(ItemStack i) {
        BlockStateMeta meta = (BlockStateMeta) i.getItemMeta();
        CreatureSpawner spawner = (CreatureSpawner) meta.getBlockState();
        return spawner.getSpawnedType().name();
    }
}
