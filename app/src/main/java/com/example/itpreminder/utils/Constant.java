package com.example.itpreminder.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.example.itpreminder.model.User;

public class Constant {
    public static final String USERS = "users";
    public static final String CARS = "cars";
    public static final String MY_LOGIN_DATA = "MY_LOGIN_DATA";
    public static final String REMEMBER_ME = "remember_me";
    public static final String USERNAME = "username";
    public static final String EMPTY_STRING = "";
    public static final String AUTO_LOGIN = "auto_login";
    public static final String DATE_PATTERN = "d-mm-yyyy";
    public static final String TEL = "tel:";
    public static int ANIMATION_DURATION = 1500;
    public static User CURRENT_USER;
    public static String[] PERMISSIONS = {
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

}
