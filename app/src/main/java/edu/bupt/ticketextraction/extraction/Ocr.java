package edu.bupt.ticketextraction.extraction;

import edu.bupt.ticketextraction.wallet.Wallet;
import edu.bupt.ticketextraction.wallet.WalletManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/07
 *     desc   : 调用ocr识别发票信息
 *     version: 0.0.1
 * </pre>
 */
public enum Ocr {
    INSTANCE;

    public static Ocr getInstance() {
        return INSTANCE;
    }

    /**
     * 识别相应资源文件
     */
    public void extract(File sourceFile) {
        CabTicket cabTicket = callOcr(sourceFile);
        Wallet wallet = WalletManager.getInstance().getWallet(cabTicket.getWALLET_NAME());
        wallet.addTicket(cabTicket);
    }

    private CabTicket callOcr(@NotNull File sourceFile) {
        String walletName = WalletManager.getInstance().getWalletNameFromFile(sourceFile);
        CabTicket.Builder builder = new CabTicket.Builder();
        builder.setSourceName(sourceFile.getAbsolutePath()).
                setWalletName(walletName).
                setUnitPrice(3.0).
                setTotalPrice(11.0).
                setDate("2021.10.4").
                setDistance(3.0);
        return builder.create();
    }
}
