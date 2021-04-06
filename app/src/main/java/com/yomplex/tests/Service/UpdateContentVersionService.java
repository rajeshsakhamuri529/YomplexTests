package com.yomplex.tests.Service;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.google.firebase.firestore.FirebaseFirestore;
import com.yomplex.tests.database.QuizGameDataBase;
import com.yomplex.tests.utils.ConstantPath;
import com.yomplex.tests.utils.SharedPrefs;

public class UpdateContentVersionService extends JobIntentService {

    /**
     * Unique job ID for this service.
     */
    static final int DOWNLOAD_JOB_ID = 1000;
    /**
     * Actions download
     */
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";

    private static QuizGameDataBase dataBase;
    private static Context context1;
    private static String usermail,userid,phone;
    private static FirebaseFirestore firestore;
    private static SharedPrefs sharedPrefs = null;




    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(final Context context, FirebaseFirestore db) {
        dataBase = new QuizGameDataBase(context);
        UpdateContentVersionService.context1 = context;
        firestore = db;
        sharedPrefs = new SharedPrefs();
        usermail = sharedPrefs.getPrefVal(context1,"email");
        userid = sharedPrefs.getPrefVal(context1, ConstantPath.UID);
        phone = sharedPrefs.getPrefVal(context,"phonenumber");

        Log.e("update content version","enque work......");


    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

    }
}
