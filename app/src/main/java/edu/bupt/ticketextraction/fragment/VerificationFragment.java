package edu.bupt.ticketextraction.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.activity.RetrievePasswordActivity;
import edu.bupt.ticketextraction.server.Server;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/06
 *     desc   : 验证身份Fragment
 *     version: 0.0.1
 * </pre>
 */
public class VerificationFragment extends Fragment {
    private EditText phoneNumberEt;
    private EditText verificationCodeEt;
    private final RetrievePasswordActivity fatherActivity;

    public VerificationFragment(RetrievePasswordActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        // 绑定所有控件
        phoneNumberEt = view.findViewById(R.id.retrieve_account);
        verificationCodeEt = view.findViewById(R.id.retrieve_verification);
        Button nextStepBtn = view.findViewById(R.id.next_step_button);
        Button getVerificationBtn = view.findViewById(R.id.get_verification_button);

        // 设置按钮点击监听器
        nextStepBtn.setOnClickListener(this::onClickListenerCallback);
        getVerificationBtn.setOnClickListener(view1 -> getVerificationCode());
        return view;
    }

    /**
     * @param view Do NOT use the param
     */
    private void onClickListenerCallback(View view) {
        if (Server.callAccountVerification(
                phoneNumberEt.getText().toString(),
                verificationCodeEt.getText().toString())) {
            // 成功则转到密码重置
            fatherActivity.showResetFragment();
        } else {
            // 失败则弹出提示
            AlertDialog.Builder builder = new AlertDialog.Builder(fatherActivity);
            builder.setMessage("验证码错误！").
                    setCancelable(false).
                    // 关闭弹窗
                            setPositiveButton("确认", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        }
    }

    /**
     * 获取验证码
     */
    private void getVerificationCode() {
        //TODO: 做一个90s之后重新发送的东东
        Server.callVerificationSending(phoneNumberEt.getText().toString());
    }
}