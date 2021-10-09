package edu.bupt.ticketextraction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.R;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/09
 *     desc   : 展示个人信息的Fragment
 *     version: 0.0.1
 * </pre>
 */
public class PersonInfoActivity extends AppCompatActivity {
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

    // 通过该回调函数监听返回键是否被点击
    // 被点击则结束此activity并返回main activity
    // 等号右侧必须是android.R.id.home
    // R.id.home会出现bug，可以运行但与getItemId()不相等
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            ActivityStack.getInstance().popActivity();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        // 将PersonInfoActivity压入栈中
        ActivityStack.getInstance().pushActivity(this);

        // 创建顶部导航栏
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 设置标题
            actionBar.setTitle("个人信息");
        }

        // 绑定控件
        Button myContactsBtn = findViewById(R.id.my_contacts_button);
        Button changePasswordBtn = findViewById(R.id.change_password_button);
        Button changeAccountBtn = findViewById(R.id.change_account_button);
        Button logoffBtn = findViewById(R.id.logoff_button);
        Button exitBtn = findViewById(R.id.exit_button);
        // 我的联系人不需要点击，只是为了样式一样设置为按钮
        myContactsBtn.setClickable(false);
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
