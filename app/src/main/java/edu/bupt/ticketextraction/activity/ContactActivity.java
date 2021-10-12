package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/12
 *     desc   : 展示联系人信息的Activity
 *     version: 0.0.1
 * </pre>
 */
public final class ContactActivity extends AutoPushPopActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent intent = getIntent();
        // 获取传入的姓名和邮箱
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        // 绑定文本框
        TextView nameTextView = findViewById(R.id.contact_name);
        TextView emailTextView = findViewById(R.id.contact_email);
        // 设置文本内容
        nameTextView.setText(name);
        emailTextView.setText(email);
    }
}
