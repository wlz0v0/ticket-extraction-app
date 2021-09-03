package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.fragment.*;
import edu.bupt.ticketextraction.wallet.Wallet;
import edu.bupt.ticketextraction.wallet.WalletManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.Integer;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/03
 *     desc   : 主activity，包含四个菜单栏，对应四个fragment
 *     version: 0.0.1
 * </pre>
 */

public class MainActivity extends AppCompatActivity {
    // 用于创建FragmentTransaction，以展示fragment
    private FragmentManager fgMng;

    // 利用hashmap简化if else语句，fragments保存fragment的id和对象的映射关系
    // Fragment利用多态，实际为继承了Fragment的自定义Fragment
    private HashMap<Integer, Fragment> fragments;

    // 跳转到其他activity前的fragment的id，默认为发票fragment
    private static int beforeJumpFragmentId = R.id.bill;

    // 存放wallet fragment的map
    // value保存的是该key是否已经被添加到FragmentTransaction中
    public static HashMap<WalletFragment, Boolean> walletFragments = new HashMap<>();

    public void jumpFromMainToWallet() {
        beforeJumpFragmentId = R.id.bill;
        Intent intent = new Intent(MainActivity.this, WalletActivity.class);
        startActivity(intent);
    }

    public void jumpFromMainToCreateWallet() {
        beforeJumpFragmentId = R.id.bill;
        Intent intent = new Intent(MainActivity.this, CreateWalletActivity.class);
        startActivity(intent);
    }

    // 跳转到login activity
    public void jumpFromMainToLogin() {
        // 从设置fragment跳转
        beforeJumpFragmentId = R.id.setting;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // 跳转到send to email activity
    public void jumpFromMainToEmail() {
        // 从导出fragment跳转
        beforeJumpFragmentId = R.id.export;
        Intent intent = new Intent(MainActivity.this, SendToEmailActivity.class);
        startActivity(intent);
    }

    // 跳转到about us activity
    public void jumpFromMainToAboutUs() {
        // 从设置fragment跳转
        beforeJumpFragmentId = R.id.setting;
        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 隐藏上部导航栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        fgMng = getSupportFragmentManager();

        initFragments();

        // 将main activity传入create wallet activity
        CreateWalletActivity.setMainActivity(this);

        // 设置控制底边菜单栏的RadioGroup的checked change事件监听器
        RadioGroup rgMenu = findViewById(R.id.radio_group);
        rgMenu.setOnCheckedChangeListener(this::bottomRadioGroupOnCheckedChangedCallback);

        // 设置相机按钮被点击的事件监听器
        Button cameraBtn = findViewById(R.id.camera_btn);
        cameraBtn.setOnClickListener(this::cameraButtonOnClickCallback);

        // 从文件中读取wallets的数据
        WalletManager.getInstance().readFromData();

        // 设置跳转前fragment的checked为true以在初始界面展示
        RadioButton beforeJumpButton = findViewById(beforeJumpFragmentId);
        beforeJumpButton.setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 展示所有钱包fragment
        if (!walletFragments.isEmpty()) {
            showWalletFragments();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将wallets中的数据保存到文件中
        WalletManager.getInstance().writeToData();
    }

    // 初始化所有界面fragment
    private void initFragments() {
        BillFragment fgBill = new BillFragment(this);
        ExportFragment fgExport = new ExportFragment(this);
        CheckFragment fgCheck = new CheckFragment("这个页面做发票验真功能");
        SettingFragment fgSetting = new SettingFragment(this);

        // 把fragment以及对应的id存入hashmap中
        fragments = new HashMap<>();
        fragments.put(R.id.bill, fgBill);
        fragments.put(R.id.export, fgExport);
        fragments.put(R.id.check, fgCheck);
        fragments.put(R.id.setting, fgSetting);

        // 初始化FragmentTransaction并将fragments添加到activity中
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();

        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            Fragment fg = entry.getValue();
            fragmentTransaction.add(R.id.fragment_container, fg);
        }
        fragmentTransaction.commit();
        hideFragments(fragmentTransaction);
    }

    /**
     * 在MainActivity中展示是因为
     * 如果在CreateWalletActivity中使用FragmentTransaction添加fragment
     * 由于未知原因app会崩掉
     * 猜测：添加fragment时必须处于该fragment所处于的activity中，否则会出现问题
     */
    private void showWalletFragments() {
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();
        for (Map.Entry<WalletFragment, Boolean> w : walletFragments.entrySet()) {
            if (!w.getValue()) {
                fragmentTransaction.add(R.id.wallet_fragment_container, w.getKey());
                w.setValue(true);
            } else {
                fragmentTransaction.show(w.getKey());
            }
        }
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    // 把所有wallet读进wallets中，需要创建一个文件管理wallet
    private void readWallets() {
        //TODO 把所有钱包读进wallets中
    }

    // 底部单选按钮点击事件监听器回调函数
    private void bottomRadioGroupOnCheckedChangedCallback(RadioGroup radioGroup, int checkId) {
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();
        hideFragments(fragmentTransaction);
        // 通过id展示对应的fragment
        Fragment fg = fragments.get(checkId);
        // 断言fragment不为null
        assert fg != null;
        fragmentTransaction.show(fg);
        fragmentTransaction.commit();
    }

    // 相机按钮点击事件监听器回调函数
    private void cameraButtonOnClickCallback(View view) {
        //TODO: 不在MainActivity中设置拍照，而改在wallet activity中调用相机，两个按钮分别为照相和录制
        // perfect!
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            fragmentTransaction.hide(entry.getValue());
        }
    }
}
