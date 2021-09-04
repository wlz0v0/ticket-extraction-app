package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.wallet.Wallet;

import java.io.File;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/02
 *     desc   : 展示钱包信息activity
 *     version: 0.0.1
 * </pre>
 */
public class WalletActivity extends AppCompatActivity {
    private static Wallet curWallet;

    public static void setWallet(Wallet Wallet) {
        WalletActivity.curWallet = Wallet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        Button shootBtn = findViewById(R.id.camera_shoot_btn);
        shootBtn.setOnClickListener(this::shootBtnOnClickCallback);

        Button recordBtn = findViewById(R.id.camera_record_btn);
        recordBtn.setOnClickListener(this::recordBtnOnClickCallback);

        showResources();
    }

    private void shootBtnOnClickCallback(View view) {
        File imageFile = curWallet.createImage();
    }

    private void recordBtnOnClickCallback(View view) {

    }

    // 在钱包中展示资源文件
    private void showResources() {
        //TODO: 通过curWallet的属性展示资源
    }
}
