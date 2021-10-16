package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import edu.bupt.ticketextraction.R;
import edu.bupt.ticketextraction.data.Contact;
import edu.bupt.ticketextraction.fragment.ContactFragment;
import edu.bupt.ticketextraction.utils.ActivityStack;

import java.util.LinkedHashMap;
import java.util.Map;

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
    private final LinkedHashMap<ContactFragment, Boolean> fragments = new LinkedHashMap<>();
    // 从Extra获取Contact时对应的Extra名
    public final static String CONTACT = "contact";
    // 从Extra获取邮箱时对应的Extra名
    public final static String NEW_EMAIL = "new email";
    // 从Extra获取姓名时对应的Extra名
    public final static String NEW_NAME = "new name";

    /**
     * 启动CreateContactActivity，并获取其返回的name和email<br>
     * 这个Launcher必须要定义在字段中
     */
    private final ActivityResultLauncher<Intent> createContactLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent intent = result.getData();
                // 当用户不点击按钮而是直接返回的情况下，intent为null
                if (intent != null) {
                    String name = intent.getStringExtra(NEW_NAME);
                    String email = intent.getStringExtra(NEW_EMAIL);
                    ContactFragment fragment = new ContactFragment(this, new Contact(name, email));
                    fragments.put(fragment, false);
                }
            });

    /**
     * 跳转到CreateContactActivity
     */
    public void jumpFromPersonInfoToCreateContact() {
        // 跳转前设置相应文本内容
        CreateContactActivity.title = "添加联系人";
        CreateContactActivity.buttonText = "添加";
        createContactLauncher.launch(new Intent(this, CreateContactActivity.class));
    }

    /**
     * 跳转到ContactActivity
     *
     * @param contact 联系人
     */
    public void jumpFromPersonInfoToContact(Contact contact) {
        Intent intent = new Intent(this, ContactActivity.class);
        intent.putExtra(CONTACT, contact);
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

        // 设置ActionBar
        super.setActionBar(this, "个人信息");

        // 绑定控件
        // 添加联系人
        Button addContactBtn = findViewById(R.id.add_contact_button);
        // 修改密码
        Button changePasswordBtn = findViewById(R.id.change_password_button);
        // 切换账号
        Button changeAccountBtn = findViewById(R.id.change_account_button);
        // 注销
        Button logoffBtn = findViewById(R.id.logoff_button);
        // 退出
        Button exitBtn = findViewById(R.id.exit_button);
        // 设置点击监听器
        addContactBtn.setOnClickListener(view1 -> jumpFromPersonInfoToCreateContact());
        changePasswordBtn.setOnClickListener(view1 -> jumpFromPersonInfoToChangePassword());
        changeAccountBtn.setOnClickListener(this::changeAccountOnClickListenerCallback);
        logoffBtn.setOnClickListener(view1 -> jumpFromPersonInfoToLogin());
        exitBtn.setOnClickListener(view1 -> ActivityStack.getInstance().finishAllActivities());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 展示所有联系人Fragment
        showFragments();
    }

    private void changeAccountOnClickListenerCallback(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 展示所有联系人Fragment
     */
    private void showFragments() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        for (Map.Entry<ContactFragment, Boolean> entry : fragments.entrySet()) {
            // 若未添加则添加
            if (!entry.getValue()) {
                fragmentTransaction.add(R.id.contact_fragment_container, entry.getKey());
                entry.setValue(true);
            } else {
                // 已添加则展示
                fragmentTransaction.show(entry.getKey());
            }
        }
        fragmentTransaction.commitNowAllowingStateLoss();
    }
}
