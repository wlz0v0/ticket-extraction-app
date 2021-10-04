package edu.bupt.ticketextraction.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/27
 *     desc   : 关于activity，本app的基本信息
 *     version: 0.0.1
 * </pre>
 */
public class AboutUsActivity extends AppCompatActivity {
    // 配套网站地址
    private final String address = "https://www.baidu.com";

    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回main activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void jumpFromAboutUsToTeam() {
        Intent intent = new Intent(AboutUsActivity.this, TeamActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("关于");
        }

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
