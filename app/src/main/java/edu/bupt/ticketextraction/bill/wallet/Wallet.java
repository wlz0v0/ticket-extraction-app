package edu.bupt.ticketextraction.bill.wallet;

import edu.bupt.ticketextraction.bill.tickets.CabTicket;
import edu.bupt.ticketextraction.utils.file.Writable;
import edu.bupt.ticketextraction.utils.file.filefactory.WalletDataFileFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/01
 *     desc   : 钱包类，定义钱包属性
 *     version: 0.0.1
 * </pre>
 */
public final class Wallet implements Serializable, Writable {
    private final ArrayList<CabTicket> tickets;
    // 钱包名
    private String walletName;

    /**
     * 创建一个钱包
     *
     * @param walletName 钱包名称
     */
    public Wallet(String walletName) {
        this.walletName = walletName;
        tickets = new ArrayList<>();
    }

    /**
     * @return 钱包名
     */
    public String getWalletName() {
        return walletName;
    }

    /**
     * @param walletName 钱包名
     */
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    /**
     * @param ticket 添加的出租车发票信息
     */
    public void addTicket(CabTicket ticket) {
        tickets.add(ticket);
    }

    public void removeTicket(CabTicket ticket) {
        tickets.remove(ticket);
    }

    /**
     * @return 存储发票信息的数组
     */
    public ArrayList<CabTicket> getTickets() {
        return tickets;
    }

    /**
     * 从data文件中读取数据
     */
    public void readFromData() {
        // 构造BufferReader来读取一行
        // 一个发票为一行
        // 读的时候需要先清空tickets
        tickets.clear();
        try (FileInputStream inputStream = new WalletDataFileFactory(walletName).createInputStream();
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            // 局部变量line保存每行读取的信息
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 按照空格切割得到发票的各个信息
                String[] info = line.split(" ");
                // 构建发票
                CabTicket.Builder builder = new CabTicket.Builder(walletName, info[0], info[5], info[6]);
                builder.setUnitPrice(Double.parseDouble(info[1])).
                        setTotalPrice(Double.parseDouble(info[2])).
                        setDistance(Double.parseDouble(info[3])).
                        setDate(info[4]);
                // 添加到数组中
                tickets.add(builder.create());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将钱包中的所有发票信息写入数据文件
     */
    @Override
    public void writeToData() {
        // 先清空数据文件
        FileOutputStream outputStream = new WalletDataFileFactory(walletName).createOutputStream();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 再把数据写进去
        for (CabTicket ticket : tickets) {
            ticket.writeToData();
        }
    }
}
