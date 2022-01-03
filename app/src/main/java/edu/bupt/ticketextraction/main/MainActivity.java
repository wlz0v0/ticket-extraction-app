package edu.bupt.ticketextraction.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.bill.wallet.*;
import edu.bupt.ticketextraction.export.SendToEmailActivity;
import edu.bupt.ticketextraction.export.WalletCheckBoxFragment;
import edu.bupt.ticketextraction.setting.AboutUsActivity;
import edu.bupt.ticketextraction.setting.LoginActivity;
import edu.bupt.ticketextraction.setting.PersonInfoActivity;
import edu.bupt.ticketextraction.utils.file.filefactory.FileFactory;
import edu.bupt.ticketextraction.utils.ocr.Ocr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/03
 *     desc   : 根Activity，包含四个菜单栏，对应四个fragment
 *     version: 0.0.1
 * </pre>
 */
public final class MainActivity extends AutoPushPopActivity {
    /**
     * 当前版本号
     */
    public static final String CUR_VERSION = "0.0.0";
    /**
     * LinkedHashMap防止打乱顺序
     * key 钱包fragment
     * value fragment是否已经被添加到activity中
     **/
    public static LinkedHashMap<WalletButtonFragment, Boolean> walletButtonFragments;
    /**
     * LinkedHashMap防止打乱顺序
     * key 钱包fragment
     * value fragment是否已经被添加到activity中
     **/
    public static LinkedHashMap<WalletCheckBoxFragment, Boolean> walletCheckBoxFragments;
    // 跳转到其他activity前的fragment的id，默认为发票fragment
    private static int beforeJumpFragmentId = R.id.bill;
    // 用于创建FragmentTransaction，以展示fragment
    private FragmentManager fgMng;
    // 利用hashmap简化if else语句，fragments保存fragment的id和对象的映射关系
    // Fragment利用多态，实际为继承了Fragment的自定义Fragment
    private HashMap<Integer, Fragment> fragments;

    /**
     * 跳转到钱包activity
     *
     * @param wallet 跳转的钱包
     */
    public void jumpFromMainToWallet(Wallet wallet) {
        // 从发票fragment跳转
        beforeJumpFragmentId = R.id.bill;
        Intent intent = new Intent(MainActivity.this, WalletActivity.class);
        // 序列化传参会生成一个新对象，无法获取之前的对象，所以采用静态setter
        WalletActivity.setWallet(wallet);
        startActivity(intent);
    }

    /**
     * 跳转到创建钱包activity
     */
    public void jumpFromMainToCreateWallet() {
        // 从发票fragment跳转
        beforeJumpFragmentId = R.id.bill;
        Intent intent = new Intent(MainActivity.this, CreateWalletActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到LoginActivity
     */
    public void jumpFromMainToLogin() {
        // 从设置fragment跳转
        beforeJumpFragmentId = R.id.setting;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到PersonInfoActivity
     */
    public void jumpFromMainToPersonInfo() {
        beforeJumpFragmentId = R.id.setting;
        Intent intent = new Intent(MainActivity.this, PersonInfoActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到SendToEmailActivity
     */
    public void jumpFromMainToEmail() {
        // 从导出fragment跳转
        beforeJumpFragmentId = R.id.export;
        Intent intent = new Intent(MainActivity.this, SendToEmailActivity.class);
        startActivity(intent);
    }

    /**
     * 跳转到AboutUsActivity
     */
    public void jumpFromMainToAboutUs() {
        // 从设置fragment跳转
        beforeJumpFragmentId = R.id.setting;
        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 首先初始化所有static变量
        initStaticVars();

        // 获取assess_token
        new Thread(Ocr::getAuth).start();
        fgMng = getSupportFragmentManager();

        ActionBar actionBar = getSupportActionBar();
        // 设置自定义顶部actionbar
        // 设置使用说明按钮点击监听器
        if (actionBar != null) {
            actionBar.setCustomView(R.layout.actionbar_main_activity);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            // 显示自定义的view
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setDisplayShowHomeEnabled(false);
            // 不显示标题
            actionBar.setDisplayShowTitleEnabled(false);
        }

        initFragments();

        // 设置控制底边菜单栏的RadioGroup的checked change事件监听器
        RadioGroup rgMenu = findViewById(R.id.radio_group);
        rgMenu.setOnCheckedChangeListener(this::bottomRadioGroupOnCheckedChangedCallback);

        // 从文件中读取wallets的数据
        WalletManager.getInstance().readWalletsFromData();

        // 设置跳转前fragment的checked为true以在初始界面展示
        RadioButton beforeJumpButton = findViewById(beforeJumpFragmentId);
        beforeJumpButton.setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 展示所有wallet button fragment
        if (!walletButtonFragments.isEmpty()) {
            showWalletButtonFragments();
        }
        // 展示所有wallet check box fragment
        if (!walletCheckBoxFragments.isEmpty()) {
            showWalletCheckBoxFragments();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将wallets中的数据保存到文件中
        WalletManager.getInstance().writeWalletsToData();
    }

    // 初始化所有static变量
    private void initStaticVars() {
        walletButtonFragments = new LinkedHashMap<>();
        walletCheckBoxFragments = new LinkedHashMap<>();
        // 存放所有CheckBox的数组
        SendToEmailActivity.checkBoxes = new ArrayList<>();
        // 存放所有钱包名和钱包CheckBox对应关系的HashMap
        WalletCheckBoxFragment.checkBoxFragmentHashMap = new HashMap<>();

        // 获取外部存储绝对路径字符串
        FileFactory.EXTERNAL_FILE_DIR = getExternalFilesDir(null).getAbsolutePath();
        FileFactory.WALLETS_DIR = FileFactory.EXTERNAL_FILE_DIR + "/wallets/";

        // 将main activity传入WalletManager
        WalletManager.setMainActivity(this);
    }

    // 初始化所有界面fragment
    private void initFragments() {
        BillFragment fgBill = new BillFragment(this);
        ExportFragment fgExport = new ExportFragment(this);
        InstructionFragment fgCheck = new InstructionFragment(this);
        SettingFragment fgSetting = new SettingFragment(this);

        // 把fragment以及对应的id存入hashmap中
        fragments = new HashMap<>();
        fragments.put(R.id.bill, fgBill);
        fragments.put(R.id.export, fgExport);
        fragments.put(R.id.instruction, fgCheck);
        fragments.put(R.id.setting, fgSetting);

        // 初始化FragmentTransaction并将fragments添加到activity中
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();

        // 原本希望使用JDK11关键字var来代替Map.Entry
        // 结果把开发环境换成JDK11后无法识别android包
        // 只好换成JDK8然后不用var了
        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            Fragment fg = entry.getValue();
            fragmentTransaction.add(R.id.fragment_container, fg);
        }
        fragmentTransaction.commit();
        hideFragments(fragmentTransaction);
    }

    /**
     * 注：在MainActivity中展示是因为
     * 如果在CreateWalletActivity中使用FragmentTransaction添加fragment<br>
     * 由于未知原因app会崩掉<br>
     * 猜测：添加fragment时必须处于该fragment所处于的activity中，否则会出现问题
     */
    private void showWalletButtonFragments() {
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();
        for (Map.Entry<WalletButtonFragment, Boolean> w : walletButtonFragments.entrySet()) {
            if (!w.getValue()) {
                fragmentTransaction.add(R.id.wallet_fragment_container_in_bill, w.getKey());
                w.setValue(true);
            } else {
                fragmentTransaction.show(w.getKey());
            }
        }
        fragmentTransaction.commitNowAllowingStateLoss();
    }

    /**
     * 注：在MainActivity中展示是因为
     * 如果在CreateWalletActivity中使用FragmentTransaction添加fragment
     * 由于未知原因app会崩掉
     * 猜测：添加fragment时必须处于该fragment所处于的activity中，否则会出现问题
     */
    private void showWalletCheckBoxFragments() {
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();
        for (Map.Entry<WalletCheckBoxFragment, Boolean> w : walletCheckBoxFragments.entrySet()) {
            if (!w.getValue()) {
                fragmentTransaction.add(R.id.wallet_fragment_container_in_export, w.getKey());
                w.setValue(true);
            } else {
                fragmentTransaction.show(w.getKey());
            }
        }
        fragmentTransaction.commitNowAllowingStateLoss();
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

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            fragmentTransaction.hide(entry.getValue());
        }
    }
}
