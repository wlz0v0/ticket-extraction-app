package edu.bupt.ticketextraction.utils.file;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/08
 *     desc   : 文件管理类，主要负责删除文件
 *     version: 0.0.1
 * </pre>
 */
public final class FileManager {
    /**
     * 单例模式，构造器私有
     */
    private FileManager() {
    }

    public static FileManager getInstance() {
        return InstanceHolder.INSTANCE;
    }

    /**
     * 递归删除文件夹下的所有文件和目录
     *
     * @param path 删除的目录或文件路径
     **/
    public void deleteAllFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] tempFiles = file.listFiles();
                // 断言tempFiles不为null
                assert tempFiles != null;
                for (File temp : tempFiles) {
                    deleteAllFiles(temp.getAbsolutePath());
                }
            } else {
                // 删除子文件
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
            // 删除文件夹
            //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    /**
     * 清空缓存
     */
    public void clearCache() {

    }

    /**
     * 私有静态内部类，负责实例化单例
     */
    private static class InstanceHolder {
        private final static FileManager INSTANCE = new FileManager();
    }
}
