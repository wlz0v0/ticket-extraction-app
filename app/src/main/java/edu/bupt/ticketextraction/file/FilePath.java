package edu.bupt.ticketextraction.file;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/31
 *     desc   : 定义存储路径常量
 *     version: 0.0.1
 * </pre>
 */
public class FilePath {
    // TODO: 确定存储路径
    // 图像存储路径
    private static final String IMAGE_PATH = "/image";
    // 视频存储路径
    private static final String VIDEO_PATH = "/video";
    // 表格存储路径
    private static final String TABLE_PATH = "/table";
    // 发票数据存储路径
    private static final String DATA_PATH = "/data";

    public static String getImagePath() {
        return IMAGE_PATH;
    }

    public static String getVideoPath() {
        return VIDEO_PATH;
    }

    public static String getTablePath() {
        return TABLE_PATH;
    }

    public static String getDataPath() {
        return DATA_PATH;
    }
}
