package edu.bupt.ticketextraction.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.export.SendToEmailActivity;
import edu.bupt.ticketextraction.main.MainActivity;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/08/24
 *     desc   : 导出fragment
 *     version: 0.0.1
 * </pre>
 */
public final class ExportFragment extends Fragment {
    private final MainActivity fatherActivity;

    public ExportFragment(MainActivity fatherActivity) {
        this.fatherActivity = fatherActivity;
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_export, container, false);
        // 绑定默认check box
        CheckBox defaultCheckBox = view.findViewById(R.id.default_wallet_check_box);
        // checkBoxes用于发送email时确定哪些需要被发送
        SendToEmailActivity.checkBoxes.add(defaultCheckBox);

        // 发送邮件时发送选定钱包对应的表格
        Button sendToEmailBtn = view.findViewById(R.id.send_to_email_btn);
        sendToEmailBtn.setOnClickListener(view1 -> fatherActivity.jumpFromMainToEmail());
        return view;
    }
}
