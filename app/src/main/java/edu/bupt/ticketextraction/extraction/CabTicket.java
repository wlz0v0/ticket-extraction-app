package edu.bupt.ticketextraction.extraction;

import edu.bupt.ticketextraction.file.filefactory.WalletDataFileFactory;
import org.jetbrains.annotations.Contract;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/06
 *     desc   : 出租车车票类，包含各种信息
 *     version: 0.0.1
 * </pre>
 */
public class CabTicket {
    private double unitPrice;
    private double totalPrice;
    private double distance;
    private String date;
    private final String WALLET_NAME;
    private final String SOURCE_NAME;

    /**
     * @param walletName 钱包名
     * @param sourceName 对应的资源文件名
     */
    public CabTicket(String walletName, String sourceName) {
        this(walletName, sourceName, 0.0, 0.0, 0.0, "\0");
    }

    /**
     * @param walletName 钱包名
     * @param sourceName 对应的资源文件名
     * @param unitPrice  单价
     * @param totalPrice 总价
     * @param distance   行驶距离
     * @param date       日期
     */
    public CabTicket(String walletName,
                     String sourceName,
                     double unitPrice,
                     double totalPrice,
                     double distance,
                     String date) {
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.distance = distance;
        this.date = date;
        this.WALLET_NAME = walletName;
        this.SOURCE_NAME = sourceName;
    }

    @Contract(pure = true)
    public CabTicket(CabTicket rhs) {
        this.unitPrice = rhs.unitPrice;
        this.totalPrice = rhs.totalPrice;
        this.distance = rhs.distance;
        this.date = rhs.date;
        this.WALLET_NAME = rhs.WALLET_NAME;
        this.SOURCE_NAME = rhs.SOURCE_NAME;
    }

    public double getDistance() {
        return distance;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    /**
     * 将发票数据写入文件中
     */
    public void writeToData() {
        FileOutputStream outputStream = new WalletDataFileFactory(WALLET_NAME).createAppendFile();
        byte[] bytes = (unitPrice + " " + totalPrice + " " + distance + " " + date + "\n").
                getBytes(StandardCharsets.UTF_8);
        try {
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
