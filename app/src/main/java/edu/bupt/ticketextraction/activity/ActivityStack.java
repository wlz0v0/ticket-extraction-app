package edu.bupt.ticketextraction.activity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

/**
 * <pre>
 *     author : 武连增
 *     e-mail : wulianzeng@bupt.edu.cn
 *     time   : 2021/10/09
 *     desc   : Activity栈，用于一键退出程序
 *     version: 0.0.1
 * </pre>
 */
public enum ActivityStack {
    INSTANCE;

    /**
     * Activity栈
     */
    private final Stack<AppCompatActivity> activityStack = new Stack<>();

    /**
     * @return ActivityStack实例
     */
    public static ActivityStack getInstance() {
        return INSTANCE;
    }

    /**
     * 将Activity压入栈中
     *
     * @param activity 要压入的Activity
     */
    public void pushActivity(AppCompatActivity activity) {
        activityStack.push(activity);
    }

    /**
     * 弹出栈顶Activity
     */
    public void popActivity() {
        activityStack.pop();
    }

    /**
     * 结束所有Activity，即退出程序
     */
    public void finishAllActivities() {
        while (!activityStack.empty()) {
            activityStack.peek().finish();
            activityStack.pop();
        }
    }
}
