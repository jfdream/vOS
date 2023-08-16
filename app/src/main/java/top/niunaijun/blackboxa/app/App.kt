package top.niunaijun.blackboxa.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import top.niunaijun.blackbox.app.BActivityThread

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 21:21
 */
class App : Application() {

    private val TAG = "BlackBoxCore";

    companion object {

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private lateinit var mContext: Context

        @JvmStatic
        fun getContext(): Context {
            return mContext
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        mContext = base!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.i(TAG, "onCreate attachBaseContext:${packageName} processName:${getProcessName()}");
        }
        AppManager.doAttachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppManager.doOnCreate(mContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.i(TAG, "onCreate packageName:${packageName} processName:${getProcessName()}");
        }
    }
}