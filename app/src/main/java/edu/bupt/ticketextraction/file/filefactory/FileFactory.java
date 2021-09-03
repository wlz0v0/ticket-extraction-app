package edu.bupt.ticketextraction.file.filefactory;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import androidx.core.content.ContextCompat;

import java.io.File;
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
 *     version: 0.0.1
 * </pre>
 */
public abstract class FileFactory {
    // TODO:获得外部存储缓存路径
    public abstract File createFile();
}
