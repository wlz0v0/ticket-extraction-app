package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.email.Email;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/25
 *     desc   : 将识别结果发送到邮箱的activity
 *     version: 0.0.1
 * </pre>
 */

public class SendToEmailActivity extends AppCompatActivity {
    private String email_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_email);
        // 创建顶部导航栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setTitle("发送到邮箱");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        EditText email_et = findViewById(R.id.email_address_et);

        Button send_btn = findViewById(R.id.send_btn);
        send_btn.setOnClickListener(view -> {
            email_address = email_et.getText().toString();
            sendEmail();
        });
    }

    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回main activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {
        Email email = new Email(email_address);
        email.send();
    }
}
