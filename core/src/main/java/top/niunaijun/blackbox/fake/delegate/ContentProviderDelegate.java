package top.niunaijun.blackbox.fake.delegate;

import android.net.Uri;
import android.os.Build;
import android.os.IInterface;
import android.util.Log;

import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import black.android.app.BRActivityThread;
import black.android.app.BRActivityThreadProviderClientRecordP;
import black.android.app.BRIActivityManagerContentProviderHolder;
import black.android.content.BRContentProviderHolderOreo;
import black.android.providers.BRSettingsContentProviderHolder;
import black.android.providers.BRSettingsGlobal;
import black.android.providers.BRSettingsNameValueCache;
import black.android.providers.BRSettingsNameValueCacheOreo;
import black.android.providers.BRSettingsSecure;
import black.android.providers.BRSettingsSystem;
import top.niunaijun.blackbox.BBCore;
import top.niunaijun.blackbox.fake.service.context.providers.ContentProviderStub;
import top.niunaijun.blackbox.fake.service.context.providers.SettingsProviderStub;
import top.niunaijun.blackbox.utils.StringUtils;
import top.niunaijun.blackbox.utils.compat.BuildCompat;

/**
 * Created by Milk on 3/31/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class ContentProviderDelegate {
    public static final String TAG = "ContentProviderDelegate.iOS";
    private static final Set<String> sInjected = new HashSet<>();

    public static void update(Object holder, String auth) {
        IInterface iInterface;
        if (BuildCompat.isOreo()) {
            iInterface = BRContentProviderHolderOreo.get(holder).provider();
        } else {
            iInterface = BRIActivityManagerContentProviderHolder.get(holder).provider();
        }

        if (iInterface instanceof Proxy)
            return;
        IInterface bContentProvider;
        if (auth.equals("settings")) {
            bContentProvider = new SettingsProviderStub().wrapper(iInterface, BBCore.getHostPkg());
        } else {
            bContentProvider = new ContentProviderStub().wrapper(iInterface, BBCore.getHostPkg());
        }
        if (BuildCompat.isOreo()) {
            BRContentProviderHolderOreo.get(holder)._set_provider(bContentProvider);
        } else {
            BRIActivityManagerContentProviderHolder.get(holder)._set_provider(bContentProvider);
        }
    }

    public static void init() {
        clearSettingProvider();
        BBCore.getContext().getContentResolver().call(Uri.parse("content://settings"), "", null, null);
        Object activityThread = BBCore.mainThread();
        Map<?, ?> map = BRActivityThread.get(activityThread).mProviderMap();
        for (Object value : map.values()) {
            String[] providerNames = BRActivityThreadProviderClientRecordP.get(value).mNames();
            if (StringUtils.isEmpty(providerNames)) {
                continue;
            }
            String hostPkg = BBCore.getHostPkg();
            for (String providerName : providerNames) {
                if (!sInjected.contains(providerName)) {
                    sInjected.add(providerName);
                    Log.i(TAG, "providerName:" + providerName + " hostPkg:" + hostPkg);
                    final IInterface iInterface = BRActivityThreadProviderClientRecordP.get(value).mProvider();
                    BRActivityThreadProviderClientRecordP.get(value)._set_mProvider(new ContentProviderStub().wrapper(iInterface, hostPkg));
                    BRActivityThreadProviderClientRecordP.get(value)._set_mNames(new String[]{providerName});
                }
            }
        }
    }

    public static void clearSettingProvider() {
        Object cache;
        cache = BRSettingsSystem.get().sNameValueCache();
        if (cache != null) {
            clearContentProvider(cache);
        }
        cache = BRSettingsSecure.get().sNameValueCache();
        if (cache != null) {
            clearContentProvider(cache);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && BRSettingsGlobal.getRealClass() != null) {
            cache = BRSettingsGlobal.get().sNameValueCache();
            if (cache != null) {
                clearContentProvider(cache);
            }
        }
    }

    private static void clearContentProvider(Object cache) {
        if (BuildCompat.isOreo()) {
            Object holder = BRSettingsNameValueCacheOreo.get(cache).mProviderHolder();
            if (holder != null) {
                BRSettingsContentProviderHolder.get(holder)._set_mContentProvider(null);
            }
        } else {
            BRSettingsNameValueCache.get(cache)._set_mContentProvider(null);
        }
    }
}
