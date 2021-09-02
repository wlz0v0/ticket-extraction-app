package edu.bupt.ticketextraction.file;

import java.io.File;
import java.util.HashMap;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/31
 *     desc   : 文件管理类，可以直接从此类获得文件对象而无需访问具体文件类
 *              没有必要写成单例模式，只是开发者在练习
 *     version: 0.0.1
 * </pre>
 */
public enum FileManager {
    INSTANCE;

    public enum FileType {
        IMAGE,
        VIDEO,
        TABLE,
        DATA
    }

    /*public File createFile(FileType fileType, String walletName) {
        switch (fileType) {
            case IMAGE:
                return new FileFactory(walletName).createImageFile();
            case VIDEO:
                return new FileFactory(walletName).createVideoFile();
            case TABLE:
                return new FileFactory(walletName).createTableFile();
            case DATA:
                return new FileFactory(walletName).createDataFile();
            default:
                return null;
        }
    }*/
}

