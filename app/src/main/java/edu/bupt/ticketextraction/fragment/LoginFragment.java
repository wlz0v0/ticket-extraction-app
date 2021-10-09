package edu.bupt.ticketextraction.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.AccountActivity;
import edu.bupt.ticketextraction.server.Server;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/09
 *     desc   : 登录Fragment
 *     version: 0.0.1
 * </pre>
 */
public class LoginFragment extends Fragment {
    private final AccountActivity fatherActivity;

    public LoginFragment(AccountActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // 绑定编辑文本框
        EditText accountEt = view.findViewById(R.id.account);
        EditText passwordEt = view.findViewById(R.id.password);

        // 注册按钮初始化及点击事件监听器设置
        TextView registerBtn = view.findViewById(R.id.jump_to_register_button);
        registerBtn.setClickable(true);
        registerBtn.setOnClickListener(view1 -> fatherActivity.jumpFromAccountToRegister());

        // 找回密码初始化及点击事件监听器设置
        TextView retrievePassword = view.findViewById(R.id.retrieve_password_button);
        retrievePassword.setClickable(true);
        retrievePassword.setOnClickListener(view1 -> fatherActivity.jumpFromAccountToRetrievePassword());

        // 登录按钮初始化及点击事件监听器设置
        Button loginBtn = view.findViewById(R.id.login_btn);
        loginBtn.setOnClickListener(view1 -> {
            // 获取输入的账号和密码
            String account = accountEt.getText().toString();
            String password = passwordEt.getText().toString();
            int loginRet;
            if ((loginRet = Server.callLogin(account, password)) == 1) {
                loginSuccessful();
            } else {
                loginFailed(loginRet);
            }
        });
        return view;
    }

    private void loginSuccessful() {
        AccountActivity.loginState = true;
        // 通过调用finish结束LoginActivity，登录成功
        getAlertDialog("登录成功",
                (dialogInterface, i) -> fatherActivity.finish())
                .show();
    }

    private void loginFailed(int errorCode) {
        AccountActivity.loginState = false;
        String builderMsg;
        // input_pwd为空则是查询不到用户名
        // 否则是密码与账号不匹配
        builderMsg = errorCode == -1 ? "用户名不存在!" : "密码错误!";
        // 登录失败，关闭提示框
        getAlertDialog(builderMsg,
                (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    private @NotNull AlertDialog getAlertDialog(String text,
                                                DialogInterface.OnClickListener onClickListener) {
        // 先创建一个builder，再通过builder构造alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", onClickListener);
        return builder.create();
    }
}
