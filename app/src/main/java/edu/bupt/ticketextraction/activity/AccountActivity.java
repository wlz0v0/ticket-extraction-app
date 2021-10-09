package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.fragment.LoginFragment;
import edu.bupt.ticketextraction.fragment.PersonInfoFragment;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/22
 *     desc   : 登录界面activity
 *     version: 0.0.1
 * </pre>
 */
public class AccountActivity extends AppCompatActivity {
    /**
     * true 展示登录后的个人信息 <br>
     * false 展示登录界面
     */
    public static boolean loginState = false;

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
     * 从AccountActivity跳转到SetPasswordActivity，完成注册
     */
    public void jumpFromAccountToRegister() {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        // 设置注册的文本内容
        // 通过putExtra传递变量
        intent.putExtra(SetPasswordActivity.TITLE_EXTRA, SetPasswordActivity.Titles.REGISTER);
        intent.putExtra(SetPasswordActivity.BUTTON_TEXT_EXTRA, SetPasswordActivity.ButtonTexts.REGISTER);
        startActivity(intent);
    }

    /**
     * 从AccountActivity跳转到SetPasswordActivity，完成找回密码
     */
    public void jumpFromAccountToRetrievePassword() {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        // 设置找回密码的文本内容
        // 通过putExtra传递变量
        intent.putExtra(SetPasswordActivity.TITLE_EXTRA, SetPasswordActivity.Titles.RETRIEVE);
        intent.putExtra(SetPasswordActivity.BUTTON_TEXT_EXTRA, SetPasswordActivity.ButtonTexts.RETRIEVE);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // 创建顶部导航栏
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
            String title = loginState ? "个人信息" : "登录";
            actionBar.setTitle(title);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.account_fragment_container,
                loginState ? new PersonInfoFragment() : new LoginFragment(this));
        transaction.commitAllowingStateLoss();
    }
}
