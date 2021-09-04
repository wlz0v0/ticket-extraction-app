package edu.bupt.ticketextraction.file.filefactory;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    // 图像和视频文件
    /**
     * @param directory 目录
     * @param prefix 文件名前缀
     * @param suffix 文件名后缀
     **/
    public File getSourceFile(String directory, String prefix, String suffix) {
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

    // 数据文件流
    /**
     * @param directory 目录
     * @param prefix 文件名前缀
     * @param suffix 文件名后缀
     **/
    public FileOutputStream getDataStream(String directory, String prefix, String suffix) {
        try {
            return new FileOutputStream(new File(directory + prefix + suffix));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
