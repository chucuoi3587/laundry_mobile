package nhan.natc.laundry.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;

import androidx.core.content.ContextCompat;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CommonUtils {

    public static boolean isNullOrEmpty(String data) {
        if (data != null && !data.equals(""))
            return false;
        return true;
    }

    public static int getColor(int colorId, Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, colorId);
        } else {
            return context.getColor(colorId);
        }
    }

    public static void hideKeyboard(View view, Context context) {
        if (view == null)
            return;
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void forceHideKeyboard(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void setBackground(View view, int rippleDrawable, int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setBackground(CommonUtils.getResourceDrawable(view.getContext(), rippleDrawable));
        } else {
            view.setBackground(CommonUtils.getResourceDrawable(view.getContext(), drawable));
        }
    }

    public static Drawable getResourceDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            return context.getResources().getDrawable(id);
        } else {
            return context.getResources().getDrawable(id, null);
        }
    }

    public static String makeMd5(String stringBeforeSign) {
        String md5string;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] md5hash = digest.digest(stringBeforeSign.getBytes());
            BigInteger md5hex = new BigInteger(1, md5hash);
            md5string = md5hex.toString(16);
            // Now we need to zero pad it if you actually want the full 32
            // chars.
            while (md5string.length() < 32) {
                md5string = "0" + md5string;
            }
        } catch (NoSuchAlgorithmException e) {
            md5string = Long.toHexString(stringBeforeSign.hashCode());
            // Log.e("urlToMd5String", e.getMessage());
        }
        return md5string;
    }

    /**
     * check permission on Android 6
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermission(String permission, Context context) {
        return (context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    public static String getMimeType(Uri uri, Context context) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }
}
