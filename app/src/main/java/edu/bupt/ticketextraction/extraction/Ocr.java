package edu.bupt.ticketextraction.extraction;

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

    public CabTicket callOcr(@NotNull File sourceFile, String walletName) {
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
