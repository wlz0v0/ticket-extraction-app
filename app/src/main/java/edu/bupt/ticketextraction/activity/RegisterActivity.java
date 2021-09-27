package edu.bupt.ticketextraction.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;

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
    private TextView phoneNumberWarning;
    private TextView passwordWarning;
    private TextView rePasswordWarning;
    private final static Pattern phoneNumberPattern = Pattern.compile("1[3-9]\\d{9}");

    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回main activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        phoneNumberWarning = findViewById(R.id.phone_number_warning);
        passwordWarning = findViewById(R.id.password_warning);
        rePasswordWarning = findViewById(R.id.re_password_warning);

        // 绑定EditText
        EditText phoneNumberEt = findViewById(R.id.register_account);
        EditText passWordEt = findViewById(R.id.register_password);
        EditText rePassWordEt = findViewById(R.id.register_re_password);

        // 注册按钮初始化及点击事件监听器设置
        Button registerBtn = findViewById(R.id.register_button);
        registerBtn.setOnClickListener(view -> {
            if (isValid()) {
                callServerRegister();
            }
        });
    }

    private boolean isValid() {
        return true;
    }

    private void callServerRegister() {
        //TODO:调用用户管理系统进行注册
    }

    private boolean isPhoneNumberValid(String number) {
        return false;
    }

    private void phoneNumberHandle() {

    }

    private boolean isPasswordValid(String password) {
        return false;
    }

    private void passwordHandle() {

    }

    private boolean isRePasswordValid(String rePassword) {
        return false;
    }

    private void rePasswordHandle() {

    }
}
