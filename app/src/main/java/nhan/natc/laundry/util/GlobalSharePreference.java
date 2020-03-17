package nhan.natc.laundry.util;

import android.content.Context;
import android.content.SharedPreferences;

import nhan.natc.laundry.Constants;
import nhan.natc.laundry.data.local.User;
import nhan.natc.laundry.data.local.UserRole;

public class GlobalSharePreference {

    public static void storeUser(User user, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE).edit();
        editor.putString(Constants.USER_TAG_EMAIL, user.getEmail());
        editor.putString(Constants.USER_TAG_FIRST_NAME, user.getFirstName());
        editor.putString(Constants.USER_TAG_LAST_NAME, user.getLastName());
        editor.putInt(Constants.USER_TAG_ROLE_ID, user.getUserRole().getId());
        editor.putString(Constants.USER_TAG_ROLE_DESCRIPTION, user.getUserRole().getRoleDescription());
        editor.putString(Constants.USER_TAG_AVATAR, user.getAvatar());
        editor.commit();
    }

    public static User getUser(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        User user = new User();
        user.setEmail(pref.getString(Constants.USER_TAG_EMAIL, ""));
        user.setFirstName(pref.getString(Constants.USER_TAG_FIRST_NAME, ""));
        user.setLastName(pref.getString(Constants.USER_TAG_LAST_NAME, ""));
        UserRole role = new UserRole(pref.getInt(Constants.USER_TAG_ROLE_ID, 0), pref.getString(Constants.USER_TAG_ROLE_DESCRIPTION, ""));
        user.setUserRole(role);
        user.setAvatar(pref.getString(Constants.USER_TAG_AVATAR, ""));
        return user;
    }

    public static void storeToken(String token, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE).edit();
        editor.putString(Constants.USER_TAG_ACCESS_TOKEN, token);
        editor.commit();
    }

    public static String getToken(Context context) {
        SharedPreferences pref = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return pref.getString(Constants.USER_TAG_ACCESS_TOKEN, "");
    }

    public static void clearUser(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(
                context.getPackageName(), Context.MODE_PRIVATE).edit();
        editor.remove(Constants.USER_TAG_EMAIL);
        editor.remove(Constants.USER_TAG_FIRST_NAME);
        editor.remove(Constants.USER_TAG_LAST_NAME);
        editor.remove(Constants.USER_TAG_ACCESS_TOKEN);
        editor.commit();
    }
}
