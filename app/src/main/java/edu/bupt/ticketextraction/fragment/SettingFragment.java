package edu.bupt.ticketextraction.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.MainActivity;

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
    private final MainActivity mainActivity;

    public SettingFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        Button setting_jump_to_login_btn = view.findViewById(R.id.setting_jump_to_login);
        setting_jump_to_login_btn.setOnClickListener(view1 -> mainActivity.jumpFromMainToLogin());

        Button clear_cache_btn = view.findViewById(R.id.clear_cache);
        clear_cache_btn.setOnClickListener(view1 -> {
            //TODO:清空缓存
        });

        Button about_us_btn = view.findViewById(R.id.about_us);
        about_us_btn.setOnClickListener(view1 -> mainActivity.jumpFromMainToAboutUs());
        return view;
    }
}
