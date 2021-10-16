package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/15
 *     desc   : 修改联系人信息Activity，与添加联系人Activity共用一个layout
 *     version: 0.0.1
 * </pre>
 */
public final class ChangeContactActivity extends AutoPushPopActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        // 设置ActionBar
        super.setActionBar(this, "修改联系人信息");

        // 绑定控件
        EditText name = findViewById(R.id.create_contact_name_et);
        EditText email = findViewById(R.id.create_contact_email_et);
        Button button = findViewById(R.id.create_contact_button);
        // 由于本Activity和修改联系人Activity共用一个layout，需要设置一下按钮文本
        button.setText("修改");
        button.setOnClickListener(view -> {

        });
    }
}
