package edu.bupt.ticketextraction.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.setting.LoginActivity;
import edu.bupt.ticketextraction.utils.file.FileManager;
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
    private final MainActivity fatherActivity;

    public SettingFragment(MainActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 从设置跳转到用户管理
        Button settingJumpToLoginBtn = view.findViewById(R.id.setting_jump_to_login);
        settingJumpToLoginBtn.setOnClickListener(this::accountManageOnClickCallback);

        // 清除缓存
        Button clearCacheBtn = view.findViewById(R.id.clear_cache);
        clearCacheBtn.setOnClickListener(view1 -> FileManager.getInstance().clearCache());

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
