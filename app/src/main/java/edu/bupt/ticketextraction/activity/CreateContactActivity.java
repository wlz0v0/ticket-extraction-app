package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.utils.ActivityStack;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/15
 *     desc   : 创建联系人的Activity, 与修改联系人共用一个Activity
 *     version: 0.0.1
 * </pre>
 */
public final class CreateContactActivity extends AutoPushPopActivity {
    public static String title = "添加联系人";
    public static String buttonText = "添加";

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);

        // 设置ActionBar
        super.setActionBar(this, title);

        // 绑定控件
        EditText name = findViewById(R.id.create_contact_name_et);
        EditText email = findViewById(R.id.create_contact_email_et);
        Button button = findViewById(R.id.create_contact_button);
        // 由于创建联系人和修改联系人共用一个Activity，需要设置一下按钮文本
        button.setText(buttonText);
        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            // 将数据返回到PersonInfoActivity中
            intent.putExtra(PersonInfoActivity.NEW_NAME, name.getText().toString());
            intent.putExtra(PersonInfoActivity.NEW_EMAIL, email.getText().toString());
            setResult(1, intent);
            // 结束此Activity
            ActivityStack.getInstance().finishActivity();
        });
    }
}
