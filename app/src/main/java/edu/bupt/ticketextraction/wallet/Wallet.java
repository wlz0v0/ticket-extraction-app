package edu.bupt.ticketextraction.wallet;

import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.file.FileFactory;
import edu.bupt.ticketextraction.file.ImageFileFactory;
import edu.bupt.ticketextraction.file.VideoFileFactory;

import java.io.File;
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
    // 读取文件进入两个数组
    private ArrayList<File> files;

    public Wallet(String walletName) {
        this.walletName = walletName;
    }

    public File createImage() {
        File file = new ImageFileFactory(walletName).createFile();
        files.add(file);
        writeToData(file);
        return file;
    }

    public File createVideo() {
        File file = new VideoFileFactory(walletName).createFile();
        files.add(file);
        writeToData(file);
        return file;
    }

    // 从data文件中读取数据
    public void readData() {
        //TODO
    }

    // 将wallet展示在MainActivity中
    public void show() {
        //TODO
    }

    private void writeToData(File file) {
        String fileName = file.getName();
        //TODO
    }
}
