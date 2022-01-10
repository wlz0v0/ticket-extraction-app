package edu.bupt.ticketextraction.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.setting.LoginActivity;
import edu.bupt.ticketextraction.utils.HttpUtils;
import edu.bupt.ticketextraction.utils.file.FileManager;
import edu.bupt.ticketextraction.utils.file.filefactory.FileFactory;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/23
 *     desc   : 设置fragment
 *     version: 0.0.1
 * </pre>
 */
public final class SettingFragment extends Fragment {
    /**
     * 当前版本号
     */
    public static final String CUR_VERSION = "0_0_0";
    /**
     * 最新版本号
     */
    public static String LATEST_VERSION;
    private final MainActivity fatherActivity;

    public SettingFragment(MainActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 从设置跳转到用户管理
        Button settingJumpToLoginBtn = view.findViewById(R.id.setting_jump_to_login);
        settingJumpToLoginBtn.setOnClickListener(this::accountManageOnClickCallback);

        // 清除缓存
        Button clearCacheBtn = view.findViewById(R.id.clear_cache);
        clearCacheBtn.setOnClickListener(view1 -> FileManager.getInstance().clearCache());

        // 检查更新
        Button checkUpdateBtn = view.findViewById(R.id.check_update);
        checkUpdateBtn.setOnClickListener(view1 -> {
            LATEST_VERSION = HttpUtils.getLatestVersionNum(fatherActivity);
            String latest = LATEST_VERSION.replace('_', '.');
            Log.e("version: ", latest);
            // 最新则提示已为最新，否则弹出窗口提示更新
            if (LATEST_VERSION.equals(HttpUtils.PERMISSION_REFUSED)) {
                fatherActivity.showBottomToast(fatherActivity, "请求被拒绝！", 5);
            } else if (LATEST_VERSION.equals(CUR_VERSION)) {
                fatherActivity.showBottomToast(fatherActivity, "已是最新版本！", 5);
            } else {
                new QMUIDialog.MessageDialogBuilder(fatherActivity).
                        setTitle("更新").
                        setMessage("最新版本为" + latest).addAction("下次一定", (dialog, index) -> dialog.dismiss()).
                        addAction("现在更新", ((dialog, index) -> {
                            FileFactory.APK_PATH = "/apk/TicketExtraction" + LATEST_VERSION + "apk";
                            HttpUtils.downloadLatestApk(fatherActivity);
                        })).
                        show();
            }
        });

        // 从设置跳转到关于
        Button aboutUsBtn = view.findViewById(R.id.about_us);
        aboutUsBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToAboutUs());
        return view;
    }

    private void accountManageOnClickCallback(View view) {
        // 根据登录状态跳转到相应Activity
        if (LoginActivity.loginState) {
            fatherActivity.jumpFromMainToPersonInfo();
        } else {
            fatherActivity.jumpFromMainToLogin();
        }
    }
}
