package edu.bupt.ticketextraction.extraction;

import edu.bupt.ticketextraction.tickets.CabTicket;
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
     * 调用ocr识别
     * TODO: 区分是图片还是视频
     *
     * @param sourceFile 资源文件，图片或视频
     * @param walletName 资源文件所在钱包名
     * @return 识别得到的发票信息
     */
    public @NotNull CabTicket callOcr(@NotNull File sourceFile, String walletName) {
        CabTicket.Builder builder = new CabTicket.Builder(walletName, sourceFile.getAbsolutePath());
        builder.setUnitPrice(3.0).
                setTotalPrice(11.0).
                setDate("2021.10.4").
                setDistance(3.0);
        return builder.create();
    }
}
