package org.black_ixx.bossshop.managers.features;

public enum PointsPlugin {
    NONE(new String[]{"none", "nothing"}),
    PLAYERPOINTS(new String[]{"PlayerPoints", "PlayerPoint", "PP"}),
    TOKENENCHANT(new String[]{"TokenEnchant", "TE", "TokenEnchants"}),
    TOKENMANAGER(new String[]{"TokenManager", "TM"}),
    JOBS(new String[]{"Jobs", "JobsReborn"}),
    VOTINGPLUGIN(new String[]{"VotingPlugin", "VP"}),
    GADGETS_MENU(new String[]{"GadgetsMenu"}),
    COINS(new String[]{"Coins"}),
    CUSTOM(new String[0]);

    private String[] nicknames;
    private String custom_name;

    PointsPlugin(String[] nicknames) {
        this.nicknames = nicknames;
    }

    public String getCustom() {
        return this.custom_name;
    }

    public void setCustom(String custom_name) {
        this.custom_name = custom_name;
    }

    public String[] getNicknames() {
        return this.nicknames;
    }

    public String getPluginName() {
        if (getNicknames() == null) {
            return custom_name;
        }
        if (getNicknames().length == 0) {
            return custom_name;
        }
        return getNicknames()[0];
    }
}