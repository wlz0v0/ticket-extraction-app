package edu.bupt.ticketextraction.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.Nullable;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.main.AutoPushPopActivity;
import edu.bupt.ticketextraction.setting.security.SetPasswordActivity;
import edu.bupt.ticketextraction.utils.ActivityStack;
import edu.bupt.ticketextraction.utils.Server;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/09
 *     desc   : 登录Fragment
 *     version: 0.0.1
 * </pre>
 */
public final class LoginActivity extends AutoPushPopActivity {
    /**
     * true 展示登录后的个人信息 <br>
     * false 展示登录界面
     */
    public static boolean loginState = false;

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
        // 绑定编辑文本框
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 设置ActionBar
        super.setActionBar(this, "登录");
        EditText accountEt = findViewById(R.id.account);
        EditText passwordEt = findViewById(R.id.password);

        // 注册按钮初始化及点击事件监听器设置
        TextView registerBtn = findViewById(R.id.jump_to_register_button);
        registerBtn.setClickable(true);
        registerBtn.setOnClickListener(view1 -> jumpFromAccountToRegister());

        // 找回密码初始化及点击事件监听器设置
        TextView retrievePassword = findViewById(R.id.retrieve_password_button);
        retrievePassword.setClickable(true);
        retrievePassword.setOnClickListener(view1 -> jumpFromAccountToRetrievePassword());

        // 登录按钮初始化及点击事件监听器设置
        Button loginBtn = findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view1 -> {
            // 获取输入的账号和密码
            String account = accountEt.getText().toString();
            String password = passwordEt.getText().toString();
            int loginRet = Server.callLogin(account, password);
            if (loginRet == 1) {
                loginSuccessful();
            } else {
                loginFailed(loginRet);
            }
        });
    }

    private void loginSuccessful() {
        loginState = true;
        // 通过调用finish结束LoginActivity，登录成功
        getAlertDialog("登录成功",
                (dialogInterface, i) -> {
                    // 结束Activity并弹出栈
                    ActivityStack.getInstance().finishActivity();
                })
                .show();
    }

    private void loginFailed(int errorCode) {
        String builderMsg;
        // input_pwd为空则是查询不到用户名
        // 否则是密码与账号不匹配
        builderMsg = errorCode == -1 ? "用户名不存在!" : "密码错误!";
        // 登录失败，关闭提示框
        getAlertDialog(builderMsg,
                (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }
}
