package edu.bupt.ticketextraction.bill.tickets;

import edu.bupt.ticketextraction.utils.file.filefactory.WalletDataFileFactory;
import org.jetbrains.annotations.NotNull;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/06
 *     desc   : 出租车车票类，包含各种信息
 *              练习一下建造者模式
 *     version: 0.0.1
 * </pre>
 */
@SuppressWarnings({"Unus", "unused"})
public final class CabTicket extends AbstractTicket {
    private Double unitPrice;
    private Double totalPrice;
    private Double distance;
    private String date;

    /**
     * CabTicket的构造器
     */
    // Builder类未使用返回值不做警告，因为一定会有一个返回值用不到
    @SuppressWarnings("UnusedReturnValue")
    public static class Builder extends AbstractTicket.Builder<Builder>{
        private Double unitPrice;
        private Double totalPrice;
        private Double distance;
        private String date;
        private final String walletName;
        private final String sourceName;

        /**
         * @param walletName 钱包名
         * @param sourceName 资源文件名
         */
        public Builder(String walletName, String sourceName) {
            unitPrice = 0.0;
            totalPrice = 0.0;
            distance = 0.0;
            date = "\0";
            this.walletName = walletName;
            this.sourceName = sourceName;
        }

        /**
         * 设置出租车发票单价
         *
         * @param unitPrice 单价
         */
        public Builder setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        /**
         * 设置出租车发票总价
         *
         * @param totalPrice 总价
         */
        public Builder setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
            return this;
        }

        /**
         * 设置出租车发票日期
         *
         * @param date 日期
         */
        public Builder setDate(String date) {
            this.date = date;
            return this;
        }

        /**
         * 设置出租车发票里程
         *
         * @param distance 里程
         */
        public Builder setDistance(double distance) {
            this.distance = distance;
            return this;
        }

        /**
         * 通过定义的Builder创建一个CabTicket实例
         */
        @Override
        public @NotNull CabTicket create() {
            return new CabTicket(walletName, sourceName, unitPrice, totalPrice, distance, date);
        }
    }

    /**
     * @param walletName 钱包名
     * @param sourceName 对应的资源文件名
     * @param unitPrice  单价
     * @param totalPrice 总价
     * @param distance   行驶距离
     * @param date       日期
     */
    private CabTicket(String walletName,
                      String sourceName,
                      double unitPrice,
                      double totalPrice,
                      double distance,
                      String date) {
        super(walletName, sourceName, "出租车发票");
        this.unitPrice = unitPrice;
        this.totalPrice = totalPrice;
        this.distance = distance;
        this.date = date;
    }

    public Double getDistance() {
        return distance;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public Double getUnitPrice() {
        return unitPrice;
    }

    public String getDate() {
        return date;
    }

    public String getSOURCE_NAME() {
        return SOURCE_NAME;
    }

    public String getWALLET_NAME() {
        return WALLET_NAME;
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
     * 将发票数据以append的形式写入文件中
     */
    @Override
    public void writeToData() {
        FileOutputStream outputStream = new WalletDataFileFactory(WALLET_NAME).createAppendFile();
        byte[] bytes = (SOURCE_NAME + " " + unitPrice + " " + totalPrice + " " + distance + " " + date + "\n").
                getBytes(StandardCharsets.UTF_8);
        try {
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
