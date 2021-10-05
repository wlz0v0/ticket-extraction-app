package edu.bupt.ticketextraction.wallet;

import edu.bupt.ticketextraction.extraction.CabTicket;
import edu.bupt.ticketextraction.file.filefactory.WalletDataFileFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
public class Wallet {
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
     * 从data文件中读取数据
     * TODO:数据存资源文件名
     */
    public void readData() {
        //TODO
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

        for (CabTicket ticket : tickets) {
            ticket.writeToData();
        }
    }
}
