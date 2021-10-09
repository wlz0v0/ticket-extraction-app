package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.fragment.SetPasswordFragment;
import edu.bupt.ticketextraction.fragment.VerificationFragment;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/06
 *     desc   : 设置密码Activity
 *              支持注册账号、找回密码、修改密码
 *              TODO:Fragment切换动画
 *     version: 0.0.1
 * </pre>
 */
public class SetPasswordActivity extends AppCompatActivity {
    private Fragment verificationFragment;
    private Fragment resetFragment;
    private RadioButton step1;
    private RadioButton step2;
    public String phoneNumber;
    // 要显示的标题：注册账号、找回密码、修改密码
    public static Titles title;
    // 按钮显示的文本：注册、重置密码
    public static ButtonTexts setPasswordButtonText;

    /**
     * 定义标题内容枚举类<br>
     * 包括注册账号、找回密码、修改密码<br>
     */
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    public enum Titles {
        REGISTER("注册账号"),
        RETRIEVE("找回密码"),
        CHANGE("修改密码");

        private final String title;

        Titles(String title) {
            this.title = title;
        }
    }

    /**
     * 定义按钮内容枚举类<br>
     * 包括注册、重置密码<br>
     */
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    public enum ButtonTexts {
        REGISTER("注册"),
        RETRIEVE("重置密码"),
        CHANGE("重置密码");

        private final String buttonText;

        ButtonTexts(String buttonText) {
            this.buttonText = buttonText;
        }
    }

    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回main activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 展示重置密码Fragment
     */
    public void showResetFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(verificationFragment);
        transaction.show(resetFragment);
        transaction.commitAllowingStateLoss();

        // 设置第一步为没被点击状态
        step1.setChecked(false);
        //设置第二步为被点击状态
        step2.setChecked(true);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        // 创建顶部导航栏
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(String.valueOf(title));
        }

        // 绑定按钮
        step1 = findViewById(R.id.step1_radio_button);
        step2 = findViewById(R.id.step2_radio_button);
        // 设置不可点击
        step1.setClickable(false);
        step2.setClickable(false);
        // 设置第一步为被点击状态
        step1.setChecked(true);

        // 先进行手机号验证，再重置密码
        // 把验证fragment添加到Activity中
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        verificationFragment = new VerificationFragment(this);
        transaction.add(R.id.retrieve_fragment_container, verificationFragment);
        // 把重置fragment添加到Activity中
        resetFragment = new SetPasswordFragment(this);
        transaction.add(R.id.retrieve_fragment_container, resetFragment);
        // 展示验证fragment
        transaction.hide(resetFragment);
        transaction.commitAllowingStateLoss();
    }
}
