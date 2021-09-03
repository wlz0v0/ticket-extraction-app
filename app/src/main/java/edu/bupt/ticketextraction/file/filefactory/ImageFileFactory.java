package edu.bupt.ticketextraction.file.filefactory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 图像文件工厂，获取图像文件
 *     version: 0.0.1
 * </pre>
 */
public class ImageFileFactory extends FileFactory{
    // 图像目录
    private final String IMAGE_DIRECTORY;
    // 图像前缀
    private final String IMAGE_PREFIX;
    // 图像后缀
    private final String IMAGE_SUFFIX;

    public ImageFileFactory(String walletName) {
        super();
        // 图像文件应该在一个包内
        IMAGE_DIRECTORY = "/wallets/" + walletName + "/image";
        IMAGE_PREFIX = "/IMAGE_";
        IMAGE_SUFFIX = ".jpg";
    }

    @Override
    public File createFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        try {
            return File.createTempFile(IMAGE_PREFIX + timeStamp, IMAGE_SUFFIX, new File(IMAGE_DIRECTORY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
