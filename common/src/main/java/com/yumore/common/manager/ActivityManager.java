package com.yumore.common.manager;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

/**
 * @author nathaniel
 */
public class ActivityManager {
    private static final String TAG = ActivityManager.class.getSimpleName();
    private static Stack<AppCompatActivity> activityStack;
    private static ActivityManager instance;

    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static synchronized ActivityManager getInstance() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public synchronized void addActivity(AppCompatActivity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
        Log.i(TAG, "ActivityManager添加了：" + activity.getClass().getName());
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public AppCompatActivity currentActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        finishActivity(activityStack.lastElement());
    }

    /**
     * 移除最后一个Activity
     */
    public void removeActivity(AppCompatActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            Log.i("TAG", "ActivityManager移除了：" + activity.getClass().getName());
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(AppCompatActivity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            Log.i("TAG", "ActivityManager关闭了：" + activity.getClass().getName());
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i));
                removeActivity(activityStack.get(i));
                return;
            }
        }
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (AppCompatActivity activity : activityStack) {
            if (activity != null) {
                activity.finish();
            }
        }
        activityStack.clear();
    }


    /**
     * 退出应用程序
     */
    public void exitApplication(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
