package edu.bupt.ticketextraction.wallet;

import edu.bupt.ticketextraction.extraction.CabTicket;
import edu.bupt.ticketextraction.file.filefactory.WalletDataFileFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/01
 *     desc   : 钱包类，定义钱包属性
 *              TODO 所有钱包共用一个activity，这个activity通过钱包的设置进行加载
 *     version: 0.0.1
 * </pre>
 */
public class Wallet implements Serializable {
    // 钱包名
    private final String walletName;

    private final ArrayList<CabTicket> tickets;

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
     * @param ticket 添加的出租车发票信息
     */
    public void addTicket(CabTicket ticket) {
        tickets.add(ticket);
    }

    /**
     * @return 存储发票信息的数组
     */
    public ArrayList<CabTicket> getTickets() {
        return tickets;
    }

    /**
     * 从data文件中读取数据
     * 包内访问权限
     * TODO:数据存资源文件名
     */
    protected void readFromData() {
        // 构造BufferReader来读取一行
        // 一个发票为一行
        // 读的时候需要先清空tickets
        tickets.clear();
        try {
            FileInputStream inputStream = new WalletDataFileFactory(walletName).getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(reader);
            // 局部变量line保存每行读取的信息
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 按照空格切割得到发票的各个信息
                String[] info = line.split(" ");
                // 构建发票
                CabTicket.Builder builder = new CabTicket.Builder();
                builder.setWalletName(walletName).
                        setSourceName(info[0]).
                        setUnitPrice(Double.parseDouble(info[1])).
                        setTotalPrice(Double.parseDouble(info[2])).
                        setDistance(Double.parseDouble(info[3])).
                        setDate(info[4]);
                // 添加到数组中
                tickets.add(builder.create());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 将钱包中的所有发票信息写入数据文件
     * 包内访问权限
     */
    protected void writeToData() {
        // 先清空数据文件
        FileOutputStream outputStream = new WalletDataFileFactory(walletName).createFile();
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
