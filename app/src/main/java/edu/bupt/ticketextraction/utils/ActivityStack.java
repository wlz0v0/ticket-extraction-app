package edu.bupt.ticketextraction.utils;

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
public final class ActivityStack {
    /**
     * Activity栈
     */
    private final Stack<AppCompatActivity> activityStack = new Stack<>();

    /**
     * 单例模式，构造器私有
     */
    private ActivityStack() {
    }

    /**
     * @return ActivityStack实例
     */
    public static ActivityStack getInstance() {
        return InstanceHolder.INSTANCE;
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
            this.popActivity();
        }
    }

    /**
     * 结束栈顶Activity
     */
    public void finishActivity() {
        activityStack.peek().finish();
        this.popActivity();
    }

    /**
     * 私有静态内部类，负责实例化单例
     */
    private static class InstanceHolder {
        private final static ActivityStack INSTANCE = new ActivityStack();
    }
}
