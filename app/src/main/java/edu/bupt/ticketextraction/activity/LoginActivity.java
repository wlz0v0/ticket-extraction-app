package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/22
 *     desc   : 登录界面activity
 *     version: 0.0.1
 * </pre>
 */
public class LoginActivity extends AppCompatActivity {
    // true 展示登录后的个人信息
    // false 展示登录界面
    public static boolean loginState = false;

    private String account;
    private String password;
    private HashMap<String, String> accountInfo;

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

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (loginState) {
            showPersonalInfo();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        setContentView(R.layout.activity_login);
        // 创建顶部导航栏
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("登录");
        }
        //TODO:登录界面

        // 绑定编辑文本框
        EditText accountEt = findViewById(R.id.account);
        EditText passwordEt = findViewById(R.id.password);

        accountInfo = new HashMap<>();
        accountInfo.put("18863238727", "123456");
        // 注册按钮初始化及点击事件监听器设置
        Button registerBtn = findViewById(R.id.jump_to_register_button);
        registerBtn.setOnClickListener(view -> jumpFromLoginToRegister());

        // 登录按钮初始化及点击事件监听器设置
        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view -> {
            // 获取输入的账号和密码
            account = accountEt.getText().toString();
            password = passwordEt.getText().toString();
            if (isMatch()) {
                //TODO:登录成功
                loginSuccessful();
            } else {
                //TODO:登录失败
                loginFailed();
            }
        });
    }

    private void showPersonalInfo() {
        setContentView(R.layout.activity_personal_info);
        // 创建顶部导航栏
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("个人信息");
        }
        //TODO
    }

    private boolean isMatch() {
        //TODO:数据库存用户名密码，去数据库中查询
        String inputPwd = accountInfo.get(account);
        return inputPwd != null && inputPwd.equals(password);
    }

    private void loginSuccessful() {
        loginState = true;
        // 通过调用finish结束LoginActivity，登录成功
        getAlertDialog("登录成功",
                (dialogInterface, i) -> finish())
                .show();
    }

    private void loginFailed() {
        loginState = false;
        String builderMsg;
        String inputPwd = accountInfo.get(account);
        // input_pwd为空则是查询不到用户名
        // 否则是密码与账号不匹配
        builderMsg = inputPwd == null ? "用户名不存在!" : "密码错误!";
        // 登录失败，关闭提示框
        getAlertDialog(builderMsg,
                (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private void jumpFromLoginToRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private @NotNull AlertDialog getAlertDialog(String text,
                                                DialogInterface.OnClickListener onClickListener) {
        // 先创建一个builder，再通过builder构造alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", onClickListener);
        return builder.create();
    }
}
