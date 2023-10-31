package top.niunaijun.blackbox.utils;

import android.os.Bundle;

public class BundleUtils {

    static public String description(Bundle bundle) {
        if (bundle == null) {
            return "";
        }
        StringBuilder v = new StringBuilder("{\n");
        for (String key: bundle.keySet())
        {
            v.append("  ").append(key).append(":").append(bundle.getString(key)).append(",").append("\n");
        }
        v.append("}");
        return v.toString();
    }
}
