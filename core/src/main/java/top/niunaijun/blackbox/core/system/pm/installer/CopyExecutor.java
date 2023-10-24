package top.niunaijun.blackbox.core.system.pm.installer;


import android.util.Log;

import java.io.File;
import java.io.IOException;

import top.niunaijun.blackbox.core.env.BEnvironment;
import top.niunaijun.blackbox.core.system.pm.BPackageSettings;
import top.niunaijun.blackbox.entity.pm.InstallOption;
import top.niunaijun.blackbox.utils.FileUtils;
import top.niunaijun.blackbox.utils.NativeUtils;

/**
 * Created by Milk on 4/24/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 * 拷贝文件相关
 */
public class CopyExecutor implements Executor {
    boolean useSystemApk;

    public CopyExecutor(boolean useSystemApk){
        this.useSystemApk = useSystemApk;
    }

    @Override
    public int exec(BPackageSettings ps, InstallOption option, int userId) {
        try {
            if (!option.isFlag(InstallOption.FLAG_SYSTEM) || !useSystemApk) {
                NativeUtils.copyNativeLib(new File(ps.pkg.baseCodePath), BEnvironment.getAppLibDir(ps.pkg.packageName));
            }
        } catch (Exception e) {
            Log.i(TAG, "copyNativeLib exception:" + e);
            return -1;
        }
        if (option.isFlag(InstallOption.FLAG_STORAGE) || !useSystemApk) {
            // 外部安装
            File origFile = new File(ps.pkg.baseCodePath);
            File newFile = BEnvironment.getBaseApkDir(ps.pkg.packageName);
            try {
                if (option.isFlag(InstallOption.FLAG_URI_FILE)) {
                    boolean b = FileUtils.renameTo(origFile, newFile);
                    if (!b) {
                        FileUtils.copyFile(origFile, newFile);
                    }
                } else {
                    FileUtils.copyFile(origFile, newFile);
                }
                // update baseCodePath for external usage
                ps.pkg.baseCodePath = newFile.getAbsolutePath();
            } catch (IOException e) {
                Log.i(TAG, "copyFile exception:" + e);
                return -1;
            }
        } else if (option.isFlag(InstallOption.FLAG_SYSTEM)) {
            // 系统安装不需要拷贝任何数据，直接使用系统 packageInfo 即可启动 App
            Log.i(TAG, "system install auto success");
        }
        return 0;
    }
}
