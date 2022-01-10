package edu.bupt.ticketextraction.bill.wallet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;
import edu.bupt.ticketextraction.utils.ActivityStack;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 创建钱包activity
 *     version: 0.0.1
 * </pre>
 */
public final class CreateWalletActivity extends AutoPushPopActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        // 设置ActionBar
        super.setActionBar(this, "创建钱包");

        EditText walletNameEt = findViewById(R.id.wallet_name_et);

        Button createBtn = findViewById(R.id.create_wallet_btn);
        createBtn.setOnClickListener(view -> {
            String walletName = walletNameEt.getText().toString();
            // 钱包名为空弹出提示
            if (walletName.equals("")) {
                showBottomToast(this, "钱包名不能为空！", 3);
                return;
            }
            if (WalletManager.getInstance().createWallet(walletName)) {
                createSuccessful();
            } else {
                createFailed();
            }
        });
    }

    private void createSuccessful() {
        QMUIDialog alertDialog = getAlertDialog("创建成功",
                (dialog, which) -> {
                    // 结束Activity并弹出栈
                    ActivityStack.getInstance().finishActivity();
                });
        alertDialog.show();
    }

    private void createFailed() {
        QMUIDialog alertDialog = getAlertDialog("创建失败",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
