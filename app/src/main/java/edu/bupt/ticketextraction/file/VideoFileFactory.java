package edu.bupt.ticketextraction.file;

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
 *     desc   : 视频文件工厂，获取视频文件
 *     version: 0.0.1
 * </pre>
 */
public class VideoFileFactory extends FileFactory{
    // 视频目录
    private final String VIDEO_DIRECTORY;
    // 视频前缀
    private final String VIDEO_PREFIX;
    // 视频后缀
    private final String VIDEO_SUFFIX;

    public VideoFileFactory(String walletName) {
        super();
        // 视频文件应该在一个包内
        VIDEO_DIRECTORY = "/wallets" + walletName + "/video";
        VIDEO_PREFIX = "/VIDEO_";
        VIDEO_SUFFIX = ".mp4";
    }
    @Override
    public File createFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        try {
            return File.createTempFile(VIDEO_PREFIX + timeStamp, VIDEO_SUFFIX, new File(VIDEO_DIRECTORY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
