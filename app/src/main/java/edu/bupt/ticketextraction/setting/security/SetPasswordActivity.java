package edu.bupt.ticketextraction.setting.security;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;

import java.io.Serializable;

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
public final class SetPasswordActivity extends AutoPushPopActivity {
    public final static String TITLE_EXTRA = "title";
    public final static String BUTTON_TEXT_EXTRA = "button_text";
    public String phoneNumber;
    // 要显示的标题：注册账号、找回密码、修改密码
    public Titles activityTitle;
    // 按钮显示的文本：注册、重置密码
    public ButtonTexts setPasswordButtonText;
    private RadioButton step1;
    private RadioButton step2;

    /**
     * 展示重置密码Fragment
     */
    public void showResetFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 把重置fragment添加到Activity中
        Fragment resetFragment = new SetPasswordFragment(this);
        transaction.replace(R.id.retrieve_fragment_container, resetFragment);
        transaction.commit();
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // 设置第一步为没被点击状态
        step1.setChecked(false);
        // 设置第二步为被点击状态
        step2.setChecked(true);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_password);

        // 获取AccountActivity传入变量
        Intent intent = getIntent();
        activityTitle = (Titles) intent.getSerializableExtra(TITLE_EXTRA);
        setPasswordButtonText = (ButtonTexts) intent.getSerializableExtra(BUTTON_TEXT_EXTRA);

        // 设置ActionBar
        super.setActionBar(this, String.valueOf(activityTitle.toString()));

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
        Fragment verificationFragment = new VerificationFragment(this);
        transaction.add(R.id.retrieve_fragment_container, verificationFragment);
//        // 把重置fragment添加到Activity中
//        resetFragment = new SetPasswordFragment(this);
//        transaction.add(R.id.retrieve_fragment_container, resetFragment);
        // 展示验证fragment
//        transaction.hide(resetFragment);
        // commit是异步提交，会出现切换Fragment时控件未完全加载的bug
        transaction.commit();
    }

    /**
     * 为了在Activity间传递，实现了Serializable接口<br>
     * 定义标题内容枚举类<br>
     * 包括注册账号、找回密码、修改密码<br>
     */
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    public enum Titles implements Serializable {
        REGISTER("注册账号"),
        RETRIEVE("找回密码"),
        CHANGE("修改密码");

        private final String title;

        Titles(String title) {
            this.title = title;
        }

        /**
         * @return 该枚举对应文本
         */
        @Override
        public String toString() {
            return title;
        }
    }

    /**
     * 为了在Activity间传递，实现了Serializable接口<br>
     * 定义按钮内容枚举类<br>
     * 包括注册、重置密码<br>
     */
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    public enum ButtonTexts implements Serializable {
        REGISTER("注册"),
        RETRIEVE("重置密码"),
        CHANGE("重置密码");

        private final String buttonText;

        ButtonTexts(String buttonText) {
            this.buttonText = buttonText;
        }

        /**
         * @return 该枚举对应文本
         */
        @Override
        public String toString() {
            return buttonText;
        }
    }
}
