package org.black_ixx.bossshop.managers.item;

import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShopHolder;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.StringManager;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.tags.CustomItemTagContainer;
import org.bukkit.inventory.meta.tags.ItemTagType;

import javax.inject.Inject;
import java.util.List;


public class ItemStackTranslator {
    private final StringManager stringManager;

    @Inject
    public ItemStackTranslator(StringManager stringManager) {
        this.stringManager = stringManager;
    }

    public ItemStack translateItemStack(BSBuy buy, BSShop shop, BSShopHolder holder, ItemStack item, Player target) {
        if (item != null) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                //Normal itemdata
                if (meta.hasDisplayName()) {
                    meta.setDisplayName(this.stringManager.transform(meta.getDisplayName(), buy, shop, holder, target));
                }

                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        lore.set(i, this.stringManager.transform(lore.get(i), buy, shop, holder, target));
                    }
                    meta.setLore(lore);
                }


                //Skull itemdata
                if (meta instanceof SkullMeta) {
                    SkullMeta skullmeta = (SkullMeta) meta;
                    NamespacedKey key = new NamespacedKey(ClassManager.manager.getPlugin(), "skullOwnerPlaceholder");
                    CustomItemTagContainer tagContainer = meta.getCustomTagContainer();
                    if (tagContainer.hasCustomTag(key, ItemTagType.STRING)) {
                        String placeholder = tagContainer.getCustomTag(key, ItemTagType.STRING);
                        if (placeholder != null) {
                            String playerName = this.stringManager.transform(placeholder, target);
                            OfflinePlayer transformedPlayer = Bukkit.getOfflinePlayer(playerName);
                            if (transformedPlayer != null) {
                                skullmeta.setOwningPlayer(transformedPlayer);
                            } else {
                                skullmeta.setOwner(playerName);
                            }
                        }
                    }
                }

                item.setItemMeta(meta);


                if (meta instanceof SkullMeta) {
                    transformCustomSkull(buy, shop, item, holder, target);
                }
            }


        }
        return item;
    }


    private void transformCustomSkull(BSBuy buy, BSShop shop, ItemStack item, BSShopHolder holder, Player target) {
        String skulltexture = ItemDataPartCustomSkull.readSkullTexture(item);
        if (skulltexture != null) {
            if (this.stringManager.checkStringForFeatures(shop, buy, skulltexture)) {
                item = ItemDataPartCustomSkull.transformSkull(item, this.stringManager.transform(skulltexture, buy, shop, holder, target));
            }
        }
    }

    public String getFriendlyText(List<ItemStack> items) {
        if (items != null) {
            String msg = "";
            int x = 0;
            for (ItemStack i : items) {
                x++;
                msg += readItemStack(i) + (x < items.size() ? ", " : "");
            }
            return msg;
        }
        return null;
    }

    public String readItemStack(ItemStack i) {
        String material = readMaterial(i);
        return i.getAmount() + " " + material;
    }

    public String readEnchantment(Enchantment e) {
        return e.getName().toLowerCase().replace("_", "");
    }


    public boolean checkItemStackForFeatures(BSShop shop, BSBuy buy, ItemStack item) { //Returns true if this would make a shop customizable
        boolean b = false;
        if (item != null) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();

                //Normal itemdata
                if (meta.hasDisplayName()) {
                    if (this.stringManager.checkStringForFeatures(shop, buy,  meta.getDisplayName())) {
                        b = true;
                    }
                }

                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();
                    for (int i = 0; i < lore.size(); i++) {
                        if (this.stringManager.checkStringForFeatures(shop, buy,  lore.get(i))) {
                            b = true;
                        }
                    }
                }

                //Skull itemdata
                if (meta instanceof SkullMeta) {
                    SkullMeta skullmeta = (SkullMeta) meta;
                    if (skullmeta.hasOwner()) {
                        if (this.stringManager.checkStringForFeatures(shop, buy,  skullmeta.getOwner())) {
                            b = true;
                        }
                    }
                }
            }
        }
        return b;
    }

    public String readItemName(ItemStack item) {
        if (item != null) {
            if (item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName()) {
                    return meta.getDisplayName();
                }
            }
            return readItemStack(item);
        }
        return null;
    }

    public String readMaterial(ItemStack item) {
        String material = item.getType().name().toLowerCase().replace("_", " ");
        material = material.replaceFirst(material.substring(0, 1), material.substring(0, 1).toUpperCase());
        return material;
    }
}
