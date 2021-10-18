package edu.bupt.ticketextraction.setting.contact;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;
import edu.bupt.ticketextraction.setting.PersonInfoActivity;

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
    // 俩文本框,展示Contact信息
    private TextView nameTextView;
    private TextView emailTextView;

    /**
     * 启动CreateContactActivity，并获取其返回的name和email<br>
     * 这个Launcher必须要定义在字段中
     */
    private final ActivityResultLauncher<Intent> changeContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                // 当用户不点击按钮而是直接返回的情况下，intent为null
                if (intent != null) {
                    // 获取返回字符串
                    String name = intent.getStringExtra(PersonInfoActivity.NEW_NAME);
                    String email = intent.getStringExtra(PersonInfoActivity.NEW_EMAIL);
                    // 设置对应文本内容
                    nameTextView.setText(name);
                    emailTextView.setText(email);
                }
            });

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Intent intent = getIntent();
        // 获取传入的联系人
        Contact contact = (Contact) intent.getSerializableExtra(PersonInfoActivity.CONTACT);
        // contact不应该为空
        assert contact != null;

        // 设置ActionBar
        super.setActionBar(this, contact.name);

        // 绑定控件
        nameTextView = findViewById(R.id.contact_name);
        emailTextView = findViewById(R.id.contact_email);

        // 设置文本内容
        nameTextView.setText(contact.name);
        emailTextView.setText(contact.email);

        // 绑定按钮
        Button changeInfoBtn = findViewById(R.id.change_contact_info_button);
        // 设置点击监听器
        changeInfoBtn.setOnClickListener(view -> {
            // 跳转前设置相应文本内容
            CreateContactActivity.title = "修改联系人信息";
            CreateContactActivity.buttonText = "修改";
            changeContactLauncher.launch(new Intent(this, CreateContactActivity.class));
        });
    }
}
