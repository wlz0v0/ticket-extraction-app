package edu.bupt.ticketextraction.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Button settingJumpToLoginBtn = view.findViewById(R.id.setting_jump_to_login);
        settingJumpToLoginBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToLogin());

        Button clearCacheBtn = view.findViewById(R.id.clear_cache);
        clearCacheBtn.setOnClickListener(view1 -> FileManager.getInstance().clearCache());

        Button aboutUsBtn = view.findViewById(R.id.about_us);
        aboutUsBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToAboutUs());
        return view;
    }
}
