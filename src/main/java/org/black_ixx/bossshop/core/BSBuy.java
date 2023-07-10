package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.conditions.Condition;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSPlayerPurchaseEvent;
import org.black_ixx.bossshop.events.BSPlayerPurchasedEvent;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.config.BSConfigShop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;


public class BSBuy {
    private BSShop shop;
    private HashMap<Plugin, Object> specialInformation;
    private ItemStack item;
    private final String name;
    private BSInputType inputType; // null by default. A value if players need to enter input before they can purchase the item.
    private String inputText;
    private final BSRewardType rewardT;
    private final BSPriceType priceT;
    private final Object reward;
    private final Object price;
    private Condition<Player> condition;
    private final String msg;
    private String permission;
    private boolean isGroup = false;
    private boolean fixItem; // In order for an item to not be fix it must contain a player-dependent placeholder (detected by StringManager.checkStringForFeatures)
    private int location;

    public BSBuy(BSRewardType rewardT, BSPriceType priceT, Object reward, Object price, String msg, int location, String permission, String name, Condition<Player> condition, BSInputType inputType, String inputText) {
        this(rewardT, priceT, reward, price, msg, location, permission, name);
        this.condition = condition;
        this.inputType = inputType;
        this.inputText = ClassManager.manager.getStringManager().transform(inputText, this, null, null, null);
    }

    public BSBuy(BSRewardType rewardT, BSPriceType priceT, Object reward, Object price, String msg, int location, String permission, String name) {
        this.priceT = priceT;
        this.rewardT = rewardT;
        if (permission != null && permission != "") {
            this.permission = permission;
            if (permission.startsWith("[") && permission.endsWith("]")) {
                if (permission.length() > 2) {
                    String group = permission.substring(1, permission.length() - 1);
                    if (group != null) {
                        this.permission = group;
                        this.isGroup = true;
                    }
                }
            }
        }
        this.reward = reward;
        this.price = price;
        this.name = name;
        this.msg = ClassManager.manager.getStringManager().transform(msg, this, null, null, null);
        this.location = location;
    }


    public BSShop getShop() {
        return shop;
    }

    public void setShop(BSShop shop) {
        this.shop = shop;
    }

    public BSRewardType getRewardType(ClickType clicktype) {
        return rewardT;
    }

    public BSPriceType getPriceType(ClickType clicktype) {
        return priceT;
    }

    public Object getReward(ClickType clicktype) {
        return reward;
    }

    public Object getPrice(ClickType clicktype) {
        return price;
    }

    public String getMessage(ClickType clicktype) {
        return msg;
    }

    public BSInputType getInputType(ClickType clicktype) {
        return inputType;
    }

    public String getInputText(ClickType clicktype) {
        return inputText;
    }

    /**
     * @return real inventory location with 0 as first possible one and -1 as "next available slot".
     */
    public int getInventoryLocation() {
        return location;
    }

    @Deprecated
    public void setInventoryLocation(int i) {
        location = i;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public boolean isItemFix() {
        return fixItem;
    }

    public Condition<Player> getCondition() {
        return condition;
    }

    public boolean meetsCondition(Player p) {
        if (condition != null) {
            return condition.isSatisfiedBy(p);
        }
        return true;
    }

    public boolean containsConditions() {
        return condition != null;
    }

    public ConfigurationSection getConfigurationSection(BSConfigShop shop) {
        return shop.getConfig().getConfigurationSection("shop").getConfigurationSection(name);
    }

    public boolean hasPermission(Player p, boolean msg, ClickType clicktype) {
        if (!isExtraPermissionExisting(clicktype)) {
            return true;
        }
        String permission = getExtraPermission(clicktype);

        if (isExtraPermissionGroup(clicktype)) {
            boolean noGroup = true;
            for (String group : ClassManager.manager.getVaultHandler().getPermission().getPlayerGroups(p)) {
                noGroup = false;
                if (group.equalsIgnoreCase(permission)) {
                    return true;
                }
            }
            if (noGroup && permission.equalsIgnoreCase("default")) {
                return true;
            }
            if (msg) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
            }
            return false;
        }

        if (p.hasPermission(permission)) {
            return true;
        }
        if (msg) {
            ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", p);
        }
        return false;
    }

    public boolean isExtraPermissionExisting(ClickType clicktype) {
        String permission = getExtraPermission(clicktype);
        if (permission == null) {
            return false;
        }
        if (permission.equalsIgnoreCase("")) {
            return false;
        }
        return true;
    }

    public boolean isExtraPermissionGroup(ClickType clicktype) {
        return this.isGroup;
    }


    public String getExtraPermission(ClickType clicktype) {
        return permission;
    }

    public Object readSpecialInformation(Plugin plugin) {
        if (specialInformation != null) {
            return specialInformation.get(plugin);
        }
        return null;
    }


    /**
     * Transforms the selected message by replacing price and reward placeholders with descriptions of price or reward.
     * If player is null only player-independent placeholders are replaced.
     * Additionally item placeholders like amount, material and more are replaced.
     * <br>
     * The method needs to be executed once the item is created (player=null):
     * <br>- to replace all player-independent placeholders
     * <br>- to mark the related shop as customizable if it contains player-dependent placeholders
     * <br>- to detect the inputType of this shopitem. If it is not null (default is null) players need to enter input before purchases are made
     * <br>
     * <br>The method also needs to be executed when a certain player accesses the item (player!=null):
     * <br>- to replace all player-dependent placeholders
     *
     * @param msg  Message to transform.
     * @param shop Shop this shopitem belongs to.
     * @param p    Related player. Can be null.
     * @return transformed message.
     */
    public String transformMessage(String msg, BSShop shop, Player p) {
        if (msg == null) {
            return null;
        }
        if (msg.length() == 0) {
            return msg;
        }

        if (p == null) {
            //Player null -> first item creation
            if (msg.contains("%input_text%")) {
                inputType = BSInputType.TEXT;
            } else if (msg.contains("%input_player%")) {
                inputType = BSInputType.PLAYER;
            }
            if (inputType != null && shop != null) {
                shop.setCustomizable(true);
                shop.setDisplaying(true);
            }
        }

        //Handle reward and price variables
        if (msg.contains("%price%") || msg.contains("%reward%")) {
            String rewardMessage = rewardT.isPlayerDependent(this, null) ? null : rewardT.getDisplayReward(p, this, reward, null);
            String priceMessage = priceT.isPlayerDependent(this, null) ? null : priceT.getDisplayPrice(p, this, price, null);


            if (shop != null) { //Does shop need to be customizable and is not already?
                if (!shop.isCustomizable()) {
                    boolean hasPrice = (msg.contains("%price%") && (priceT.isPlayerDependent(this, null)));
                    boolean hasReward = (msg.contains("%reward%") && (rewardT.isPlayerDependent(this, null)));
                    if (hasPrice || hasReward) {
                        shop.setCustomizable(true);
                        shop.setDisplaying(true);
                    }
                }
            }

            boolean possiblyCustomizable = shop == null ? true : shop.isCustomizable();
            if (possiblyCustomizable) {
                if (p != null) { //When shop is customizable, the variables needs to be adapted to the player
                    rewardMessage = rewardT.getDisplayReward(p, this, reward, null);
                    priceMessage = priceT.getDisplayPrice(p, this, price, null);
                }
            }

            if (priceMessage != null && priceMessage != "" && priceMessage.length() > 0) {
                msg = msg.replace("%price%", priceMessage);
            }
            if (rewardMessage != null && rewardMessage != "" && rewardMessage.length() > 0) {
                msg = msg.replace("%reward%", rewardMessage);
            }
        }

        //Not working with these variables anymore. They are still included and set to "" in order to make previous shops still look good and stay compatible.
        if (priceT != null && priceT.name() != "" && priceT.name().length() > 0) {
            msg = msg.replace(" %pricetype%", "");
            msg = msg.replace("%pricetype%", "");
        }
        if (rewardT != null && rewardT.name() != "" && rewardT.name().length() > 0) {
            msg = msg.replace(" %rewardtype%", "");
            msg = msg.replace("%rewardtype%", "");
        }

        //Handle rest
        msg = msg.replace("%shopitemname%", this.name);

        String name;
        if (shop != null && item != null) {
            String title = ClassManager.manager.getItemStackTranslator().readItemName(item);
            if (title != null) {
                name = title;
                msg = msg.replace("%itemname%", name);
            }

            if (msg.contains("%amount%")) {
                msg = msg.replace("%amount%", String.valueOf(item.getAmount()));
            }
            if (msg.contains("%material%")) {
                msg = msg.replace("%material%", ClassManager.manager.getItemStackTranslator().readMaterial(item));
            }
            if (msg.contains("%rewardraw%")) {
                msg = msg.replace("%rewardraw%", String.valueOf(reward));
            }
            if (msg.contains("%priceraw%")) {
                msg = msg.replace("%priceraw%", String.valueOf(price));
            }
        }
        return msg;
    }

    public void updateShop(BSShop shop, ItemStack menuitem, ClassManager manager, boolean addItem) {
        if (manager.getFactory().settings().hideItemsWhenNoPermission()) {
            if (!shop.isCustomizable()) {
                if (isExtraPermissionExisting(null)) {
                    shop.setCustomizable(true);
                }
            }
        }

        if (!shop.isCustomizable()) {
            for (BSBuy b : shop.getItems()) {
                if (b != null) {
                    if (b.getInventoryLocation() == getInventoryLocation() || b.containsConditions()) {
                        shop.setCustomizable(true);
                        break;
                    }
                }
            }
        }

        if (menuitem.hasItemMeta()) {
            if (ClassManager.manager.getItemStackTranslator().checkItemStackForFeatures(shop, this, menuitem)) {
                shop.setCustomizable(true);
                shop.setDisplaying(true);
            }

            if (!isItemFix()) { //Addons can make items fix instantly. In normal cases this check is not needed.
                ClassManager.manager.getItemStackTranslator().translateItemStack(this, shop, null, menuitem, null);
            }
        }
        setItem(menuitem, !ClassManager.manager.getItemStackTranslator().checkItemStackForFeatures(shop, this, menuitem));
        if (isItemFix()) { //When all placeholders are replaced the plugin can finally cut the lore and do final stuff
            ClassManager.manager.getItemStackTranslator().translateItemStack(null, null, null, getItem(), null);
        }
        if (addItem) {
            shop.getItems().add(this);
        }
    }

    public void putSpecialInformation(Plugin plugin, Object information) {
        if (plugin != null && information != null) {
            if (specialInformation == null) {
                specialInformation = new HashMap<>();
            }
            specialInformation.put(plugin, information);
        }
    }

    public void setItem(ItemStack item, boolean fixItem) {
        this.item = item;
        this.fixItem = fixItem;
    }

    public void click(Player p, BSShop shop, BSShopHolder holder, ClickType clicktype, InventoryClickEvent event, BossShop plugin) {
        if (!hasPermission(p, true, clicktype)) {
            p.playSound(ClassManager.manager.getFactory().settings().sounds().get("noPermission"));
            return;
        }
        if (!meetsCondition(p)) {
            return; //Can only happen when player click-spams item before it is refreshed
        }

        this.purchaseTry(p, shop, holder, clicktype, event, plugin);
    }

    /**
     * Tries executing a purchase action. Fails if the player is unable to buy the item or unable to pay the price.
     *
     * @param p         Player to click purchase.
     * @param shop      Shop this shopitem belongs to.
     * @param holder    Holder of the shop.
     * @param clicktype Clicktype.
     * @param event     Click event which caused purchase. Can be null (for example when click is simulated).
     * @param plugin    Bossshop plugin.
     */
    public void purchaseTry(Player p, BSShop shop, BSShopHolder holder, ClickType clicktype, InventoryClickEvent event, BossShop plugin) {
        BSPlayerPurchaseEvent e1 = new BSPlayerPurchaseEvent(p, shop, this, clicktype);//Custom Event
        Bukkit.getPluginManager().callEvent(e1);
        if (e1.isCancelled()) {
            return;
        }//Custom Event end

        BSRewardType rewardtype = getRewardType(clicktype);
        BSPriceType pricetype = getPriceType(clicktype);

        if (!rewardtype.canBuy(p, this, true, getReward(clicktype), clicktype)) {
            return;
        }
        if (!pricetype.hasPrice(p, this, getPrice(clicktype), clicktype, true)) {
            p.playSound(ClassManager.manager.getFactory().settings().sounds().get("notEnoughMoney"));
            return;
        }

        purchaseTask(p, shop, holder, clicktype, rewardtype, pricetype, event, plugin);
    }

    /**
     * Triggers {@link BSBuy#purchase(Player, BSShop, BSShopHolder, ClickType, BSRewardType, BSPriceType, InventoryClickEvent, BossShop, boolean)}.
     * This is done asynchronously if async-actions = true.
     *
     * @param p          Player to purchase the item.
     * @param shop       Shop this shopitem belongs to.
     * @param holder     Holder of the shop.
     * @param clicktype  Clicktype.
     * @param rewardtype Rewardtype.
     * @param pricetype  Pricetype.
     * @param event      Click event which caused purchase. Can be null (for example when click is simulated).
     * @param plugin     Bossshop plugin.
     */
    @Deprecated
    public void purchaseTask(final Player p, final BSShop shop, final BSShopHolder holder, final ClickType clicktype, final BSRewardType rewardtype, final BSPriceType pricetype, final InventoryClickEvent event, final BossShop plugin) {
        if (inputType != null) {
            inputType.forceInput(p, shop, this, holder, clicktype, rewardtype, pricetype, event, plugin);
            return;
        }
        if (ClassManager.manager.getFactory().settings().asyncActions()) {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> this.purchase(p, shop, holder, clicktype, rewardtype, pricetype, event, ClassManager.manager.getPlugin(), true));
        } else {
            purchase(p, shop, holder, clicktype, rewardtype, pricetype, event, plugin, false);
        }
    }


    /**
     * Actions:
     * <br>- take price from player (price might depend on rewardtype)
     * <br>- optionally close the inventory
     * <br>- give reward to player (reward might depend on pricetype; executed asynchronously if async=true and the rewardtype supports it)
     * <br>- optionally logs transaction in transactionslog
     * <br>- take price from player
     * <br>- sends purchase message to player
     * <br>- optionally plays purchase sound
     * <br>- updates shop if needed and inventory still open (done asynchronously if async=true)
     *
     * @param p          Player to purchase the item.
     * @param shop       Shop this shopitem belongs to.
     * @param holder     Holder of the shop.
     * @param clicktype  Clicktype.
     * @param rewardtype Rewardtype.
     * @param pricetype  Pricetype.
     * @param event      Click event which caused purchase. Can be null (for example when click is simulated).
     * @param plugin     Bossshop plugin.
     * @param async      Whether actions which can be executed asynchronously should be.
     */
    @Deprecated
    public void purchase(final Player p, final BSShop shop, final BSShopHolder holder, final ClickType clicktype, final BSRewardType rewardtype, BSPriceType pricetype, final InventoryClickEvent event, final BossShop plugin, boolean async) {
        //Generate message
        String message = getMessage(clicktype);
        if (message != null) {
            message = transformMessage(message, shop, p); //Transform message before taking price because then ItemAll works fine
        }

        String o = null;


        if (!rewardtype.overridesPrice()) {
            o = pricetype.takePrice(p, this, getPrice(clicktype), clicktype); //Take price
        }


        //Close shop if wanted
        //TODO: check shop item for this property: CloseShopAfterPurchase and then close inventory if true.
        if (!pricetype.overridesReward()) {
            //Give Reward
            //Some rewardtypes may not be async!
            if (async && rewardtype.allowAsync()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        rewardtype.giveReward(p, BSBuy.this, getReward(clicktype), clicktype);
                    }
                }.runTask(plugin);
            } else {
                rewardtype.giveReward(p, this, getReward(clicktype), clicktype);
            }

        }

        if (rewardtype.overridesPrice()) {
            o = rewardtype.getPriceReturnMessage(p, this, pricetype, clicktype);
        }


        //Update message
        if (message != null) {
            if (o != null && o != "" && message.contains("%left%")) {
                message = message.replace("%left%", o);
            }
            message = plugin.getClassManager().getStringManager().transform(message, this, shop, holder, p); //Transform message before taking price because then ItemAll works fine
        }

        boolean need_update = rewardtype.mightNeedShopUpdate() || pricetype.mightNeedShopUpdate();

        //Transactionslog
        if (plugin.getClassManager().getFactory().settings().enableTransactionLog()) {
            plugin.getClassManager().getTransactionLog().addTransaction(p, this, clicktype);
        }

        //Custom "BSPlayerPurchasedEvent" event
        BSPlayerPurchasedEvent e2 = new BSPlayerPurchasedEvent(p, shop, this, clicktype);//Custom Event
        Bukkit.getPluginManager().callEvent(e2);//Custom Event end

        //Send message and play sound
        ClassManager.manager.getMessageHandler().sendMessageDirect(message, p);
        if (pricetype != BSPriceType.Nothing) {
            p.playSound(ClassManager.manager.getFactory().settings().sounds().get("purchase"));
        } else {
            if (rewardtype.isActualReward()) {
                p.playSound(ClassManager.manager.getFactory().settings().sounds().get("click"));
            }
        }

        //Update shop if needed
        if (shop.isCustomizable() && need_update && event != null && (p.getOpenInventory() == event.getView())) { //only if inventory is still open
            if (async) {
                Bukkit.getScheduler().runTask(ClassManager.manager.getPlugin(), () -> shop.updateInventory(event.getInventory(), holder, p, plugin.getClassManager(), holder.getPage(), holder.getHighestPage(), false));
            } else {
                shop.updateInventory(event.getInventory(), holder, p, plugin.getClassManager(), holder.getPage(), holder.getHighestPage(), false);
            }
        }
    }
}
