package edu.bupt.ticketextraction.file.filefactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/31
 *     desc   : 文件工厂，根据参数创建文件
 *              用工厂模式似乎不太合适
 *              只是最近看了工厂模式，于是就在app中写了一个文件工厂作为练习
 *              图像和视频文件是创建File然后托管给相机
 *              数据文件是使用流自己写入
 *              TODO:表格文件待定
 *     version: 0.0.1
 * </pre>
 */
public abstract class FileFactory {
    public static String EXTERNAL_FILE_DIR;
    public static String WALLETS_DIR;

    /**
     * 图像和视频文件
     *
     * @param directory 目录
     * @param prefix    文件名前缀
     * @param suffix    文件名后缀
     * @return File实例 图像或视频资源文件
     **/
    protected File getSourceFile(String directory, String prefix, String suffix) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        try {
            File dir = new File(directory);
            if (!dir.exists()) {
                // 忽略mkdirs()返回值
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            File file = new File(directory + prefix + timeStamp + suffix);
            //忽略返回值
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 数据文件流
     *
     * @param directory 目录
     * @param prefix    文件名前缀
     * @param isAppend  是否追加
     * @return 文件输出流，用于写入数据文件
     **/
    protected FileOutputStream getOutDataStream(String directory, String prefix, boolean isAppend) {
        File file = new File(directory + prefix + ".dat");
        if (!file.exists()) {
            try {
                //noinspection ResultOfMethodCallIgnored
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            return new FileOutputStream(directory + prefix + ".dat", isAppend);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected FileInputStream getInDataStream(String directory, String prefix) {
        try {
            return new FileInputStream(directory + prefix + ".dat");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
