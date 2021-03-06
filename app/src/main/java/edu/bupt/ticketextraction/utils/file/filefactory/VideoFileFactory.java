package edu.bupt.ticketextraction.utils.file.filefactory;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 视频文件工厂，获取视频文件
 *     version: 0.0.1
 * </pre>
 */
public final class VideoFileFactory extends FileFactory {
    // 视频目录
    private final String VIDEO_DIRECTORY;
    // 视频前缀
    private final String VIDEO_PREFIX;
    // 视频后缀
    private final String VIDEO_SUFFIX;

    /**
     * 根据钱包名称在该钱包下创建视频
     *
     * @param walletName 钱包名称
     */
    public VideoFileFactory(String walletName) {
        super();
        // 视频文件应该在一个包内
        VIDEO_DIRECTORY = EXTERNAL_FILE_DIR + "/wallets/" + walletName + "/video/";
        VIDEO_PREFIX = "VIDEO_";
        VIDEO_SUFFIX = ".mp4";
    }

    /**
     * 在某钱包下创建视频文件
     *
     * @return 生成的视频文件
     */
    public File createFile() {
        return getSourceFile(VIDEO_DIRECTORY, VIDEO_PREFIX, VIDEO_SUFFIX);
    }
}
