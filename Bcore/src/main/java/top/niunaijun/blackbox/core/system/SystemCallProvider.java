package top.niunaijun.blackbox.core.system;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import top.niunaijun.blackbox.utils.compat.BundleCompat;

/**
 * Created by Milk on 3/31/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class SystemCallProvider extends ContentProvider {
    public static final String TAG = "iOS";

    @Override
    public boolean onCreate() {
        BlackBoxSystem.getSystem().startup();
        Log.i(TAG, "SystemCallProvider start up success");
        return true;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Log.d(TAG, "call: " + method + ", " + extras);
        if ("VM".equals(method)) {
            Bundle bundle = new Bundle();
            if (extras != null) {
                String name = extras.getString("_B_|_server_name_");
                BundleCompat.putBinder(bundle, "_B_|_server_", ServiceManager.getService(name));
            }
            return bundle;
        }
        return super.call(method, arg, extras);
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
