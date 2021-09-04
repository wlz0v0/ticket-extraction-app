package edu.bupt.ticketextraction.wallet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/03
 *     desc   : 钱包管理类
 *              练习一下单例模式
 *     version: 0.0.1
 * </pre>
 */
public enum WalletManager {
    INSTANCE;

    /**
     *  key: wallet name string
     *  value: wallet
     **/
    private final HashMap<String, Wallet> wallets = new HashMap<>();

    public static WalletManager getInstance() {
        return INSTANCE;
    }

    public void writeToData() {

    }

    public void readFromData() {

    }

    public void addWallet(Wallet wallet) {
        wallets.put(wallet.getWalletName(), wallet);
    }

    public void deleteWallet(Wallet wallet) {
        wallets.remove(wallet.getWalletName());
    }

    public Wallet getWallet(String walletName) {
        return wallets.get(walletName);
    }
}
