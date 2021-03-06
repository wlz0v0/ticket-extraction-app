package edu.bupt.ticketextraction.setting.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.setting.PersonInfoActivity;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/12
 *     desc   : 在个人信息中展示联系人信息并能够跳转到ContactActivity
 *     version: 0.0.1
 * </pre>
 */
public final class ContactFragment extends Fragment {
    // 父Activity
    private final PersonInfoActivity fatherActivity;
    // 联系人姓名
    private final Contact contact;

    public ContactFragment(PersonInfoActivity fatherActivity, @NotNull Contact contact) {
        this.fatherActivity = fatherActivity;
        this.contact = contact;
    }

    @Override
    public @NotNull View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                                      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                                      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        Button contactBtn = view.findViewById(R.id.contact_button);
        // 设置联系人姓名
        contactBtn.setText(contact.getName());
        // 设置点击跳转
        contactBtn.setOnClickListener(view1 -> fatherActivity.jumpFromPersonInfoToContact(contact));
        return view;
    }
}
