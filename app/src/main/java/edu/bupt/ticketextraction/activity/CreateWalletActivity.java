package edu.bupt.ticketextraction.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.fragment.WalletFragment;
import edu.bupt.ticketextraction.wallet.Wallet;
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
//TODO: 9.3task!
public class CreateWalletActivity extends AppCompatActivity {
    private static MainActivity mainActivity;

    /**
     *  由于在创建wallet fragment时需要设置按钮点击监听器
     *  使得点击wallet时能够从main activity跳转到对应wallet activity
     *  于是需要一个main activity的实例来进行跳转
     *  在本类中只能选择用一个静态变量获取到main activity
     *  以便将其传给wallet fragment
     */
    public static void setMainActivity(MainActivity mainActivity) {
        CreateWalletActivity.mainActivity = mainActivity;
    }

    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回main activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            if (createWallet(walletName)) {
                createSuccessful();
            } else {
                createFailed();
            }
        });
    }

    private boolean createWallet(String walletName) {
        if (walletName.isEmpty()) {
            return false;
        }
        // 新建一个wallet并添加到wallets中管理
        Wallet wallet = new Wallet(walletName);
        WalletManager.getInstance().addWallet(wallet);

        // 新建一个wallet fragment并将其添加到对应的container中
        WalletFragment fgWallet = new WalletFragment(walletName, mainActivity);
        // 将新建的wallet fragment添加到MainActivity中并在其中展示
        MainActivity.walletFragments.put(fgWallet, false);
        return true;
    }

    private void createSuccessful() {
        AlertDialog alertDialog = getAlertDialog("创建成功",
                (dialog, which) -> finish());
        alertDialog.show();
    }

    private void createFailed() {
        AlertDialog alertDialog = getAlertDialog("创建失败",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private AlertDialog getAlertDialog(String text,
                                       DialogInterface.OnClickListener onClickListener) {
        // 先创建一个builder，再通过builder构造alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", onClickListener);
        return builder.create();
    }
}
