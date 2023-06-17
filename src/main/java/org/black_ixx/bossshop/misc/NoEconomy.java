package org.black_ixx.bossshop.misc;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class NoEconomy implements Economy {

    public NoEconomy() {
        Bukkit.getLogger().warning("No economy plugin was found!");
    }

    @Override
    public EconomyResponse bankBalance(String arg0) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public boolean createPlayerAccount(String arg0) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String arg0, String arg1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public String currencyNamePlural() {
        return null;
    }

    @Override
    public String currencyNameSingular() {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

    @Override
    public String format(double arg0) {
        return null;
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public double getBalance(String arg0) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return 0;
    }

    @Override
    public double getBalance(String arg0, String arg1) {
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return 0;
    }

    @Override
    public List<String> getBanks() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean has(String arg0, double arg1) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return false;
    }

    @Override
    public boolean has(String arg0, String arg1, double arg2) {
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return false;
    }

    @Override
    public boolean hasAccount(String arg0) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean hasAccount(String arg0, String arg1) {
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return null;
    }

}
