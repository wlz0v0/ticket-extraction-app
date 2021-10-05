package edu.bupt.ticketextraction.file.filefactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 钱包数据工厂类，获取钱包数据
 *     version: 0.0.1
 * </pre>
 */
public class WalletDataFileFactory extends FileFactory {
    // 发票数据文件目录
    private final String DATA_DIRECTORY;
    // 发票数据文件前缀
    private final String WALLET_DATA_PREFIX;

    /**
     * 根据钱包名称在该钱包下创建钱包数据文件
     *
     * @param walletName 钱包名称
     */
    public WalletDataFileFactory(String walletName) {
        super();
        // 一个包对应一个数据文件
        DATA_DIRECTORY = EXTERNAL_FILE_DIR + "/wallets/" + walletName + "/";
        WALLET_DATA_PREFIX = "WalletData";
    }

    /**
     * 根据钱包名称创建输出流将数据写入文件，覆盖所有数据
     *
     * @return 钱包数据文件输出流
     */
    public FileOutputStream createFile() {
        return getOutDataStream(DATA_DIRECTORY, WALLET_DATA_PREFIX, false);
    }

    /**
     * 根据钱包名称创建输出流将数据写入文件，追加模式
     *
     * @return 钱包数据文件输出流
     */
    public FileOutputStream createAppendFile() {
        return getOutDataStream(DATA_DIRECTORY, WALLET_DATA_PREFIX, true);
    }

    public FileInputStream getInputStream() {
        return getInDataStream(DATA_DIRECTORY, WALLET_DATA_PREFIX);
    }
}
