package android.compact.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

public class IntentCompactUtil {

    public static boolean checkIntentHasHandle(Context ctx, Intent intent) {
        try {
            boolean hasHandle = true;
            if (ctx.getPackageManager().resolveActivity(intent,
                    PackageManager.MATCH_DEFAULT_ONLY) == null) {
                if (ctx.getPackageManager().resolveService(intent,
                        PackageManager.MATCH_DEFAULT_ONLY) == null) {
                    hasHandle = false;
                }
            }
            return hasHandle;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String convertIntentToString(Intent intent) {
        try {
            return intent.toUri(Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Intent convertStringToIntent(String string) {
        try {
            return Intent.parseUri(string, Intent.URI_INTENT_SCHEME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getSegment(Intent intent, String key) {
        String extraString = null;
        if (intent != null) {
            extraString = getSegment(intent.getExtras(), key);
            if (TextUtils.isEmpty(extraString)) {
                extraString = getSegment(intent.getData(), key);
            }
        }
        return extraString;
    }

    public static String getSegment(Bundle extra, String key) {
        String extraString = null;
        if (extra != null) {
            try {
                extraString = extra.getString(key);
            } catch (Throwable t) {
                t.printStackTrace();
                extraString = null;
            }
        }
        return extraString;
    }

    public static String getSegment(Uri uri, String key) {
        String extraString = null;
        if (uri != null) {
            try {
                extraString = uri.getQueryParameter(key);
            } catch (Throwable t) {
                t.printStackTrace();
                extraString = null;
            }
        }
        return extraString;
    }
}
