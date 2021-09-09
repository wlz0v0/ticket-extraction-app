package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.email.Email;

import java.util.ArrayList;

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
    private String emailAddress;

    public static ArrayList<CheckBox> checkBoxes;

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

        EditText emailEt = findViewById(R.id.email_address_et);

        Button sendBtn = findViewById(R.id.send_btn);
        sendBtn.setOnClickListener(view -> {
            emailAddress = emailEt.getText().toString();
            sendEmail();
        });
    }

    private void sendEmail() {
        for (CheckBox cb : checkBoxes) {
            if (cb.isChecked()) {
                Log.d("", "");
                // TODO: 将此钱包对应的excel添加到附件中！
            }
        }
        Email email = new Email(emailAddress);
        if (email.send()) {
            sendSuccessful();
        } else {
            sendFailed();
        }
    }

    private void sendSuccessful() {
        getAlertDialog("发送邮件成功").show();
    }

    private void sendFailed() {
        getAlertDialog("发送邮件失败").show();
    }

    private AlertDialog getAlertDialog(String text) {
        // 先创建一个builder，再通过builder构造alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", (dialog, which) ->
                    dialog.dismiss());
        return builder.create();
    }
}
