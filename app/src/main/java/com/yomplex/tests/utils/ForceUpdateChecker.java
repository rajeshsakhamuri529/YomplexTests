package com.yomplex.tests.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.yomplex.tests.activity.GradeActivity;

public class ForceUpdateChecker {

    private static final String TAG = ForceUpdateChecker.class.getSimpleName();

    public static final String KEY_UPDATE_REQUIRED = "force_update_required";
    public static final String KEY_CURRENT_VERSION = "force_update_current_version";
    public static final String MIN_APP_VERSION = "minAppVer";
    public static final String KEY_UPDATE_URL = "force_update_store_url";

    private OnUpdateNeededListener onUpdateNeededListener;
    private Context context;

    public interface OnUpdateNeededListener {
        void onUpdateNeeded(String updateUrl);
    }

    public static Builder with(@NonNull Context context) {
        return new Builder(context);
    }

    public ForceUpdateChecker(@NonNull Context context, OnUpdateNeededListener onUpdateNeededListener) {
        this.context = context;
        this.onUpdateNeededListener = onUpdateNeededListener;
    }

    public void check() {
        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        if (/*remoteConfig.getBoolean(KEY_UPDATE_REQUIRED)*/true) {
            Log.d("ForceUpdateCHecker","keyUpdate true");
            Log.d("remote",remoteConfig.getAll().toString()+"!");
            String currentVersion = remoteConfig.getString(MIN_APP_VERSION);
            String appVersion = getAppVersion(context);
            String updateUrl = remoteConfig.getString(KEY_UPDATE_URL);

            Log.d("currentVersion",currentVersion+"!");
            Log.d("appVersion",appVersion+"!");
            Log.d("updateUrl",updateUrl+"!"+remoteConfig.getBoolean(KEY_UPDATE_REQUIRED));
            updateUrl = "https://play.google.com/store/apps/details?id=com.sembozdemir.renstagram";
            if (!TextUtils.equals(currentVersion, appVersion) && onUpdateNeededListener != null) {
                Log.d("ForceUpdateCHecker","appVersion true");
                //onUpdateNeededListener.onUpdateNeeded(updateUrl);
                gradeActivity();
            }else{
                Log.d("ForceUpdateCHecker","appVersion false");
                gradeActivity();
            }

        }else{
            Log.d("ForceUpdateCHecker","keyUpdate false");
            gradeActivity();
        }
    }

    private void gradeActivity()
    {
        Intent intent = new Intent(context, GradeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);


    }

    private String getAppVersion(Context context) {
        String result = "";

        try {
            result = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0)
                    .versionName;
            result = result.replaceAll("[a-zA-Z]|-", "");
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        return result;
    }

    public static class Builder {

        private Context context;
        private OnUpdateNeededListener onUpdateNeededListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder onUpdateNeeded(OnUpdateNeededListener onUpdateNeededListener) {
            this.onUpdateNeededListener = onUpdateNeededListener;
            return this;
        }

        public ForceUpdateChecker build() {
            return new ForceUpdateChecker(context, onUpdateNeededListener);
        }

        public ForceUpdateChecker check() {
            ForceUpdateChecker forceUpdateChecker = build();
            forceUpdateChecker.check();

            return forceUpdateChecker;
        }
    }
}