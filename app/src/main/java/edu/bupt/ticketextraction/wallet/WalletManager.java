package edu.bupt.ticketextraction.wallet;

import java.util.ArrayList;

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

    private final ArrayList<Wallet> wallets = new ArrayList<>();

    public static WalletManager getInstance() {
        return INSTANCE;
    }

    public void writeToData() {

    }

    public void readFromData() {

    }

    public void addWallet(Wallet wallet) {
        wallets.add(wallet);
    }

    public void deleteWallet(Wallet wallet) {
        wallets.remove(wallet);
    }
}
