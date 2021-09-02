package edu.bupt.ticketextraction.file;

import edu.bupt.ticketextraction.wallet.Wallet;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 钱包数据工厂类，获取钱包数据
 *     version: 0.0.1
 * </pre>
 */
public class WalletDataFileFactory extends FileFactory{
    // 发票数据文件目录
    private final String DATA_DIRECTORY;
    // 发票数据文件前缀
    private final String WALLET_DATA_PREFIX;
    // 发票数据文件后缀
    private final String DATA_SUFFIX;

    public WalletDataFileFactory(String walletName) {
        super();
        // 一个包对应一个数据文件
        DATA_DIRECTORY = "/wallets" + walletName + "/data";
        WALLET_DATA_PREFIX = "_WALLET_DATA_";
        DATA_SUFFIX = ".dat";
    }

    @Override
    public File createFile() {
        try {
            return File.createTempFile(WALLET_DATA_PREFIX, DATA_SUFFIX, new File(DATA_DIRECTORY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
