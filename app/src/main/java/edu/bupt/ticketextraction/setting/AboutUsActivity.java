package edu.bupt.ticketextraction.setting;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/27
 *     desc   : 关于activity，本app的基本信息
 *     version: 0.0.1
 * </pre>
 */
public final class AboutUsActivity extends AutoPushPopActivity {
    // 配套网站地址
    private final String address = "https://www.baidu.com";

    public void jumpFromAboutUsToTeam() {
        Intent intent = new Intent(AboutUsActivity.this, TeamActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        // 设置ActionBar
        super.setActionBar(this, "关于");

        TextView textView = findViewById(R.id.version_textview);
        // 当前版本
        final String cur_version = "0.0.0";
        textView.setText(cur_version);

        Button teamBtn = findViewById(R.id.team_btn);
        teamBtn.setOnClickListener(view -> jumpFromAboutUsToTeam());

        Button webBtn = findViewById(R.id.website_btn);
        webBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse(address);
            intent.setData(uri);
            startActivity(intent);
        });

    }
}
