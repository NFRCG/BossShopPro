package org.black_ixx.bossshop.managers.config;

import org.apache.commons.lang3.Validate;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.core.BSShops;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSLoadShopItemsEvent;
import org.black_ixx.bossshop.files.ErrorLog;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class BSConfigShop extends BSShop {

    private final String ymlName;
    private File f;
    private FileConfiguration config;
    private ConfigurationSection section;

    //////////////////////////////////

    public BSConfigShop(int shopId, String ymlName, BSShops shophandler) {
        this(shopId, new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/shops/" + ymlName), shophandler);
    }

    public BSConfigShop(int shopId, File f, BSShops shophandler) {
        this(shopId, f, shophandler, null);
    }

    public BSConfigShop(int shopId, File f, BSShops shophandler, ConfigurationSection sectionOptional) {
        super(shopId);

        this.f = f;
        this.ymlName = f.getName();
        try {
            this.config = this.loadConfiguration(f, true);
        } catch (InvalidConfigurationException e) {
            ErrorLog.warn("Invalid Configuration! File: /shops/" + f.getName() + " Cause: " + e.getMessage());
            String name = ymlName.replace(".yml", "");
            setSignText("[" + name + "]");
            setShopName(name);

            ItemStack i = new ItemStack(Material.WHITE_WOOL, 1);
            ItemMeta m = i.getItemMeta();
            m.setDisplayName(ChatColor.RED + "Your Config File contains mistakes! (" + ymlName + ")");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "For more information check /plugins/" + "BossShopPro" + "/BugFinder.yml out!");
            m.setLore(lore);
            i.setItemMeta(m);
            addShopItem(new BSBuy(BSRewardType.Command, BSPriceType.Nothing, "tell %player% the config file (" + ymlName + ") contains mistakes...", null, "", 0, "", name), i, ClassManager.manager);
            finishedAddingItems();
            return;
        }

        setup(shophandler, sectionOptional == null ? config : sectionOptional);

    }

    public void setup(BSShops shophandler, ConfigurationSection section) {
        this.section = section;

        //Add defaults if not existing already
        addDefaults();

        setShopName(section.getString("ShopName"));
        setDisplayName(section.getString("DisplayName"));
        setSignText(section.getString("signs.text"));
        setManualInventoryRows(section.getInt("InventoryRows", -1));

        String commands = section.getString("Command");
        if (commands != null) {
            setCommands(commands.split(":"));
        }

        //Load Items
        loadItems();
        BSLoadShopItemsEvent event = new BSLoadShopItemsEvent(shophandler, this);
        Bukkit.getPluginManager().callEvent(event);
        finishedAddingItems();
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            config.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void reloadConfig() {
        f = new File(ClassManager.manager.getPlugin().getDataFolder().getAbsolutePath() + "/shops/" + ymlName);
        config = YamlConfiguration.loadConfiguration(f);
        InputStream defConfigStream = ClassManager.manager.getPlugin().getResource(f.getName());
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            config.setDefaults(defConfig);
        }
    }

    public void addDefault(String name, String rewardType, String priceType, Object reward, Object price, List<String> menuitem, String message, int loc, String permission) {
        ConfigurationSection c = section.getConfigurationSection("shop").createSection(name);
        c.set("RewardType", rewardType);
        c.set("PriceType", priceType);
        c.set("Price", price);
        c.set("Reward", reward);
        c.set("MenuItem", menuitem);
        c.set("Message", message);
        c.set("InventoryLocation", loc);
        c.set("ExtraPermission", permission);
    }

    public void addDefaults() {
        section.addDefault("ShopName", "ExtraShop");
        section.addDefault("signs.text", "[ExtraShop]");
        section.addDefault("signs.NeedPermissionToCreateSign", false);

        if (section.getConfigurationSection("shop") == null && section.getConfigurationSection("itemshop") == null) {
            section.createSection("shop");

            List<String> menuItem = new ArrayList<>();
            menuItem.add("type:STONE");
            menuItem.add("amount:1");
            menuItem.add("name:&8Example");
            List<String> cmd = new ArrayList<>();
            cmd.add("tell %name% Example");
            addDefault("Example", "command", "money", cmd, 5000, menuItem, "", 1, "");
            config.options().copyDefaults(true);
            saveConfig();
        }
    }

    @Override
    public int getInventorySize() {
        if (section.getInt("InventorySize") != 0) {
            return section.getInt("InventorySize");
        }
        return super.getInventorySize();
    }

    public void loadItems() {
        ConfigurationSection c = section.getConfigurationSection("shop");
        if (c != null) {
            for (String key : c.getKeys(false)) {
                ClassManager.manager.getBuyItemHandler().loadItem(c, this, key);
            }
        }
    }

    @Override
    public void reloadShop() {
        reloadConfig();
    }

    private YamlConfiguration loadConfiguration(File file, boolean debug) throws InvalidConfigurationException {
        Validate.notNull(file, "File cannot be null");

        YamlConfiguration config = new YamlConfiguration();

        try {
            config.load(file);
        } catch (FileNotFoundException ex) {
            if (debug)
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        } catch (IOException ex) {
            if (debug)
                Bukkit.getLogger().log(Level.SEVERE, "Cannot load " + file, ex);
        }

        return config;
    }

}
