package edu.bupt.ticketextraction.file.filefactory;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 表格文件工厂类，获取表格文件
 *     version: 0.0.1
 * </pre>
 */
public class TableFileFactory extends FileFactory{
    // 表格目录
    private final String TABLE_DIRECTORY;
    // 表格前缀
    private final String TABLE_PREFIX;
    // 表格后缀
    private final String TABLE_SUFFIX;

    public TableFileFactory(String walletName) {
        super();
        // 一个包对应一个表格文件
        TABLE_DIRECTORY = EXTERNAL_FILE_DIR + "/wallets/" + walletName + "/table";
        TABLE_PREFIX = "_TABLE_";
        TABLE_SUFFIX = ".xlsx";
    }

    @Override
    public File createFile() {
        try {
            return File.createTempFile(TABLE_PREFIX, TABLE_SUFFIX, new File(TABLE_DIRECTORY));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
