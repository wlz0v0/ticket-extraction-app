package edu.bupt.ticketextraction.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.MainActivity;
import edu.bupt.ticketextraction.file.FileManager;
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
public class SettingFragment extends Fragment {
    private final MainActivity fatherActivity;

    public SettingFragment(MainActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        // 从设置跳转到登录
        Button settingJumpToLoginBtn = view.findViewById(R.id.setting_jump_to_login);
        settingJumpToLoginBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToLogin());

        // 清除缓存
        Button clearCacheBtn = view.findViewById(R.id.clear_cache);
        clearCacheBtn.setOnClickListener(view1 -> FileManager.getInstance().clearCache());

        // 从设置跳转到关于
        Button aboutUsBtn = view.findViewById(R.id.about_us);
        aboutUsBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToAboutUs());
        return view;
    }
}
