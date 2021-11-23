package edu.bupt.ticketextraction.export;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.bill.wallet.Wallet;
import edu.bupt.ticketextraction.bill.wallet.WalletManager;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;
import edu.bupt.ticketextraction.utils.HttpUtils;

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
    // checkBoxes用于发送email时确定哪些需要被发送
    public static ArrayList<CheckBox> checkBoxes;
    private String emailAddress;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_email);

        // 设置ActionBar
        super.setActionBar(this, "发送到邮件");

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
                Wallet w = WalletManager.getInstance().getWallet(cb.getText().toString());
                assert w != null;
                if (HttpUtils.sendEmail(w.getTickets(), emailAddress)) {
                    sendSuccessful();
                } else {
                    sendFailed();
                }
            }
        }
    }

    private void sendSuccessful() {
        showBottomToast(this, "发送邮件成功", 5);
    }

    private void sendFailed() {
        showBottomToast(this, "发送邮件失败", 5);
    }
}
