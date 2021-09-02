package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.fragment.BillFragment;
import edu.bupt.ticketextraction.fragment.CheckFragment;
import edu.bupt.ticketextraction.fragment.ExportFragment;
import edu.bupt.ticketextraction.fragment.SettingFragment;
import edu.bupt.ticketextraction.wallet.Wallet;

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
    private static int before_jump_fragment_id = R.id.bill;

    public static ArrayList<Wallet> wallets;

    // 跳转到login activity
    public void jumpFromMainToLogin() {
        // 从设置fragment跳转
        before_jump_fragment_id = R.id.setting;
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // 跳转到send to email activity
    public void jumpFromMainToEmail() {
        // 从导出fragment跳转
        before_jump_fragment_id = R.id.export;
        Intent intent = new Intent(MainActivity.this, SendToEmailActivity.class);
        startActivity(intent);
    }

    // 跳转到about us activity
    public void jumpFromMainToAboutUs() {
        // 从设置fragment跳转
        before_jump_fragment_id = R.id.setting;
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

        // 设置控制底边菜单栏的RadioGroup的checked change事件监听器
        RadioGroup rg_menu = findViewById(R.id.radio_group);
        rg_menu.setOnCheckedChangeListener(this::bottomRadioGroupOnCheckedChangedCallback);

        // 设置相机按钮被点击的事件监听器
        Button camera_btn = findViewById(R.id.camera_btn);
        camera_btn.setOnClickListener(this::cameraButtonOnClickCallback);

        // 设置跳转前fragment的checked为true以在初始界面展示
        RadioButton before_jump_button = findViewById(before_jump_fragment_id);
        before_jump_button.setChecked(true);

        // 把所有wallet读进wallets中
        readWallets();
        // 读取每个wallet的数据并把wallet展示到MainActivity中
        for (Wallet wallet : wallets) {
            wallet.readData();
            wallet.show();
        }
    }

    // 初始化所有fragment
    private void initFragments() {
        BillFragment fg_bill = new BillFragment();
        ExportFragment fg_export = new ExportFragment(this);
        CheckFragment fg_check = new CheckFragment("这个页面暂时还没想好干啥");
        SettingFragment fg_setting = new SettingFragment(this);

        // 把fragment以及对应的id存入hashmap中
        fragments = new HashMap<>();
        fragments.put(R.id.bill, fg_bill);
        fragments.put(R.id.export, fg_export);
        fragments.put(R.id.check, fg_check);
        fragments.put(R.id.setting, fg_setting);

        // 初始化FragmentTransaction并将fragments添加到activity中
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();

        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            Fragment fg = entry.getValue();
            fragmentTransaction.add(R.id.fragment_container, fg);
        }
        fragmentTransaction.commit();
        hideFragments(fragmentTransaction);
    }

    // 把所有wallet读进wallets中，需要创建一个文件管理wallet
    private void readWallets() {
        //TODO 把所有钱包读进wallets中
    }

    // 底部单选按钮点击事件监听器回调函数
    private void bottomRadioGroupOnCheckedChangedCallback(RadioGroup radioGroup, int check_id) {
        FragmentTransaction fragmentTransaction = fgMng.beginTransaction();
        hideFragments(fragmentTransaction);
        // 通过id展示对应的fragment
        Fragment fg = fragments.get(check_id);
        // 断言fragment不为null
        assert fg != null;
        fragmentTransaction.show(fg);
        fragmentTransaction.commit();
    }

    // 相机按钮点击事件监听器回调函数
    private void cameraButtonOnClickCallback(View view) {
        //TODO: by xhy
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        for (Map.Entry<Integer, Fragment> entry : fragments.entrySet()) {
            fragmentTransaction.hide(entry.getValue());
        }
    }
}
