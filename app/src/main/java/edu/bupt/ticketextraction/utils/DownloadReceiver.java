package edu.bupt.ticketextraction.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import androidx.core.content.FileProvider;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;
import edu.bupt.ticketextraction.utils.file.filefactory.FileFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2022/01/03
 *     desc   : 下载监听器，下载后自动安装apk
 *     version: 0.0.1
 * </pre>
 */
public class DownloadReceiver extends BroadcastReceiver {
    /**
     * @param context 上下文
     * @param path    安装包路径
     */
    public static void install(Context context, String path) {
        Intent installIntent = new Intent(Intent.ACTION_VIEW);
        Uri fileUri = FileProvider.getUriForFile(context, "edu.bupt.ticketextraction.FileProvider", new File(path));
        Log.e("download", fileUri.getPath());
        installIntent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        // 设置允许其他应用读文件，不然会解析包失败
        installIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 启动安装
        context.startActivity(installIntent);
    }

    @Override
    public void onReceive(Context context, @NotNull Intent intent) {
        if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            // 动态获取权限
            AutoPushPopActivity activity = (AutoPushPopActivity) context;
            boolean hasInstallPermission = activity.getPackageManager().canRequestPackageInstalls();
            if (!hasInstallPermission) {
                Uri pkgUri = Uri.parse("package:" + activity.getPackageName());
                Intent permissionIntent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, pkgUri);
                //noinspection deprecation
                activity.startActivityForResult(permissionIntent, 1000);
                return;
            }
            installApk(context, id, FileFactory.EXTERNAL_FILE_DIR + FileFactory.APK_PATH);
        }
    }

    private void installApk(@NotNull Context context, long apkId, String path) {
        Log.e("download", "id: " + apkId);
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        // 这个uri是检查下载是否有问题
        Uri uri = dm.getUriForDownloadedFile(apkId);
        if (uri != null) {
            install(context, path);
        }
    }
}
