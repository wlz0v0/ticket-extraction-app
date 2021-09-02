package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.wallet.Wallet;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        EditText wallet_name_et = findViewById(R.id.wallet_name_et);

        Button create_btn = findViewById(R.id.create_btn);
        create_btn.setOnClickListener(view -> {
            String walletName = wallet_name_et.getText().toString();
            Wallet wallet = new Wallet(walletName);
            MainActivity.wallets.add(wallet);
        });
    }
}
