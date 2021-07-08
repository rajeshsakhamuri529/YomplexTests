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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.reflect.TypeToken;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;
import com.yomplex.tests.activity.ContentVersionUpdateService;
import com.yomplex.tests.database.QuizGameDataBase;
import com.yomplex.tests.model.PlayCount;
import com.yomplex.tests.model.TestDownload;
import com.yomplex.tests.model.UserContentVersion;
import com.yomplex.tests.utils.ConstantPath;
import com.yomplex.tests.utils.SharedPrefs;
import com.yomplex.tests.utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private static String usermail,userid,phone;
    private static FirebaseFirestore firestore;
    private static SharedPrefs sharedPrefs = null;
    private static final Type REVIEW_TYPE = new TypeToken<List<PlayCount>>() {
    }.getType();
    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(final Context context, FirebaseFirestore db) {
        dataBase = new QuizGameDataBase(context);
        ContentDownloadService.context1 = context;
        firestore = db;
        sharedPrefs = new SharedPrefs();
        usermail = sharedPrefs.getPrefVal(context1,"email");
        userid = sharedPrefs.getPrefVal(context1, ConstantPath.UID);
        phone = sharedPrefs.getPrefVal(context,"phonenumber");
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
                                Log.e("grade activity", "for loop...(documentSnapshot.getData().size() - 5)..."+(documentSnapshot.getData().size() - 5));
                                Log.e("grade activity", "for loop...i value..."+i);
                                if(i == 0){
                                    String version = documentSnapshot.getData().get("Calculus1Version").toString();
                                    String url = documentSnapshot.getData().get("Calculus1Url").toString();
                                    Log.e("grade activity", "else....version..."+version);

                                    for(int j = 0; j < testcontentlist.size();j++){
                                        Log.e("grade activity", "else...testcontentlist.get(j).getTestversion()...."+testcontentlist.get(j).getTestversion());
                                        if(testcontentlist.get(j).getTesttype().equals("calculus1")){
                                            if(Integer.parseInt(testcontentlist.get(j).getTestversion()) < Integer.parseInt(version)) {
                                                Log.e("grade activity", "i==0....if......"+version);
                                                //break;
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")

                                           // }else{
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
                                            if(Integer.parseInt(testcontentlist.get(j).getTestversion()) < Integer.parseInt(version)) {
                                                Log.e("grade activity", "i==1....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                               // break;

                                            //}else{
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
                                            if(Integer.parseInt(testcontentlist.get(j).getTestversion()) < Integer.parseInt(version)) {
                                                Log.e("grade activity", "i==2....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                              //  break;

                                           // }else{
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
                                            if(Integer.parseInt(testcontentlist.get(j).getTestversion()) < Integer.parseInt(version)) {
                                                Log.e("grade activity", "i==3....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                             //   break;

                                           // }else{
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
                                            if(Integer.parseInt(testcontentlist.get(j).getTestversion()) < Integer.parseInt(version)) {
                                                Log.e("grade activity", "i==3....if......"+version);
                                                //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                             //   break;

                                           // }else{
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
                public void onSuccess(final DocumentSnapshot documentSnapshot) {

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

                        /*UserContentVersion userContentVersion=new UserContentVersion();
                        //String uid = sharedPrefs.getPrefVal(context,ConstantPath.UID);
                        //String email = sharedPrefs.getPrefVal(context,"email");

                        userContentVersion.setUseremail(usermail);
                        userContentVersion.setUserid(userid);
                        userContentVersion.setPhonenumber(phone);
                        userContentVersion.setAlgebraversion("-1");
                        userContentVersion.setOtherversion("-1");
                        userContentVersion.setGeometryversion("-1");
                        userContentVersion.setCalculus1version("-1");
                        userContentVersion.setCalculus2version("-1");

                        firestore.collection("usercontentversion")
                                .add(userContentVersion)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        //Log.w("HARI", "BGImplementReceiver = Error adding Meetings document", e);
                                    }
                                });*/



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
                                            /*if(usermail.equals("")){
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("phonenumber",phone).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("calculus1version", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());

                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }else{
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("calculus1version", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());
                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }*/


                                            //UpdateContentVersionService.enqueueWork(context1, firestore);
                                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                            String date = dataBase.getContentDate();
                                            if(date != null){
                                                dataBase.updateContentDate(format1.format(Utils.date),1);
                                            }else{
                                                //Log.e("content download","date......................."+format1.format(Utils.date));
                                                dataBase.insertContentUpdateDate(format1.format(Utils.date));
                                            }
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                            String currentDate = sdf.format(new Date());
                                            sharedPrefs.setPrefVal(context1,"contentupdatedate",currentDate);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontentsyncstatus(0,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                            dataBase.updateCourseExist("CALCULUS 1",0);
                                            try{
                                                ContentVersionUpdateService mSensorService = new ContentVersionUpdateService();
                                                Intent mServiceIntent = new Intent(context1, mSensorService.getClass());
                                                if (!Utils.isMyServiceRunning(context1, mSensorService.getClass())) {
                                                    startService(mServiceIntent);
                                                }
                                            }catch(Exception e){

                                            }


                                            try{
                                                String jsonstr = Utils.readFromFile(dirpath+"/"+testtype+"/jee-calculus-1/coursetestinfo.json");
                                                Gson gson = new Gson();
                                                ArrayList<PlayCount> list = gson.fromJson(jsonstr, REVIEW_TYPE);

                                                for(int i = 0;i < list.size();i++) {
                                                    PlayCount playCount = list.get(i);
                                                    int count = dataBase.getPlayCount(playCount.getCourse(),playCount.getTopic(),playCount.getLevel());
                                                    if(count == 0){
                                                        dataBase.insertPlayCount(playCount);
                                                    }
                                                }
                                                File f4=new File(dirpath+"/"+testtype+"/jee-calculus-1/coursetestdelete.json");
                                                if(f4.exists()){
                                                    String jsonstr1 = Utils.readFromFile(dirpath+"/"+testtype+"/jee-calculus-1/coursetestdelete.json");
                                                    ArrayList<PlayCount> list1 = gson.fromJson(jsonstr1, REVIEW_TYPE);
                                                    if(list1.size() > 0){
                                                        for(int i = 0; i < list1.size();i++){
                                                            PlayCount playCount = list1.get(i);
                                                            int count = dataBase.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel());

                                                        }

                                                    }
                                                }

                                            }catch (Exception e){

                                            }


                                        }
                                    }else if(testtype.equals("algebra")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/ii-algebra.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/ii-algebra.zip");
                                            dirFile.delete();
                                        //    File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                          //  boolean isdeleted = Utils.deleteFolder(dirFile1);

                                            /*CollectionReference docRef = firestore.collection("usercontentversion");
                                            docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                        if(task.getResult().size() > 0){

                                                            Map<String, Object> data = new HashMap<>();
                                                            data.put("algebraversion", version);

                                                            firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                    .set(data, SetOptions.merge());
                                                        }
                                                    }
                                                }
                                            });*/
                                            /*if(usermail.equals("")){
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("phonenumber",phone).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("algebraversion", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());

                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }else{
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("algebraversion", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());
                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }*/
                                           // UpdateContentVersionService.enqueueWork(context1, firestore);
                                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                            String date = dataBase.getContentDate();
                                            if(date != null){
                                                dataBase.updateContentDate(format1.format(Utils.date),1);
                                            }else{
                                                //Log.e("content download","date......................."+format1.format(Utils.date));
                                                dataBase.insertContentUpdateDate(format1.format(Utils.date));
                                            }
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                            String currentDate = sdf.format(new Date());
                                            sharedPrefs.setPrefVal(context1,"contentupdatedate",currentDate);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontentsyncstatus(0,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                            dataBase.updateCourseExist("ALGEBRA",0);
                                            try{
                                                ContentVersionUpdateService mSensorService = new ContentVersionUpdateService();
                                                Intent mServiceIntent = new Intent(context1, mSensorService.getClass());
                                                if (!Utils.isMyServiceRunning(context1, mSensorService.getClass())) {
                                                    startService(mServiceIntent);
                                                }
                                            }catch(Exception e){

                                            }

                                            try{
                                                String jsonstr = Utils.readFromFile(dirpath+"/"+testtype+"/ii-algebra/coursetestinfo.json");
                                                Gson gson = new Gson();
                                                ArrayList<PlayCount> list = gson.fromJson(jsonstr, REVIEW_TYPE);

                                                for(int i = 0;i < list.size();i++) {
                                                    PlayCount playCount = list.get(i);
                                                    int count = dataBase.getPlayCount(playCount.getCourse(),playCount.getTopic(),playCount.getLevel());
                                                    if(count == 0){
                                                        dataBase.insertPlayCount(playCount);
                                                    }
                                                }
                                                File f3=new File(dirpath+"/"+testtype+"/ii-algebra/coursetestdelete.json");
                                                if(f3.exists()){
                                                    String jsonstr1 = Utils.readFromFile(dirpath+"/"+testtype+"/ii-algebra/coursetestdelete.json");
                                                    ArrayList<PlayCount> list1 = gson.fromJson(jsonstr1, REVIEW_TYPE);
                                                    if(list1.size() > 0){
                                                        for(int i = 0; i < list1.size();i++){
                                                            PlayCount playCount = list1.get(i);
                                                            int count = dataBase.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel());

                                                        }

                                                    }
                                                }

                                            }catch (Exception e){

                                            }

                                        }
                                    }else if(testtype.equals("calculus2")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/jee-calculus-2.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/jee-calculus-2.zip");
                                            dirFile.delete();
                                          //  File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                           // boolean isdeleted = Utils.deleteFolder(dirFile1);

                                            /*CollectionReference docRef = firestore.collection("usercontentversion");
                                            docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                        if(task.getResult().size() > 0){

                                                            Map<String, Object> data = new HashMap<>();
                                                            data.put("calculus2version", version);

                                                            firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                    .set(data, SetOptions.merge());
                                                        }
                                                    }
                                                }
                                            });*/
                                            /*if(usermail.equals("")){
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("phonenumber",phone).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("calculus2version", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());

                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }else{
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("calculus2version", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());
                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }*/
                                           // UpdateContentVersionService.enqueueWork(context1, firestore);
                                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                            String date = dataBase.getContentDate();
                                            if(date != null){
                                                dataBase.updateContentDate(format1.format(Utils.date),1);
                                            }else{
                                                //Log.e("content download","date......................."+format1.format(Utils.date));
                                                dataBase.insertContentUpdateDate(format1.format(Utils.date));
                                            }
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                            String currentDate = sdf.format(new Date());
                                            sharedPrefs.setPrefVal(context1,"contentupdatedate",currentDate);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontentsyncstatus(0,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                            dataBase.updateCourseExist("CALCULUS 2",0);
                                            try{
                                                ContentVersionUpdateService mSensorService = new ContentVersionUpdateService();
                                                Intent mServiceIntent = new Intent(context1, mSensorService.getClass());
                                                if (!Utils.isMyServiceRunning(context1, mSensorService.getClass())) {
                                                    startService(mServiceIntent);
                                                }
                                            }catch(Exception e){

                                            }

                                            try{
                                                String jsonstr = Utils.readFromFile(dirpath+"/"+testtype+"/jee-calculus-2/coursetestinfo.json");
                                                Gson gson = new Gson();
                                                ArrayList<PlayCount> list = gson.fromJson(jsonstr, REVIEW_TYPE);

                                                for(int i = 0;i < list.size();i++) {
                                                    PlayCount playCount = list.get(i);
                                                    int count = dataBase.getPlayCount(playCount.getCourse(),playCount.getTopic(),playCount.getLevel());
                                                    if(count == 0){
                                                        dataBase.insertPlayCount(playCount);
                                                    }
                                                }
                                                File f2=new File(dirpath+"/"+testtype+"/jee-calculus-2/coursetestdelete.json");
                                                if(f2.exists()){
                                                    String jsonstr1 = Utils.readFromFile(dirpath+"/"+testtype+"/jee-calculus-2/coursetestdelete.json");
                                                    ArrayList<PlayCount> list1 = gson.fromJson(jsonstr1, REVIEW_TYPE);
                                                    if(list1.size() > 0){
                                                        for(int i = 0; i < list1.size();i++){
                                                            PlayCount playCount = list1.get(i);
                                                            int count = dataBase.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel());

                                                        }

                                                    }
                                                }

                                            }catch (Exception e){

                                            }

                                        }
                                    }else if(testtype.equals("geometry")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/iii-geometry.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/iii-geometry.zip");
                                            dirFile.delete();
                                           // File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                           // boolean isdeleted = Utils.deleteFolder(dirFile1);

                                            /*CollectionReference docRef = firestore.collection("usercontentversion");
                                            docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                        if(task.getResult().size() > 0){

                                                            Map<String, Object> data = new HashMap<>();
                                                            data.put("geometryversion", version);

                                                            firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                    .set(data, SetOptions.merge());
                                                        }
                                                    }
                                                }
                                            });*/
                                            /*if(usermail.equals("")){
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("phonenumber",phone).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("geometryversion", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());

                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }else{
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("geometryversion", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());
                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }*/
                                          //  UpdateContentVersionService.enqueueWork(context1, firestore);
                                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                            String date = dataBase.getContentDate();
                                            if(date != null){
                                                dataBase.updateContentDate(format1.format(Utils.date),1);
                                            }else{
                                               // Log.e("content download","date......................."+format1.format(Utils.date));
                                                dataBase.insertContentUpdateDate(format1.format(Utils.date));
                                            }
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                            String currentDate = sdf.format(new Date());
                                            sharedPrefs.setPrefVal(context1,"contentupdatedate",currentDate);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontentsyncstatus(0,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                            dataBase.updateCourseExist("GEOMETRY",0);
                                            try{
                                                ContentVersionUpdateService mSensorService = new ContentVersionUpdateService();
                                                Intent mServiceIntent = new Intent(context1, mSensorService.getClass());
                                                if (!Utils.isMyServiceRunning(context1, mSensorService.getClass())) {
                                                    startService(mServiceIntent);
                                                }
                                            }catch(Exception e){

                                            }

                                            try{
                                                String jsonstr = Utils.readFromFile(dirpath+"/"+testtype+"/iii-geometry/coursetestinfo.json");
                                                Gson gson = new Gson();
                                                ArrayList<PlayCount> list = gson.fromJson(jsonstr, REVIEW_TYPE);

                                                for(int i = 0;i < list.size();i++) {
                                                    PlayCount playCount = list.get(i);
                                                    int count = dataBase.getPlayCount(playCount.getCourse(),playCount.getTopic(),playCount.getLevel());
                                                    if(count == 0){
                                                        dataBase.insertPlayCount(playCount);
                                                    }
                                                }
                                                File f1=new File(dirpath+"/"+testtype+"/iii-geometry/coursetestdelete.json");
                                                if(f1.exists()){
                                                    String jsonstr1 = Utils.readFromFile(dirpath+"/"+testtype+"/iii-geometry/coursetestdelete.json");
                                                    ArrayList<PlayCount> list1 = gson.fromJson(jsonstr1, REVIEW_TYPE);
                                                    if(list1.size() > 0){
                                                        for(int i = 0; i < list1.size();i++){
                                                            PlayCount playCount = list1.get(i);
                                                            int count = dataBase.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel());

                                                        }

                                                    }
                                                }

                                            }catch (Exception e){

                                            }

                                        }
                                    }else if(testtype.equals("other")){
                                        boolean iszip = Utils.unpackZip(dirpath+"/"+testtype,"/other.zip");
                                        if(iszip){
                                            File dirFile = new File(context1.getCacheDir(),testtype+"/other.zip");
                                            dirFile.delete();
                                           // File dirFile1 = new File(context1.getCacheDir(),testtype+"/test");
                                           // boolean isdeleted = Utils.deleteFolder(dirFile1);

                                            /*CollectionReference docRef = firestore.collection("usercontentversion");
                                            docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                        if(task.getResult().size() > 0){

                                                            Map<String, Object> data = new HashMap<>();
                                                            data.put("otherversion", version);

                                                            firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                    .set(data, SetOptions.merge());
                                                        }
                                                    }
                                                }
                                            });*/
                                            /*if(usermail.equals("")){
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("phonenumber",phone).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("otherversion", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());

                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }else{
                                                CollectionReference docRef = firestore.collection("usercontentversion");
                                                docRef.whereEqualTo("useremail",usermail).whereEqualTo("userid",userid)
                                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {

                                                            if(task.getResult().size() > 0){

                                                                Map<String, Object> data = new HashMap<>();
                                                                data.put("otherversion", version);
                                                                data.put("updatedtime" , FieldValue.serverTimestamp());
                                                                firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                        .set(data, SetOptions.merge());
                                                            }
                                                        }
                                                    }
                                                });
                                            }*/
                                           // UpdateContentVersionService.enqueueWork(context1, firestore);
                                            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                            String date = dataBase.getContentDate();
                                            if(date != null){
                                                dataBase.updateContentDate(format1.format(Utils.date),1);
                                            }else{
                                                //Log.e("content download","date......................."+format1.format(Utils.date));
                                                dataBase.insertContentUpdateDate(format1.format(Utils.date));
                                            }
                                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                                            String currentDate = sdf.format(new Date());
                                            sharedPrefs.setPrefVal(context1,"contentupdatedate",currentDate);
                                            dataBase.updatetestcontentversion(version,testtype);
                                            dataBase.updatetestcontentdownloadstatus(1,testtype);
                                            dataBase.updatetestcontentsyncstatus(0,testtype);
                                            dataBase.updatetestcontenturl(url,testtype);
                                            dataBase.updateCourseExist("OTHER",0);
                                            try{
                                                ContentVersionUpdateService mSensorService = new ContentVersionUpdateService();
                                                Intent mServiceIntent = new Intent(context1, mSensorService.getClass());
                                                if (!Utils.isMyServiceRunning(context1, mSensorService.getClass())) {
                                                    startService(mServiceIntent);
                                                }
                                            }catch(Exception e){

                                            }

                                            try{
                                                String jsonstr = Utils.readFromFile(dirpath+"/"+testtype+"/other/coursetestinfo.json");
                                                Gson gson = new Gson();
                                                ArrayList<PlayCount> list = gson.fromJson(jsonstr, REVIEW_TYPE);

                                                for(int i = 0;i < list.size();i++) {
                                                    PlayCount playCount = list.get(i);
                                                    int count = dataBase.getPlayCount(playCount.getCourse(),playCount.getTopic(),playCount.getLevel());
                                                    if(count == 0){
                                                        dataBase.insertPlayCount(playCount);
                                                    }
                                                }
                                                File f=new File(dirpath+"/"+testtype+"/other/coursetestdelete.json");
                                                if(f.exists()){
                                                    String jsonstr1 = Utils.readFromFile(dirpath+"/"+testtype+"/other/coursetestdelete.json");
                                                    ArrayList<PlayCount> list1 = gson.fromJson(jsonstr1, REVIEW_TYPE);
                                                    if(list1.size() > 0){
                                                        for(int i = 0; i < list1.size();i++){
                                                            PlayCount playCount = list1.get(i);
                                                            int count = dataBase.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel());

                                                        }

                                                    }
                                                }

                                            }catch (Exception e){

                                            }

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
