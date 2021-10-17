package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.utils.ActivityStack;
import edu.bupt.ticketextraction.wallet.WalletManager;

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
            if (WalletManager.getInstance().createWallet(walletName)) {
                createSuccessful();
            } else {
                createFailed();
            }
        });
    }

    private void createSuccessful() {
        AlertDialog alertDialog = getAlertDialog("创建成功",
                (dialog, which) -> {
                    // 结束Activity并弹出栈
                    ActivityStack.getInstance().finishActivity();
                });
        alertDialog.show();
    }

    private void createFailed() {
        AlertDialog alertDialog = getAlertDialog("创建失败",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}
