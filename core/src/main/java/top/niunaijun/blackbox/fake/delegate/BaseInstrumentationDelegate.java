package top.niunaijun.blackbox.fake.delegate;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.app.Instrumentation;
import android.app.UiAutomation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.os.UserHandle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import top.niunaijun.blackbox.BBCore;
import top.niunaijun.blackbox.app.BActivityThread;
import top.niunaijun.blackbox.app.configuration.AppLifecycleCallback;
import top.niunaijun.blackbox.fake.provider.FileProviderHandler;
import top.niunaijun.blackbox.utils.Reflector;

public class BaseInstrumentationDelegate extends Instrumentation {

    private static final String TAG = "BaseInstrumentationDelegate.iOS";
    protected Instrumentation mBaseInstrumentation;


    @Override
    public void onCreate(Bundle arguments) {
        mBaseInstrumentation.onCreate(arguments);
    }

    @Override
    public void start() {
        mBaseInstrumentation.start();
    }

    @Override
    public void onStart() {
        mBaseInstrumentation.onStart();
    }

    @Override
    public boolean onException(Object obj, Throwable e) {
        Log.e(TAG, "onException:" + e);
        return false;
    }

    @Override
    public void sendStatus(int resultCode, Bundle results) {
        mBaseInstrumentation.sendStatus(resultCode, results);
    }

    @Override
    public void addResults(Bundle results) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBaseInstrumentation.addResults(results);
        }
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        mBaseInstrumentation.finish(resultCode, results);
    }

    @Override
    public void setAutomaticPerformanceSnapshots() {
        mBaseInstrumentation.setAutomaticPerformanceSnapshots();
    }

    @Override
    public void startPerformanceSnapshot() {
        mBaseInstrumentation.startPerformanceSnapshot();
    }

    @Override
    public void endPerformanceSnapshot() {
        mBaseInstrumentation.endPerformanceSnapshot();
    }

    @Override
    public void onDestroy() {
        mBaseInstrumentation.onDestroy();
    }

    @Override
    public Context getContext() {
        return mBaseInstrumentation.getContext();
    }

    @Override
    public ComponentName getComponentName() {
        return mBaseInstrumentation.getComponentName();
    }

    @Override
    public Context getTargetContext() {
        return mBaseInstrumentation.getTargetContext();
    }

    @Override
    public boolean isProfiling() {
        return mBaseInstrumentation.isProfiling();
    }

    @Override
    public void startProfiling() {
        mBaseInstrumentation.startProfiling();
    }

    @Override
    public void stopProfiling() {
        mBaseInstrumentation.stopProfiling();
    }

    @Override
    public void setInTouchMode(boolean inTouch) {
        mBaseInstrumentation.setInTouchMode(inTouch);
    }

    @Override
    public void waitForIdle(Runnable recipient) {
        mBaseInstrumentation.waitForIdle(recipient);
    }

    @Override
    public void waitForIdleSync() {
        mBaseInstrumentation.waitForIdleSync();
    }

    @Override
    public void runOnMainSync(Runnable runner) {
        mBaseInstrumentation.runOnMainSync(runner);
    }

    @Override
    public Activity startActivitySync(Intent intent) {
        return mBaseInstrumentation.startActivitySync(intent);
    }

    @Override
    public void addMonitor(ActivityMonitor monitor) {
        mBaseInstrumentation.addMonitor(monitor);
    }

    @Override
    public ActivityMonitor addMonitor(IntentFilter filter, ActivityResult result, boolean block) {
        return mBaseInstrumentation.addMonitor(filter, result, block);
    }

    @Override
    public ActivityMonitor addMonitor(String cls, ActivityResult result, boolean block) {
        return mBaseInstrumentation.addMonitor(cls, result, block);
    }

    @Override
    public boolean checkMonitorHit(ActivityMonitor monitor, int minHits) {
        return mBaseInstrumentation.checkMonitorHit(monitor, minHits);
    }

    @Override
    public Activity waitForMonitor(ActivityMonitor monitor) {
        return mBaseInstrumentation.waitForMonitor(monitor);
    }

    @Override
    public Activity waitForMonitorWithTimeout(ActivityMonitor monitor, long timeOut) {
        return mBaseInstrumentation.waitForMonitorWithTimeout(monitor, timeOut);
    }

    @Override
    public void removeMonitor(ActivityMonitor monitor) {
        mBaseInstrumentation.removeMonitor(monitor);
    }

    @Override
    public boolean invokeMenuActionSync(Activity targetActivity, int id, int flag) {
        return mBaseInstrumentation.invokeMenuActionSync(targetActivity, id, flag);
    }

    @Override
    public boolean invokeContextMenuAction(Activity targetActivity, int id, int flag) {
        return mBaseInstrumentation.invokeContextMenuAction(targetActivity, id, flag);
    }

    @Override
    public void sendStringSync(String text) {
        mBaseInstrumentation.sendStringSync(text);
    }

    @Override
    public void sendKeySync(KeyEvent event) {
        mBaseInstrumentation.sendKeySync(event);
    }

    @Override
    public void sendKeyDownUpSync(int key) {
        mBaseInstrumentation.sendKeyDownUpSync(key);
    }

    @Override
    public void sendCharacterSync(int keyCode) {
        mBaseInstrumentation.sendCharacterSync(keyCode);
    }

    @Override
    public void sendPointerSync(MotionEvent event) {
        mBaseInstrumentation.sendPointerSync(event);
    }

    @Override
    public void sendTrackballEventSync(MotionEvent event) {
        mBaseInstrumentation.sendTrackballEventSync(event);
    }

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Application application = mBaseInstrumentation.newApplication(cl, className, context);
        BActivityThread.setApplication(application);
        return application;
    }

    @Override
    public void callApplicationOnCreate(Application app) {
        mBaseInstrumentation.callApplicationOnCreate(app);
    }

    @Override
    public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws IllegalAccessException, InstantiationException {
        return mBaseInstrumentation.newActivity(clazz, context, token, application, intent, info, title, parent, id, lastNonConfigurationInstance);
    }

    @Override
    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        return mBaseInstrumentation.newActivity(cl, className, intent);
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        mBaseInstrumentation.callActivityOnCreate(activity, icicle);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivityCreated(activity, icicle);
        }
    }

    @Override
    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        mBaseInstrumentation.callActivityOnCreate(activity, icicle, persistentState);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivityCreated(activity, icicle);
        }
    }

    @Override
    public void callActivityOnDestroy(Activity activity) {
        mBaseInstrumentation.callActivityOnDestroy(activity);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivityDestroyed(activity);
        }
    }

    @Override
    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        mBaseInstrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState);
    }

    @Override
    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
        mBaseInstrumentation.callActivityOnRestoreInstanceState(activity, savedInstanceState, persistentState);
    }

    @Override
    public void callActivityOnPostCreate(Activity activity, Bundle icicle) {
        mBaseInstrumentation.callActivityOnPostCreate(activity, icicle);
    }

    @Override
    public void callActivityOnPostCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        mBaseInstrumentation.callActivityOnPostCreate(activity, icicle, persistentState);
    }

    @Override
    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        mBaseInstrumentation.callActivityOnNewIntent(activity, intent);
    }

    @Override
    public void callActivityOnStart(Activity activity) {
        mBaseInstrumentation.callActivityOnStart(activity);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivityStarted(activity);
        }
    }

    @Override
    public void callActivityOnRestart(Activity activity) {
        mBaseInstrumentation.callActivityOnRestart(activity);
    }

    @Override
    public void callActivityOnResume(Activity activity) {
        mBaseInstrumentation.callActivityOnResume(activity);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivityResumed(activity);
        }
    }

    @Override
    public void callActivityOnStop(Activity activity) {
        mBaseInstrumentation.callActivityOnStop(activity);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivityStopped(activity);
        }
    }

    @Override
    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
        mBaseInstrumentation.callActivityOnSaveInstanceState(activity, outState);
    }

    @Override
    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState, PersistableBundle outPersistentState) {
        mBaseInstrumentation.callActivityOnSaveInstanceState(activity, outState, outPersistentState);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void callActivityOnPause(Activity activity) {
        mBaseInstrumentation.callActivityOnPause(activity);
        for (AppLifecycleCallback appLifecycleCallback : BBCore.get().getAppLifecycleCallbacks()) {
            appLifecycleCallback.onActivityPaused(activity);
        }
    }

    @Override
    public void callActivityOnUserLeaving(Activity activity) {
        mBaseInstrumentation.callActivityOnUserLeaving(activity);
    }

    @Override
    public void startAllocCounting() {
        mBaseInstrumentation.startAllocCounting();
    }

    @Override
    public void stopAllocCounting() {
        mBaseInstrumentation.stopAllocCounting();
    }

    @Override
    public Bundle getAllocCounts() {
        return mBaseInstrumentation.getAllocCounts();
    }

    @Override
    public Bundle getBinderCounts() {
        return mBaseInstrumentation.getBinderCounts();
    }

    @Override
    public UiAutomation getUiAutomation() {
        return mBaseInstrumentation.getUiAutomation();
    }

    /**
     *
     * @param who The Context from which the activity is being started.
     * @param contextThread The main thread of the Context from which the activity is being started.
     * @param token Internal token identifying to the system who is starting the activity; may be null.
     * @param activity Which activity is performing the start (and thus receiving any result); may be null if this call is not being made from an activity.
     * @param intent The actual Intent to start.
     * @param requestCode Identifier for this request's result; less than zero if the caller is not expecting a result.
     * @param options Addition options.
     * @return
     * @throws Throwable
     */
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity activity, Intent intent, int requestCode, Bundle options) throws Throwable {
        Reflector reflector = invokeExecStartActivity(mBaseInstrumentation,
                Context.class,
                IBinder.class,
                IBinder.class,
                Activity.class,
                Intent.class,
                Integer.TYPE,
                Bundle.class);

        // 可能会报这个错误导致崩溃，这个是由找不到 FileProvider 导致的，不同的 App 配置的文件提供这不一样，此处需要单独配置一个文件提供者
//        Caused by: android.content.ActivityNotFoundException: No Activity found to handle Intent { act=android.intent.action.VIEW
//            dat=content://com.android.example.camera2.video.provider/files/VID_2023_08_24_14_16_56_812.mp4 flg=0x4000001 }
//            at android.app.Instrumentation.checkStartActivityResult(Instrumentation.java:2076)
//        try {
        Log.i(TAG, "execStartActivity:" + activity + " intent:" + intent + " extra:" + intent.getExtras() + " options:" + options);
        Uri uri = intent.getData();
        if (uri != null) {
            intent.setData(FileProviderHandler.convertFileUri(BActivityThread.getApplication(), uri));
        }
        return reflector.callByCaller(mBaseInstrumentation, new Object[]{who, contextThread, token, activity, intent, requestCode, options});
    }

    public ActivityResult execStartActivity(Context context, IBinder contextThread, IBinder token, String str, Intent intent, int i, Bundle bundle) throws Throwable {
        return invokeExecStartActivity(mBaseInstrumentation,
                Context.class,
                IBinder.class,
                IBinder.class,
                String.class,
                Intent.class,
                Integer.TYPE,
                Bundle.class).callByCaller(mBaseInstrumentation, new Object[]{context, contextThread, token, str, intent, i, bundle});
    }

    public ActivityResult execStartActivity(Context context, IBinder contextThread, IBinder token, Fragment fragment, Intent intent, int i) throws Throwable {
        return invokeExecStartActivity(mBaseInstrumentation,
                Context.class,
                IBinder.class,
                IBinder.class,
                Fragment.class,
                Intent.class,
                Integer.TYPE).callByCaller(mBaseInstrumentation, new Object[]{context, contextThread, token, fragment, intent, i});
    }

    public ActivityResult execStartActivity(Context context, IBinder contextThread, IBinder token, Activity activity, Intent intent, int i) throws Throwable {
        return invokeExecStartActivity(mBaseInstrumentation,
                Context.class,
                IBinder.class,
                IBinder.class,
                Activity.class,
                Intent.class,
                Integer.TYPE).callByCaller(mBaseInstrumentation, new Object[]{context, contextThread, token, activity, intent, i});
    }

    public ActivityResult execStartActivity(Context context, IBinder contextThread, IBinder token, Fragment fragment, Intent intent, int i, Bundle bundle) throws Throwable {
        return invokeExecStartActivity(mBaseInstrumentation,
                Context.class,
                IBinder.class,
                IBinder.class,
                Fragment.class,
                Intent.class,
                Integer.TYPE,
                Bundle.class).callByCaller(mBaseInstrumentation, new Object[]{context, contextThread, token, fragment, intent, i, bundle});
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public ActivityResult execStartActivity(Context context, IBinder contextThread, IBinder token, Activity activity, Intent intent, int i, Bundle bundle, UserHandle userHandle) throws Throwable {
        return invokeExecStartActivity(mBaseInstrumentation,
                Context.class,
                IBinder.class,
                IBinder.class,
                Activity.class,
                Intent.class,
                Integer.TYPE,
                Bundle.class,
                UserHandle.class).callByCaller(mBaseInstrumentation, new Object[]{context, contextThread, token, activity, intent, i, bundle, userHandle});
    }

    private static Reflector invokeExecStartActivity(Object obj, Class<?>... args) throws NoSuchMethodException {
        Class<?> cls = obj.getClass();
        while (cls != null) {
            try {
                return Reflector.on(obj.getClass()).method("execStartActivity", args);
            } catch (Exception e) {
                cls = cls.getSuperclass();
            }
        }
        throw new NoSuchMethodException();
    }
}
