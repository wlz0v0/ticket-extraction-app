package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.data.Contact;
import edu.bupt.ticketextraction.fragment.ContactFragment;
import edu.bupt.ticketextraction.utils.ActivityStack;

import java.util.ArrayList;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/09
 *     desc   : 展示个人信息的Fragment
 *     version: 0.0.1
 * </pre>
 */
public final class PersonInfoActivity extends AutoPushPopActivity {
    private final ArrayList<ContactFragment> fragments = new ArrayList<>();

    /**
     * 跳转到ContactActivity
     *
     * @param contact 联系人
     */
    public void jumpFromPersonInfoToContact(Contact contact) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(ContactActivity.CONTACT, contact);
        startActivity(intent);
    }

    /**
     * 跳转到ChangePasswordActivity
     */
    public void jumpFromPersonInfoToChangePassword() {
        Intent intent = new Intent(this, SetPasswordActivity.class);
        // 设置找回密码的文本内容
        // 通过putExtra传递变量
        intent.putExtra(SetPasswordActivity.TITLE_EXTRA, SetPasswordActivity.Titles.CHANGE);
        intent.putExtra(SetPasswordActivity.BUTTON_TEXT_EXTRA, SetPasswordActivity.ButtonTexts.CHANGE);
        startActivity(intent);
    }

    /**
     * 跳转到LoginActivity
     */
    public void jumpFromPersonInfoToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // 创建顶部导航栏
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 设置标题
            actionBar.setTitle("个人信息");
        }

        //TODO 创建ContactFragment
        ContactFragment fragment = new ContactFragment(this, new Contact("wlz", "1228393790@qq.com"));
        fragments.add(fragment);

        // 添加联系人
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (Fragment fg : fragments) {
            transaction.add(R.id.contact_fragment_container, fg);
        }
        transaction.commitAllowingStateLoss();

        // 绑定控件
        Button changePasswordBtn = findViewById(R.id.change_password_button);
        Button changeAccountBtn = findViewById(R.id.change_account_button);
        Button logoffBtn = findViewById(R.id.logoff_button);
        Button exitBtn = findViewById(R.id.exit_button);
        // 设置点击监听器
        changePasswordBtn.setOnClickListener(view1 -> jumpFromPersonInfoToChangePassword());
        changeAccountBtn.setOnClickListener(this::changeAccountOnClickListenerCallback);
        logoffBtn.setOnClickListener(view1 -> jumpFromPersonInfoToLogin());
        exitBtn.setOnClickListener(view1 -> ActivityStack.getInstance().finishAllActivities());
    }

    private void changeAccountOnClickListenerCallback(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
