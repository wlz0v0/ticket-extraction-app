package edu.bupt.ticketextraction.utils;

import edu.bupt.ticketextraction.data.tickets.CabTicket;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/07
 *     desc   : Ocr工具类，调用ocr识别发票信息，不可实例化
 *     version: 0.0.1
 * </pre>
 */
public final class Ocr {
    /**
     * 调用ocr识别
     * TODO: 区分是图片还是视频，设置一个枚举类区分吧
     *
     * @param sourceFile 资源文件，图片或视频
     * @param walletName 资源文件所在钱包名
     * @return 识别得到的发票信息
     */
    public static @NotNull CabTicket extract(@NotNull File sourceFile, String walletName) {
        CabTicket.Builder builder = new CabTicket.Builder(walletName, sourceFile.getAbsolutePath());
        builder.setUnitPrice(3.0).
                setTotalPrice(11.0).
                setDate("2021.10.4").
                setDistance(3.0);
        return builder.create();
    }

    /**
     * Ocr工具类，请不要实例化此类！
     *
     * @throws InstantiationException 实例化异常，因为该类不可实例化
     */
    private Ocr() throws InstantiationException{
        throw new InstantiationException();
    }
}
