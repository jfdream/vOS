package top.niunaijun.blackbox.core;

import android.util.Log;

import top.niunaijun.blackbox.BBCore;

/**
 * Created by Milk on 4/30/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler.iOS";
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static void create() {
        new CrashHandler();
    }

    public CrashHandler() {
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (BBCore.get().getExceptionHandler() != null) {
            BBCore.get().getExceptionHandler().uncaughtException(t, e);
        }
        mDefaultHandler.uncaughtException(t, e);
        Log.i(TAG, "uncaughtException:" + e);
    }
}
