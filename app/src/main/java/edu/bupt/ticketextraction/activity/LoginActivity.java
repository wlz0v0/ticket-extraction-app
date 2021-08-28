package edu.bupt.ticketextraction.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
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
    public static boolean login_state = false;

    private String account;
    private String password;
    private HashMap<String, String> account_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (login_state) {
            show_personal_info();
        } else {
            show_login();
        }
    }

    private void show_login() {
        setContentView(R.layout.activity_login);
        //TODO:登录界面

        // 编辑文本框初始化
        EditText account_et = findViewById(R.id.account);
        EditText password_et = findViewById(R.id.password);

        account_info = new HashMap<>();
        account_info.put("18863238727", "123456");

        // 登录按钮初始化及点击事件监听器设置
        Button login_btn = findViewById(R.id.login_btn);
        login_btn.setOnClickListener(view -> {
            // 获取输入的账号和密码
            account = account_et.getText().toString();
            password = password_et.getText().toString();
            if (isMatch()) {
                //TODO:登录成功
                login_successful();
            } else {
                //TODO:登录失败
                login_failed();
            }
        });
    }

    private void show_personal_info() {
        setContentView(R.layout.activity_personal_info);
        //TODO
    }

    private boolean isMatch() {
        //TODO:数据库存用户名密码，去数据库中查询
        String input_pwd = account_info.get(account);
        return input_pwd != null && input_pwd.equals(password);
    }

    private void login_successful() {
        login_state = true;
        getAlertDialog("登录成功",
                (dialogInterface, i) -> finish()) // 结束LoginActivity
                .show();
    }

    private void login_failed() {
        login_state = false;
        String builder_msg;
        String input_pwd = account_info.get(account);
        builder_msg = input_pwd == null ? "用户名错误!" : "密码错误!";
        getAlertDialog(builder_msg,
                (dialogInterface, i) -> dialogInterface.dismiss()) // 关闭提示框
                .show();
    }

    private AlertDialog getAlertDialog(String text,
                                       DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", onClickListener);
        return builder.create();
    }
}
