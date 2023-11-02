package top.niunaijun.blackboxa.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import com.umeng.commonsdk.UMConfigure

/**
 *
 * @Description:
 * @Author: wukaicheng
 * @CreateDate: 2021/4/29 21:21
 */
class App : Application() {

    private val TAG = "App";

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
            Log.i(TAG, "onCreate attachBaseContext:${packageName} processName:${getProcessName()}, app:${this}");
        }
        AppManager.doAttachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        AppManager.doOnCreate(mContext)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Log.i(TAG, "onCreate packageName:${packageName} processName:${getProcessName()}, app:${this}");
        }
        initUMeng();
    }


    private fun initUMeng(){
        UMConfigure.setLogEnabled(true)
        //添加注释
        UMConfigure.init(this, "65436e9858a9eb5b0afe5200", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "")
    }
}