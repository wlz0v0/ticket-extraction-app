package edu.bupt.ticketextraction.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/09/26
 *     desc   : 注册Activity，用于注册新账号
 *     version: 0.0.1
 * </pre>
 */
public class RegisterActivity extends AppCompatActivity {
    // 手机账号默认正确，不做判断
    private TextView passwordWarning;
    private TextView rePasswordWarning;
    private EditText phoneNumberEt;
    private EditText passWordEt;
    private EditText rePassWordEt;
    // 大写字母+数字+小写字母 3~16位
    private final static Pattern passwordPattern = Pattern.compile("[A-Za-z0-9]{3,16}");

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
        setContentView(R.layout.activity_register);

        // 创建顶部导航栏
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("注册");
        }

        // 绑定错误提醒文本框
        passwordWarning = findViewById(R.id.password_warning);
        rePasswordWarning = findViewById(R.id.re_password_warning);

        // 绑定EditText
        phoneNumberEt = findViewById(R.id.register_account);
        passWordEt = findViewById(R.id.register_password);
        rePassWordEt = findViewById(R.id.register_re_password);

        // 注册按钮初始化及点击事件监听器设置
        Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(view -> {
            if (isValid()) {
                callServerRegister();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Thread checkValid = new Thread(() -> {
            while (true) {
                passwordHandle(isPasswordValid());
                rePasswordHandle(isRePasswordValid());
            }
        });
        checkValid.start();
    }

    private boolean isValid() {
        String password = passWordEt.getText().toString();
        String rePassword = rePassWordEt.getText().toString();
        return password.isEmpty() && rePassword.isEmpty()
                && isPasswordValid() && isRePasswordValid();
    }

    private void callServerRegister() {
        //TODO:调用用户管理系统进行注册
        String phoneNumber = phoneNumberEt.getText().toString();
    }

    private boolean isPasswordValid() {
        String password = passWordEt.getText().toString();
        if (password.isEmpty())
            return true;
        return passwordPattern.matcher(password).matches();
    }

    @SuppressLint("SetTextI18n")
    private void passwordHandle(boolean valid) {
        if (valid) {
            passwordWarning.setText("");
        } else {
            passwordWarning.setText("密码长度3~16位，包含大小写字母和数字");
        }
    }

    private boolean isRePasswordValid() {
        String rePassword = rePassWordEt.getText().toString();
        if (rePassword.isEmpty()) {
            return true;
        }
        String password = passWordEt.getText().toString();
        return password.equals(rePassword);
    }

    private void rePasswordHandle(boolean valid) {
        if (valid) {
            rePasswordWarning.setText("");
        } else {
            rePasswordWarning.setText("两次密码不匹配！");
        }

    }
}
