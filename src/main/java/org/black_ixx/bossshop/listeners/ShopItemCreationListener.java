package org.black_ixx.bossshop.listeners;

import org.black_ixx.bossshop.core.BSInputType;
import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;
import org.black_ixx.bossshop.events.BSCheckStringForFeaturesEvent;
import org.black_ixx.bossshop.events.BSCreateShopItemEvent;
import org.black_ixx.bossshop.core.ActionSet;
import org.black_ixx.bossshop.core.BSBuyAdvanced;
import org.black_ixx.bossshop.files.ErrorLog;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;


public class ShopItemCreationListener implements Listener {
    public ShopItemCreationListener() {
    }

    @EventHandler
    public void onCreate(final BSCreateShopItemEvent event) {
        ConfigurationSection c = event.getConfigurationSection();

        Map<ClickType, ActionSet> map = null;

        for (ClickType clicktype : ClickType.values()) {
            String s = clicktype.name().toLowerCase();
            if (c.contains("RewardType_" + s)) {

                if (map == null) {
                    map = new HashMap<>();
                }

                String priceType = c.getString("PriceType_" + s);
                String rewardType = c.getString("RewardType_" + s);
                String message = c.getString("Message_" + s);
                String permission = c.getString("ExtraPermission_" + s);
                if (permission == null || permission == "") {
                    permission = null;
                }

                BSRewardType rewardT = BSRewardType.detectType(rewardType);
                BSPriceType priceT = BSPriceType.detectType(priceType);

                if (rewardT == null) {
                    ErrorLog.warn("Was not able to create advanced BuyItem '" + event.getShopItemName() + "'! '" + rewardType + "' is not a valid RewardType! Switching back to simple kind of BuyItem.");
                    ErrorLog.warn("Valid RewardTypes:");
                    for (BSRewardType type : BSRewardType.values()) {
                        ErrorLog.warn("-" + type.name());
                    }
                    return;
                }

                if (priceT == null) {
                    ErrorLog.warn("Was not able to create advanced BuyItem '" + event.getShopItemName() + "!' '" + priceType + "' is not a valid PriceType! Switching back to simple kind of BuyItem.");
                    ErrorLog.warn("Valid PriceTypes:");
                    for (BSPriceType type : BSPriceType.values()) {
                        ErrorLog.warn("-" + type.name());
                    }
                    return;
                }

                rewardT.enableType();
                priceT.enableType();

                Object price = c.get("Price_" + s);
                Object reward = c.get("Reward_" + s);


                price = priceT.createObject(price, true);
                reward = rewardT.createObject(reward, true);

                if (!priceT.validityCheck(event.getShopItemName(), price)) {
                    return;
                }
                if (!rewardT.validityCheck(event.getShopItemName(), reward)) {
                    return;
                }


                String inputtypename = c.getString("ForceInput_" + s);
                String inputtext = c.getString("ForceInputMessage_" + s);
                BSInputType inputtype = null;
                if (inputtypename != null) {
                    for (BSInputType it : BSInputType.values()) {
                        if (it.name().equalsIgnoreCase(inputtypename)) {
                            inputtype = it;
                            break;
                        }
                    }
                    if (inputtype == null) {
                        ErrorLog.warn("Invalid advanced ForceInput type: '" + inputtypename + "' of shopitem '" + event.getShopItemName());
                    }
                }


                map.put(clicktype, new ActionSet(rewardT, priceT, reward, price, message, permission, inputtype, inputtext));

            }
        }


        BSBuyAdvanced buy = new BSBuyAdvanced(event.getRewardType(), event.getPriceType(), event.getReward(), event.getPrice(), event.getMessage(), event.getInventoryLocation(), event.getExtraPermission(), event.getShopItemName(), event.getCondition(), event.getInputType(), event.getInputText(), map);
        event.useCustomShopItem(buy);

    }


    @EventHandler
    public void onCheckStringForFeatures(BSCheckStringForFeaturesEvent event) {
        if (event.getShopItem() instanceof BSBuyAdvanced) {
            for (ClickType clicktype : ClickType.values()) {
                String s = clicktype.name().toLowerCase();
                if (event.getText().contains("%price_" + s + "%")) {
                    if (event.getShopItem().getPriceType(clicktype).isPlayerDependent(event.getShopItem(), clicktype)) {
                        event.approveFeature();
                        return;
                    }
                }
                if (event.getText().contains("%reward_" + s + "%")) {
                    if (event.getShopItem().getRewardType(clicktype).isPlayerDependent(event.getShopItem(), clicktype)) {
                        event.approveFeature();
                        return;
                    }
                }
            }
        }
    }

}
