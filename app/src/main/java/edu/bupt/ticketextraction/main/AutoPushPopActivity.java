package edu.bupt.ticketextraction.main;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import edu.bupt.ticketextraction.utils.ActivityStack;
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

    /**
     * 本函数用于设置Activity的返回键和标题
     *
     * @param activity 要设置的Activity，在相应的Activity中传this就行
     * @param title    Activity标题
     */
    protected void setActionBar(@NotNull AutoPushPopActivity activity, String title) {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            // 设置标题
            actionBar.setTitle(title);
            // 设置返回键
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 展示Toast
     *
     * @param activity 要展示Toast的Activity
     * @param message  Toast要展示的消息
     * @param time     Toast要展示的时间，单位秒
     */
    public void showBottomToast(AutoPushPopActivity activity, String message, int time) {
        Toast toast = Toast.makeText(activity, message, time);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
    }

    /**
     * 展示AlertDialog
     *
     * @param text            要展示的文本
     * @param onClickListener PositiveButton的回调函数
     * @return 对应AlertDialog
     */
    public @NotNull AlertDialog getAlertDialog(String text,
                                               DialogInterface.OnClickListener onClickListener) {
        // 先创建一个builder，再通过builder构造alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(text).
                setCancelable(false).
                setPositiveButton("确认", onClickListener);
        return builder.create();
    }
}
