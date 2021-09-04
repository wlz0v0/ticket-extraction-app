package edu.bupt.ticketextraction.file.filefactory;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/31
 *     desc   : 文件工厂，根据参数创建文件
 *              用工厂模式似乎不太合适
 *              只是最近看了工厂模式，于是就在app中写了一个文件工厂作为练习
 *     version: 0.0.1
 * </pre>
 */
public abstract class FileFactory {
    public static String EXTERNAL_FILE_DIR;
    public abstract File createFile();
}
