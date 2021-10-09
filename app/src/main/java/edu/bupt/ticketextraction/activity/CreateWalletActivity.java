package edu.bupt.ticketextraction.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.wallet.WalletManager;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 创建钱包activity
 *     version: 0.0.1
 * </pre>
 */
public class CreateWalletActivity extends AppCompatActivity {
    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回main activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 结束Activity并弹出栈
            ActivityStack.getInstance().popActivity();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("创建钱包");
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        EditText walletNameEt = findViewById(R.id.wallet_name_et);

        Button createBtn = findViewById(R.id.create_btn);
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
                    ActivityStack.getInstance().popActivity();
                    finish();
                });
        alertDialog.show();
    }

    private void createFailed() {
        AlertDialog alertDialog = getAlertDialog("创建失败",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private @NotNull AlertDialog getAlertDialog(String text,
                                                DialogInterface.OnClickListener onClickListener) {
        // 先创建一个builder，再通过builder构造alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", onClickListener);
        return builder.create();
    }
}
