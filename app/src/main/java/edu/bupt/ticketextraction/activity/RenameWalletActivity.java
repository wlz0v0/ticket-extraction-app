package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.fragment.WalletButtonFragment;
import edu.bupt.ticketextraction.utils.ActivityStack;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/16
 *     desc   : 重命名钱包Activity，与创建钱包共用一个layout
 *     version: 0.0.1
 * </pre>
 */
public final class RenameWalletActivity extends AutoPushPopActivity {
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        // 设置ActionBar
        setActionBar(this, "重命名钱包");
        // 绑定控件
        EditText name = findViewById(R.id.wallet_name_et);
        Button button = findViewById(R.id.create_wallet_btn);
        button.setText("重命名");
        button.setOnClickListener(view -> {
            Intent intent = new Intent();
            // 将新钱包名传回去
            intent.putExtra(WalletButtonFragment.NEW_WALLET_NAME, name.getText().toString());
            // 结束当前Activity
            ActivityStack.getInstance().finishActivity();
        });
    }
}
