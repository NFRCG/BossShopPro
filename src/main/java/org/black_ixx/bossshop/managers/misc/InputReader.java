package org.black_ixx.bossshop.managers.misc;

import net.kyori.adventure.key.Key;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.misc.Enchant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InputReader {


    /**
     * Get a string from an object
     *
     * @param o         object to check
     * @param lowercase lowercase or not
     * @return string
     */
    public static String readString(Object o, boolean lowercase) {
        if (o == null) {
            return null;
        }
        String s = String.valueOf(o);
        if (s != null && lowercase) {
            s = s.toLowerCase();
        }
        return s;
    }

    /**
     * Get a string list from an object
     *
     * @param o object to check
     * @return string list
     */
    @SuppressWarnings("unchecked")
    public static List<String> readStringList(Object o) {
        if (o instanceof List<?>) {
            return (List<String>) o;
        }
        if (o instanceof String) {
            ArrayList<String> list = new ArrayList<>();
            list.add((String) o);
            return list;
        }
        return null;
    }

    /**
     * Get a list of string list from an object
     *
     * @param o object to check
     * @return list of string list
     */
    @SuppressWarnings("unchecked")
    public static List<List<String>> readStringListList(Object o) {
        if (!(o instanceof List<?>)) {
            return null;
        }
        List<?> list = (List<?>) o;
        if (list.isEmpty()) {
            return null;
        }
        if (list.get(0) instanceof List<?>) { //Everything perfect: Having a list inside a list
            return (List<List<String>>) o;
        } else { //Having one list only
            ArrayList<List<String>> main = new ArrayList<>();
            main.add((List<String>) o);
            return main;
        }
    }

    /**
     * Get list of itemstacks from object
     *
     * @param o            object to check
     * @return list of itemstacks
     */
    public static List<ItemStack> readItemList(Object o) {
        List<List<String>> list = readStringListList(o);
        if (list != null) {
            List<ItemStack> items = new ArrayList<>();
            for (List<String> s : list) {
                items.add(ClassManager.manager.getItemStackCreator().createItemStack(s));
            }
            return items;
        }
        return null;
    }

    /**
     * Get itemstack from object
     *
     * @param o            object to check
     * @return itemstack
     */
    public static ItemStack readItem(Object o) {
        List<ItemStack> list = readItemList(o);
        if (list != null & !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Get enchant from an object
     *
     * @param o object to check
     * @return enchant
     */
    public static Enchant readEnchant(Object o) {
        String s = readString(o, false);
        if (s != null) {
            String parts[] = s.split("#", 2);
            if (parts.length == 2) {
                String name = parts[0].trim();
                String level = parts[1].trim();
                int lvl;
                Enchantment e;

                try {
                    lvl = Integer.parseInt(level);
                } catch (NumberFormatException ex) {
                    ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + level + "' is not a valid enchantment level.");
                    return null;
                }
                e = Enchantment.getByKey(NamespacedKey.minecraft(name.toLowerCase()));

				/* Enchantment seems to somehow not be detected.
				if(e == null && Bukkit.getPluginManager().isPluginEnabled("TokenEnchant")){
					TokenEnchantAPI te = TokenEnchantAPI.getInstance();
					p_name = p_name.substring(0,1).toUpperCase()+p_name.substring(1).toLowerCase();
					System.out.println("Enchantment for " + p_name+": " + te.getEnchant(p_name));
					System.out.println("PE for " + p_name+": " + te.getPotion(p_name));
					e = te.getEnchant(p_name);
				}*/

                if (e == null) {
                    ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + name + "' is not a valid enchantment name/id.");
                    return null;
                }

                return new Enchant(e, lvl);

            }
        }
        return null;
    }

    /**
     * Get a double from an object
     *
     * @param o         objecct to get from
     * @param exception exception
     * @return double
     */
    public static double getDouble(Object o, double exception) {
        try {
            return Double.parseDouble(o.toString());
        } catch (NumberFormatException e) {
            return exception;
        }
    }

    /**
     * Get an int from an object
     *
     * @param o         object to get from
     * @param exception exception
     * @return int
     */
    public static int getInt(Object o, int exception) {
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return exception;
        }
    }
}
