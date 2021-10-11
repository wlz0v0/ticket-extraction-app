package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.email.Email;
import org.jetbrains.annotations.NotNull;

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

public final class SendToEmailActivity extends AutoPushPopActivity {
    private String emailAddress;

    // checkBoxes用于发送email时确定哪些需要被发送
    public static ArrayList<CheckBox> checkBoxes;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
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

    /**
     * 获得一个弹窗，用于提高代码复用率
     * @param text 创建的弹窗的信息的字符串
     **/
    private @NotNull AlertDialog getAlertDialog(String text) {
        // 先创建一个builder，再通过builder构造alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", (dialog, which) ->
                    dialog.dismiss());
        return builder.create();
    }
}
