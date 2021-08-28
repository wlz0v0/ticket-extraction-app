package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;

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

        EditText email_et = findViewById(R.id.email_address_et);

        Button send_btn = findViewById(R.id.send_btn);
        send_btn.setOnClickListener(view -> {
            email_address = email_et.getText().toString();
            sendEmail();
        });
    }

    private void sendEmail() {
        //TODO:发送到邮箱
    }
}
