package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.fragment.ResetPasswordFragment;
import edu.bupt.ticketextraction.fragment.VerificationFragment;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/06
 *     desc   : 找回密码Activity
 *     version: 0.0.1
 * </pre>
 */
public class RetrievePasswordActivity extends AppCompatActivity {
    private Fragment verificationFragment;
    private Fragment resetFragment;
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
     * 展示验证Fragment
     */
    public void showVerificationFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(verificationFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 展示重置密码Fragment
     */
    public void showResetFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.show(resetFragment);
        transaction.commitAllowingStateLoss();
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
            actionBar.setTitle("找回密码");
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 先进行手机号验证，再重置密码
        // 把验证fragment添加到Activity中
        verificationFragment = new VerificationFragment();
        transaction.add(R.id.retrieve_fragment_container, verificationFragment);
        // 把重置fragment添加到Activity中
        resetFragment = new ResetPasswordFragment();
        transaction.add(R.id.retrieve_fragment_container, resetFragment);
        // 展示验证fragment
        transaction.hide(resetFragment);
        transaction.commitAllowingStateLoss();
    }
}
