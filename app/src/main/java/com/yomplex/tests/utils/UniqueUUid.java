package com.yomplex.tests.utils;

import android.content.Context;
import android.util.Log;

import java.util.UUID;

import static com.yomplex.tests.utils.ConstantPath.PREF_UNIQUE_ID;


public class UniqueUUid {
    private static String uniqueID = null;

    public synchronized static String id(Context context) {
        if (uniqueID == null) {
            SharedPrefs sharedPrefs = new SharedPrefs();
            uniqueID = sharedPrefs.getPrefVal(context, PREF_UNIQUE_ID);
            Log.e("unique id",".....id...."+uniqueID);
            if (uniqueID == null || uniqueID == "") {
                uniqueID = UUID.randomUUID().toString();
                sharedPrefs.setPrefVal(context, PREF_UNIQUE_ID, uniqueID);
            }
        }
        return uniqueID;
    }
}
