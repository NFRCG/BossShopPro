package org.black_ixx.bossshop.core;

import org.black_ixx.bossshop.core.prices.BSPriceType;
import org.black_ixx.bossshop.core.rewards.BSRewardType;

public class ActionSet {
    private final BSRewardType rewardT;
    private final BSPriceType priceT;
    private final Object reward;
    private final Object price;
    private final String msg;
    private final BSInputType inputType;
    private final String inputText;
    private final boolean isGroupPerm;
    private String permission;

    public ActionSet(BSRewardType rewardType, BSPriceType priceType, Object reward, Object price, String msg, String permission, BSInputType inputType, String inputText) {
        this.rewardT = rewardType;
        this.priceT = priceType;
        this.reward = reward;
        this.price = price;
        this.msg = msg;
        this.inputType = inputType;
        this.inputText = inputText;
        this.permission = permission;
        this.isGroupPerm = this.permission.startsWith("[") && this.permission.endsWith("]") && this.permission.length() > 2;
        if (this.isGroupPerm) {
            this.permission = this.permission.substring(1, this.permission.length() - 1);
        }
    }

    public BSRewardType getRewardType() {
        return this.rewardT;
    }

    public BSPriceType getPriceType() {
        return this.priceT;
    }

    public Object getReward() {
        return this.reward;
    }

    public Object getPrice() {
        return this.price;
    }

    public String getMessage() {
        return this.msg;
    }

    public String getExtraPermission() {
        return this.permission;
    }

    public boolean isExtraPermissionGroup() {
        return this.isGroupPerm;
    }

    public BSInputType getInputType() {
        return this.inputType;
    }

    public String getInputText() {
        return this.inputText;
    }

}
