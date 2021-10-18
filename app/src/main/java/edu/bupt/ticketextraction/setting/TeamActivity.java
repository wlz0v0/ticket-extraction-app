package edu.bupt.ticketextraction.setting;

import android.os.Bundle;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/29
 *     desc   : 团队介绍activity
 *     version: 0.0.1
 * </pre>
 */
public final class TeamActivity extends AutoPushPopActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // 设置ActionBar
        super.setActionBar(this, "团队介绍");
    }
}
