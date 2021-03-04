package com.yomplex.tests.Service;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yomplex.tests.database.QuizGameDataBase;
import com.yomplex.tests.model.TestDownload;
import com.yomplex.tests.utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContentDownloadService extends JobIntentService {

    public static final String URL = "url";
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
    private static String dirpath;
    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, FirebaseFirestore db) {
        dataBase = new QuizGameDataBase(context);
        ContentDownloadService.context1 = context;
        File f =  new File((context.getCacheDir()).getAbsolutePath());
        dirpath = f.getAbsolutePath();
        List<Integer> statuslist = dataBase.gettesttopicdownloadstatus();
        //Log.e("content download","status list........"+statuslist);
        Log.e("content download","status list....size...."+statuslist.size());
        //Log.e("content download","statuslist.contains(\"0\")...."+statuslist.contains("0"));
        if(statuslist.size() > 0){
            if(statuslist.contains(0)){
                Log.e("content download","statuslist.contains(\"0\").....if....");
                List<TestDownload> testcontentlist = dataBase.gettestContent();
                for(int i= 0; i < testcontentlist.size();i++){
                    if(testcontentlist.get(i).getTestdownloadstatus() == 0){
                        String url = testcontentlist.get(i).getTesturl();
                        String version = testcontentlist.get(i).getTestversion();
                        String type = testcontentlist.get(i).getTesttype();
                        //downloadDataFromBackground(this@GradeActivity,url,version,type)
                        Intent intent = new Intent(context, ContentDownloadService.class);
                        intent.putExtra(URL, url);
                        intent.putExtra("version", version);
                        intent.putExtra("testtype", type);
                        intent.setAction(ACTION_DOWNLOAD);
                        enqueueWork(context, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                    }
                }
            } else {
                Log.e("content download","statuslist.contains(\"0\").....else.....");
                DocumentReference docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4");
                docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot != null) {
                            Log.e("grade activity", "else....DocumentSnapshot data: ${document.data}..."+documentSnapshot.getData());
                            Log.e("grade activity", "else...DocumentSnapshot data: ${document.data!!.size}...."+documentSnapshot.getData().size());
                            List<TestDownload> testcontentlist = dataBase.gettestContent();
                            Log.e("grade activity", "else....testcontentlist.size......"+testcontentlist.size());

                            for(int i = 0;i < (documentSnapshot.getData().size() - 5);i++){
                                if(i == 0){
                                    String version = documentSnapshot.getData().get("Calculus1Version").toString();
                                    String url = documentSnapshot.getData().get("Calculus1Url").toString();
                                    Log.e("grade activity", "else....version..."+version);

                                    for(int j = 0; j < testcontentlist.size();j++){
                                        Log.e("grade activity", "else...testcontentlist.get(j).getTestversion()...."+testcontentlist.get(j).getTestversion());
                                        if(testcontentlist.get(j).getTesttype().equals("calculus1")){
                                            if(testcontentlist.get(j).getTestversion().equals(version)) {
                                                Log.e("grade activity", "i==0....if......"+version);
                                                break;
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")

                                            }else{
                                                dataBase.updatetestcontentdownloadstatus(0,"calculus1");
                                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                                intent.putExtra(URL, url);
                                                intent.putExtra("version", version);
                                                intent.putExtra("testtype", "calculus1");
                                                intent.setAction(ACTION_DOWNLOAD);
                                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                                            }

                                        }
                                    }



                                }else if(i == 1){
                                    String version = documentSnapshot.getData().get("AlgebraVersion").toString();
                                    String url = documentSnapshot.getData().get("AlgebraUrl").toString();
                                    for(int j = 0; j < testcontentlist.size();j++){
                                        if(testcontentlist.get(j).getTesttype().equals("algebra")){
                                            if(testcontentlist.get(j).getTestversion().equals(version)) {
                                                Log.e("grade activity", "i==1....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                                break;

                                            }else{
                                                dataBase.updatetestcontentdownloadstatus(0,"algebra");
                                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                                intent.putExtra(URL, url);
                                                intent.putExtra("version", version);
                                                intent.putExtra("testtype", "algebra");
                                                intent.setAction(ACTION_DOWNLOAD);
                                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                                            }

                                        }
                                    }
                                }else if(i == 2){
                                    String version = documentSnapshot.getData().get("Calculus2Version").toString();
                                    String url = documentSnapshot.getData().get("Calculus2Url").toString();
                                    for(int j = 0; j < testcontentlist.size();j++){
                                        if(testcontentlist.get(j).getTesttype().equals("calculus2")){
                                            if(testcontentlist.get(j).getTestversion().equals(version)) {
                                                Log.e("grade activity", "i==2....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                                break;

                                            }else{
                                                dataBase.updatetestcontentdownloadstatus(0,"calculus2");
                                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                                intent.putExtra(URL, url);
                                                intent.putExtra("version", version);
                                                intent.putExtra("testtype", "calculus2");
                                                intent.setAction(ACTION_DOWNLOAD);
                                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                                            }

                                        }
                                    }
                                }else if(i == 3){
                                    String version = documentSnapshot.getData().get("GeometryVersion").toString();
                                    String url = documentSnapshot.getData().get("GeometryUrl").toString();
                                    for(int j = 0; j < testcontentlist.size();j++){
                                        if(testcontentlist.get(j).getTesttype().equals("geometry")){
                                            if(testcontentlist.get(j).getTestversion().equals(version)) {
                                                Log.e("grade activity", "i==3....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                                break;

                                            }else{
                                                dataBase.updatetestcontentdownloadstatus(0,"geometry");
                                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                                intent.putExtra(URL, url);
                                                intent.putExtra("version", version);
                                                intent.putExtra("testtype", "geometry");
                                                intent.setAction(ACTION_DOWNLOAD);
                                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                                            }

                                        }
                                    }
                                }else if(i == 4){
                                    String version = documentSnapshot.getData().get("BasicVersion").toString();
                                    String url = documentSnapshot.getData().get("BasicUrl").toString();
                                    for(int j = 0; j < testcontentlist.size();j++){
                                        if(testcontentlist.get(j).getTesttype().equals("other")){
                                            if(testcontentlist.get(j).getTestversion().equals(version)) {
                                                Log.e("grade activity", "i==3....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                                break;

                                            }else{
                                                dataBase.updatetestcontentdownloadstatus(0,"other");
                                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                                intent.putExtra(URL, url);
                                                intent.putExtra("version", version);
                                                intent.putExtra("testtype", "other");
                                                intent.setAction(ACTION_DOWNLOAD);
                                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                                            }

                                        }
                                    }
                                }

                            }
                        }else {
                            Log.e("grade activity", "No such document");
                        }

                    }
                });
            }
        }else {
            DocumentReference docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4");
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if (documentSnapshot != null) {
                        Log.e("grade activity", "DocumentSnapshot data: ${document.data}..."+documentSnapshot.getData());
                        Log.e("grade activity", "DocumentSnapshot data: ${document.data!!.size}...."+documentSnapshot.getData().size());
                        for(int i = 0;i < (documentSnapshot.getData().size() - 5);i++){
                            if(i == 0){
                                String version = documentSnapshot.getData().get("Calculus2Version").toString();
                                String url = documentSnapshot.getData().get("Calculus2Url").toString();
                                dataBase.insertTESTCONTENTDOWNLOAD(version,url,"calculus2",0);
                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                intent.putExtra(URL, url);
                                intent.putExtra("version", version);
                                intent.putExtra("testtype", "calculus2");
                                intent.setAction(ACTION_DOWNLOAD);
                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                            }else if(i == 1){
                                String version = documentSnapshot.getData().get("AlgebraVersion").toString();
                                String url = documentSnapshot.getData().get("AlgebraUrl").toString();
                                dataBase.insertTESTCONTENTDOWNLOAD(version,url,"algebra",0);
                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                intent.putExtra(URL, url);
                                intent.putExtra("version", version);
                                intent.putExtra("testtype", "algebra");
                                intent.setAction(ACTION_DOWNLOAD);
                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                            }else if(i == 2){
                                String version = documentSnapshot.getData().get("Calculus1Version").toString();
                                String url = documentSnapshot.getData().get("Calculus1Url").toString();
                                dataBase.insertTESTCONTENTDOWNLOAD(version,url,"calculus1",0);
                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                intent.putExtra(URL, url);
                                intent.putExtra("version", version);
                                intent.putExtra("testtype", "calculus1");
                                intent.setAction(ACTION_DOWNLOAD);
                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                            }else if(i == 3){
                                String version = documentSnapshot.getData().get("GeometryVersion").toString();
                                String url = documentSnapshot.getData().get("GeometryUrl").toString();
                                dataBase.insertTESTCONTENTDOWNLOAD(version,url,"geometry",0);
                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                intent.putExtra(URL, url);
                                intent.putExtra("version", version);
                                intent.putExtra("testtype", "geometry");
                                intent.setAction(ACTION_DOWNLOAD);
                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                            }else if(i == 4){
                                String version = documentSnapshot.getData().get("BasicVersion").toString();
                                String url = documentSnapshot.getData().get("BasicUrl").toString();
                                dataBase.insertTESTCONTENTDOWNLOAD(version,url,"other",0);
                                Intent intent = new Intent(context1, ContentDownloadService.class);
                                intent.putExtra(URL, url);
                                intent.putExtra("version", version);
                                intent.putExtra("testtype", "other");
                                intent.setAction(ACTION_DOWNLOAD);
                                enqueueWork(context1, ContentDownloadService.class, DOWNLOAD_JOB_ID, intent);
                            }

                        }
                    }else {
                        Log.e("grade activity", "No such document");
                    }

                }
            });
        }







    }
    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case ACTION_DOWNLOAD:
                    //mResultReceiver = intent.getParcelableExtra(RECEIVER);
                    final String url = intent.getStringExtra(URL);
                    final String version = intent.getStringExtra("version");
                    final String testtype = intent.getStringExtra("testtype");
                    Log.e("content download","dirpath........"+dirpath);
                    Log.e("content download","testtype........"+testtype);
                    String filename ="";
                    if(testtype.equals("calculus1")){
                        filename = "/jee-calculus-1.zip";
                    }else if(testtype.equals("calculus2")){
                        filename = "/jee-calculus-2.zip";
                    }else if(testtype.equals("algebra")){
                        filename = "/ii-algebra.zip";
                    }else if(testtype.equals("other")){
                        filename = "/other.zip";
                    }else if(testtype.equals("geometry")){
                        filename = "/iii-geometry.zip";
                    }
                    Log.e("content download","filename........"+filename);
                    Log.e("content download","url........"+url);
                    final String finalFilename = filename;
                    int downloadId = PRDownloader.download(url, dirpath+"/"+testtype, filename)
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {
                                    Log.e("job service","onStartOrResume.....");
                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {
                                    Log.e("job service","onPause.....");
                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {
                                    Log.e("job service","onCancel.....");
                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {
                                    Log.e("job service","onProgress....."+progress);
                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    Log.e("job service","onDownloadComplete.....");
                                    try{
                                        File dirFile = new File(context1.getCacheDir(),"test");
                                        FileUtils.deleteDirectory(dirFile);
                                    }catch (Exception e){

                                    }
                                    if(testtype.equals("calculus1")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/jee-calculus-1.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/jee-calculus-1.zip");
                                            dirFile.delete();
                                       //     File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                        //    boolean isdeleted = Utils.deleteFolder(dirFile1);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                        }
                                    }else if(testtype.equals("algebra")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/ii-algebra.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/ii-algebra.zip");
                                            dirFile.delete();
                                        //    File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                          //  boolean isdeleted = Utils.deleteFolder(dirFile1);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                        }
                                    }else if(testtype.equals("calculus2")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/jee-calculus-2.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/jee-calculus-2.zip");
                                            dirFile.delete();
                                          //  File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                           // boolean isdeleted = Utils.deleteFolder(dirFile1);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                        }
                                    }else if(testtype.equals("geometry")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/iii-geometry.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/iii-geometry.zip");
                                            dirFile.delete();
                                           // File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                           // boolean isdeleted = Utils.deleteFolder(dirFile1);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                        }
                                    }else if(testtype.equals("other")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/other.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/other.zip");
                                            dirFile.delete();
                                           // File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                           // boolean isdeleted = Utils.deleteFolder(dirFile1);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                        }
                                    }

                                    /*Bundle bundle = new Bundle();
                                    bundle.putString("data","success");
                                    mResultReceiver.send(SHOW_RESULT, bundle);*/
                                }

                                @Override
                                public void onError(Error error) {
                                    Log.e("job service","onerror....."+error.toString());
                                    // JobService.enqueueWork(context1,url,version);
                                    /*Bundle bundle = new Bundle();
                                    bundle.putString("data","failure");
                                    mResultReceiver.send(SHOW_RESULT, bundle);*/
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
