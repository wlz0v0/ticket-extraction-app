package edu.bupt.ticketextraction.fragment;

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
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_verification, container, false);
        // 绑定所有控件
        EditText phoneNumberEt = view.findViewById(R.id.retrieve_account);
        EditText verificationCodeEt = view.findViewById(R.id.retrieve_verification);
        Button nextStepBtn = view.findViewById(R.id.next_step_button);
        Button getVerificationBtn = view.findViewById(R.id.get_verification_button);
        return view;
    }
}
