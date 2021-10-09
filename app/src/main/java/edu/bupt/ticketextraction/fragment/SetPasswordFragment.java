package edu.bupt.ticketextraction.fragment;

import android.annotation.SuppressLint;
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
import edu.bupt.ticketextraction.activity.SetPasswordActivity;
import edu.bupt.ticketextraction.server.Server;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/06
 *     desc   : 设置密码Fragment
 *              注册和找回密码修改密码共用一套
 *     version: 0.0.1
 * </pre>
 */
public class SetPasswordFragment extends Fragment {
    private TextView passwordWarning;
    private TextView rePasswordWarning;
    private EditText passwordEt;
    private EditText rePasswordEt;
    private final SetPasswordActivity fatherActivity;
    // 大写字母+数字+小写字母 3~16位
    private final static Pattern passwordPattern = Pattern.compile("[A-Za-z0-9]{3,16}");

    public SetPasswordFragment(SetPasswordActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);
        // 绑定控件
        // 错误提醒文本框
        passwordWarning = view.findViewById(R.id.password_warning);
        rePasswordWarning = view.findViewById(R.id.re_password_warning);
        // 编辑框
        passwordEt = view.findViewById(R.id.set_password);
        rePasswordEt = view.findViewById(R.id.set_re_password);
        // 设置按钮点击监听器
        Button setPasswordBtn = view.findViewById(R.id.set_password_button);
        // 根据当前修改密码的类型来设置按钮文字提示
        setPasswordBtn.setText(String.valueOf(fatherActivity.setPasswordButtonText));
        setPasswordBtn.setOnClickListener(view1 -> {
            if (isValid()) {
                boolean isSuccess = Server.callRegister(
                        fatherActivity.phoneNumber,
                        passwordEt.getText().toString(),
                        rePasswordEt.getText().toString());
                // 断言注册成功，但估摸着可能会因为与服务器之间的连接出问题
                assert isSuccess;
                AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
                builder.setMessage("注册成功").
                        setCancelable(false).
                        setPositiveButton("确认", (dialog, which) -> dialog.dismiss());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 设置一个线程来检查密码的合法性
        Thread checkValid = new Thread(() -> {
            // 死循环就完了
            while (true) {
                passwordHandle(isPasswordValid());
                rePasswordHandle(isRePasswordValid());
            }
        });
        checkValid.start();
    }

    private boolean isValid() {
        String password = passwordEt.getText().toString();
        String rePassword = rePasswordEt.getText().toString();
        return password.isEmpty() && rePassword.isEmpty()
                && isPasswordValid() && isRePasswordValid();
    }

    private boolean isPasswordValid() {
        // 当密码为空时不做提醒
        String password = passwordEt.getText().toString();
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
        // 当重复密码为空时不做提醒
        String rePassword = rePasswordEt.getText().toString();
        if (rePassword.isEmpty()) {
            return true;
        }
        String password = passwordEt.getText().toString();
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
