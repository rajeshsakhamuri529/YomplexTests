package com.yomplex.tests.Service;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.yomplex.tests.database.QuizGameDataBase;
import com.yomplex.tests.utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * Created by Sambhaji Karad on 01/11/18.
 */

public class ProgressJobService extends JobIntentService {

    private static final String TAG = ProgressJobService.class.getSimpleName();
    public static final String RECEIVER = "receiver";
    public static final String URL = "url";
    public static final int SHOW_RESULT = 123;
    /**
     * Result receiver object to send results
     */
    private ResultReceiver mResultReceiver;
    /**
     * Unique job ID for this service.
     */
    static final int DOWNLOAD_JOB_ID = 1000;
    /**
     * Actions download
     */
    private static final String ACTION_DOWNLOAD = "action.DOWNLOAD_DATA";
    private static String dirpath;
    private static Context context1;
    private static QuizGameDataBase dataBase;
    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, String url, String version, ServiceResultReceiver workerResultReceiver) {
        dataBase = new QuizGameDataBase(context);
        File f =  new File((context.getExternalFilesDir(null)).getAbsolutePath());
        dirpath = f.getAbsolutePath();
        ProgressJobService.context1 = context;
        Intent intent = new Intent(context, ProgressJobService.class);
        intent.putExtra(RECEIVER, workerResultReceiver);
        intent.putExtra(URL, url);
        intent.putExtra("version", version);
        intent.setAction(ACTION_DOWNLOAD);
        enqueueWork(context, ProgressJobService.class, DOWNLOAD_JOB_ID, intent);
    }



    @SuppressLint("DefaultLocale")
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "onHandleWork() called with: intent = [" + intent + "]");
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_DOWNLOAD:
                    mResultReceiver = intent.getParcelableExtra(RECEIVER);
                    final String url = intent.getStringExtra(URL);
                    final String version = intent.getStringExtra("version");
                    Log.e("progress job service","url....."+url);
                    Log.e("progress job service","version....."+version);
                    int downloadId = PRDownloader.download(url, dirpath, "/testcontent.rar")
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {
                                    Log.e("ProgressJobService","onStartOrResume.....");
                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {
                                    Log.e("ProgressJobService","onPause.....");
                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                    Log.e("ProgressJobService","onCancel.....");
                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {
                                    Log.e("ProgressJobService","onProgress....."+progress);
                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    Log.e("ProgressJobService","onDownloadComplete.....");
                                    try{
                                        File dirFile = new File(context1.getExternalFilesDir(null),"test");
                                        FileUtils.deleteDirectory(dirFile);
                                    }catch (Exception e){

                                    }
                                    boolean iszip = Utils.unpackZip(dirpath,"/testcontent.rar");
                                    if(iszip){
                                        File dirFile = new File(context1.getExternalFilesDir(null),"testcontent.rar");
                                        dirFile.delete();
                                        dataBase.updatetestcontentversion(version,"");
                                        dataBase.updatetestcontentdownloadstatus(1,"");
                                    }
                                    Bundle bundle = new Bundle();
                                    bundle.putString("data","success");
                                    mResultReceiver.send(SHOW_RESULT, bundle);
                                }

                                @Override
                                public void onError(Error error) {
                                    Log.e("ProgressJobService","onerror....."+error);
                                    // JobService.enqueueWork(context1,url,version);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("data","failure");
                                    mResultReceiver.send(SHOW_RESULT, bundle);
                                }

                                /*@Override
                                public void onError(Error error) {
                                    Log.e("job service","onerror....."+error);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("data","failure");
                                    mResultReceiver.send(SHOW_RESULT, bundle);
                                }*/


                            });
                    /*for(int i=0;i<10;i++){
                        try {
                            Thread.sleep(1000);
                            Bundle bundle = new Bundle();
                            bundle.putString("data",String.format("Showing From JobIntent Service %d", i));
                            mResultReceiver.send(SHOW_RESULT, bundle);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }*/
                    break;
            }
        }
    }
}
