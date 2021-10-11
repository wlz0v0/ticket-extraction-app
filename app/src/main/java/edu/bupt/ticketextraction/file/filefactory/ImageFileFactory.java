package edu.bupt.ticketextraction.file.filefactory;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 图像文件工厂，获取图像文件
 *     version: 0.0.1
 * </pre>
 */
public final class ImageFileFactory extends FileFactory {
    // 图像目录
    private final String IMAGE_DIRECTORY;
    // 图像前缀
    private final String IMAGE_PREFIX;
    // 图像后缀
    private final String IMAGE_SUFFIX;

    /**
     * 根据钱包名称在该钱包下创建图片
     *
     * @param walletName 钱包名称
     */
    public ImageFileFactory(String walletName) {
        super();
        // 图像文件应该在一个包内
        IMAGE_DIRECTORY = EXTERNAL_FILE_DIR + "/wallets/" + walletName + "/image/";
        IMAGE_PREFIX = "IMAGE_";
        IMAGE_SUFFIX = ".jpg";
    }

    /**
     * 在某钱包下创建图片文件
     *
     * @return 生成的图片文件
     */
    public File createFile() {
        return getSourceFile(IMAGE_DIRECTORY, IMAGE_PREFIX, IMAGE_SUFFIX);
    }
}
