package edu.bupt.ticketextraction.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.NotNull;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/10
 *     desc   : 自动进出ActivityStack的Activity，被所有自定义Activity继承
 *              在onCreate时压入栈中，在finish时弹出
 *     version: 0.0.1
 * </pre>
 */
public abstract class AutoPushPopActivity extends AppCompatActivity {
    /**
     * 通过该回调函数监听返回键是否被点击 <br>
     * 被点击则结束此Activity、出栈并返回上一级Activity <br>
     * 等号右侧必须是android.R.id.home <br>
     * R.id.home会出现bug，可以运行但与getItemId()不相等 <br>
     */
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // 结束栈顶Activity
            ActivityStack.getInstance().finishActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 处理返回按键事件<br>
     * 点击按键结束当前Activity并出栈
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 结束栈顶Activity
            ActivityStack.getInstance().finishActivity();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在onCreate时将自身压入栈中
     */
    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将自身压入栈中
        ActivityStack.getInstance().pushActivity(this);
    }
}
