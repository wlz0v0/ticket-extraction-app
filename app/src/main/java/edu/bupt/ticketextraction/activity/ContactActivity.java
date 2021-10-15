package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.data.Contact;

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
    public final static String CONTACT = "contact";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent intent = getIntent();
        // 获取传入的联系人
        Contact contact = (Contact) intent.getSerializableExtra(CONTACT);
        // contact不应该为空
        assert contact != null;

        // 设置ActionBar
        super.setActionBar(this, contact.name);

        // 绑定文本框
        TextView nameTextView = findViewById(R.id.contact_name);
        TextView emailTextView = findViewById(R.id.contact_email);
        // 设置文本内容
        nameTextView.setText(contact.name);
        emailTextView.setText(contact.email);

        // 绑定按钮
        Button changeInfoBtn = findViewById(R.id.change_contact_info_button);
        // 设置点击监听器
        changeInfoBtn.setOnClickListener(view -> {
            //TODO
        });
    }
}
