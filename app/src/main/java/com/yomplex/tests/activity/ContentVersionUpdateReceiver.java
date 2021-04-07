package com.yomplex.tests.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.yomplex.tests.database.QuizGameDataBase;
import com.yomplex.tests.model.TestDownload;
import com.yomplex.tests.model.UserContentVersion;
import com.yomplex.tests.utils.ConstantPath;
import com.yomplex.tests.utils.SharedPrefs;
import com.yomplex.tests.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentVersionUpdateReceiver extends BroadcastReceiver {

    private FirebaseFirestore firestore;
    private QuizGameDataBase databaseHandler;
    private SharedPrefs sharedPrefs;
    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            Log.i("BGImplementReceiver-->", "HARI");
            if (Utils.isOnline(context)) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                databaseHandler = new QuizGameDataBase(context);
                sharedPrefs = new SharedPrefs();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        List<Integer> syncstatuslist = databaseHandler.gettesttopicsyncstatus();

                        String userid = sharedPrefs.getPrefVal(context, ConstantPath.UID);
                        String usermail = sharedPrefs.getPrefVal(context,"email");
                        String phone = sharedPrefs.getPrefVal(context,"phonenumber");
                        Log.e("UpdateReceiver","userid........."+userid);
                        Log.e("UpdateReceiver","usermail........."+usermail);
                        Log.e("UpdateReceiver","phone........."+phone);
                        Log.e("UpdateReceiver","syncstatuslist.size()........."+syncstatuslist.size());

                        if(syncstatuslist.size() == 5) {
                            if (syncstatuslist.contains(0)) {
                                firestore = FirebaseFirestore.getInstance();
                                if(usermail.equals("")){
                                    CollectionReference docRef = firestore.collection("usercontentversion");
                                    docRef.whereEqualTo("phonenumber",phone).whereEqualTo("userid",userid)
                                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                if(task.getResult().size() > 0){
                                                    if(task.getResult().size() == 1){
                                                        List<TestDownload> testcontentlist = databaseHandler.gettestContent();
                                                        String algversion = "";
                                                        String cal1version = "";
                                                        String cal2version = "";
                                                        String othversion = "";
                                                        String gemversion = "";
                                                        for(int i = 0; i < testcontentlist.size(); i++) {
                                                            if (testcontentlist.get(i).getTesttype().equals("algebra")) {
                                                                algversion = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus1")) {
                                                                cal1version = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus2")) {
                                                                cal2version = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("geometry")) {
                                                                gemversion = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("other")) {
                                                                othversion = (testcontentlist.get(i).getTestversion());
                                                            }


                                                        }
                                                        String updatedate= sharedPrefs.getPrefVal(context,"contentupdatedate");
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("calculus1version", cal1version);
                                                        data.put("algebraversion", algversion);
                                                        data.put("calculus2version", cal2version);
                                                        data.put("geometryversion", gemversion);
                                                        data.put("otherversion", othversion);
                                                        data.put("updatedtime" , updatedate);

                                                        firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                .set(data, SetOptions.merge());

                                                        databaseHandler.updatetestcontentsyncstatus(1,"algebra");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus1");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"geometry");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"other");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus2");
                                                    }else{
                                                        for(int i = 0;i< task.getResult().size();i++) {
                                                            Log.e("dashboard","doc id......................"+task.getResult().getDocuments().get(i).getId());
                                                            firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(i).getId())
                                                                    .delete();
                                                        }
                                                        String updatedate= sharedPrefs.getPrefVal(context,"contentupdatedate");
                                                        UserContentVersion userContentVersion = new UserContentVersion();
                                                        userContentVersion.setUseremail(usermail);
                                                        userContentVersion.setUserid(userid);
                                                        userContentVersion.setPhonenumber(phone);
                                                        userContentVersion.setUpdatedtime(updatedate);
                                                        List<TestDownload> testcontentlist = databaseHandler.gettestContent();
                                                        for(int i = 0; i<testcontentlist.size();i++) {
                                                            if (testcontentlist.get(i).getTesttype().equals("algebra")) {
                                                                userContentVersion.setAlgebraversion(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus1")) {
                                                                userContentVersion.setCalculus1version(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus2")) {
                                                                userContentVersion.setCalculus2version(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("geometry")) {
                                                                userContentVersion.setGeometryversion(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("other")) {
                                                                userContentVersion.setOtherversion(testcontentlist.get(i).getTestversion());
                                                            }


                                                        }
                                                        firestore.collection("usercontentversion")
                                                                .add(userContentVersion)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"algebra");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus1");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"geometry");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"other");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus2");
                                                                        Log.d("HARI", "BGImplementReceiver = Meetings document added - id: " + documentReference.getId());
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w("HARI", "BGImplementReceiver = Error adding Meetings document", e);
                                                                    }
                                                                });


                                                    }


                                                }else{
                                                    String updatedate= sharedPrefs.getPrefVal(context,"contentupdatedate");
                                                    UserContentVersion userContentVersion = new UserContentVersion();
                                                    userContentVersion.setUseremail(usermail);
                                                    userContentVersion.setUserid(userid);
                                                    userContentVersion.setPhonenumber(phone);
                                                    userContentVersion.setUpdatedtime(updatedate);
                                                    List<TestDownload> testcontentlist = databaseHandler.gettestContent();
                                                    for(int i = 0; i<testcontentlist.size();i++) {
                                                        if (testcontentlist.get(i).getTesttype().equals("algebra")) {
                                                            userContentVersion.setAlgebraversion(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("calculus1")) {
                                                            userContentVersion.setCalculus1version(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("calculus2")) {
                                                            userContentVersion.setCalculus2version(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("geometry")) {
                                                            userContentVersion.setGeometryversion(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("other")) {
                                                            userContentVersion.setOtherversion(testcontentlist.get(i).getTestversion());
                                                        }


                                                    }
                                                    firestore.collection("usercontentversion")
                                                            .add(userContentVersion)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"algebra");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"calculus1");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"geometry");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"other");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"calculus2");
                                                                    Log.d("HARI", "BGImplementReceiver = Meetings document added - id: " + documentReference.getId());
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("HARI", "BGImplementReceiver = Error adding Meetings document", e);
                                                                }
                                                            });
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
                                                //List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();
                                                if(task.getResult().size() > 0){
                                                    if(task.getResult().size() == 1){
                                                        List<TestDownload> testcontentlist = databaseHandler.gettestContent();
                                                        String algversion = "";
                                                        String cal1version = "";
                                                        String cal2version = "";
                                                        String othversion = "";
                                                        String gemversion = "";
                                                        for(int i = 0; i < testcontentlist.size(); i++) {
                                                            if (testcontentlist.get(i).getTesttype().equals("algebra")) {
                                                                algversion = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus1")) {
                                                                cal1version = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus2")) {
                                                                cal2version = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("geometry")) {
                                                                gemversion = (testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("other")) {
                                                                othversion = (testcontentlist.get(i).getTestversion());
                                                            }


                                                        }
                                                        String updatedate= sharedPrefs.getPrefVal(context,"contentupdatedate");
                                                        Map<String, Object> data = new HashMap<>();
                                                        data.put("calculus1version", cal1version);
                                                        data.put("algebraversion", algversion);
                                                        data.put("calculus2version", cal2version);
                                                        data.put("geometryversion", gemversion);
                                                        data.put("otherversion", othversion);
                                                        data.put("updatedtime" , updatedate);

                                                        firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(0).getId())
                                                                .set(data, SetOptions.merge());

                                                        databaseHandler.updatetestcontentsyncstatus(1,"algebra");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus1");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"geometry");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"other");
                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus2");
                                                    }else{
                                                        for(int i = 0;i< task.getResult().size();i++) {
                                                            Log.e("dashboard","doc id......................"+task.getResult().getDocuments().get(i).getId());
                                                            firestore.collection("usercontentversion").document(task.getResult().getDocuments().get(i).getId())
                                                                    .delete();
                                                        }
                                                        String updatedate= sharedPrefs.getPrefVal(context,"contentupdatedate");
                                                        UserContentVersion userContentVersion = new UserContentVersion();
                                                        userContentVersion.setUseremail(usermail);
                                                        userContentVersion.setUserid(userid);
                                                        userContentVersion.setPhonenumber(phone);
                                                        userContentVersion.setUpdatedtime(updatedate);
                                                        List<TestDownload> testcontentlist = databaseHandler.gettestContent();
                                                        for(int i = 0; i<testcontentlist.size();i++) {
                                                            if (testcontentlist.get(i).getTesttype().equals("algebra")) {
                                                                userContentVersion.setAlgebraversion(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus1")) {
                                                                userContentVersion.setCalculus1version(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("calculus2")) {
                                                                userContentVersion.setCalculus2version(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("geometry")) {
                                                                userContentVersion.setGeometryversion(testcontentlist.get(i).getTestversion());
                                                            }
                                                            if (testcontentlist.get(i).getTesttype().equals("other")) {
                                                                userContentVersion.setOtherversion(testcontentlist.get(i).getTestversion());
                                                            }


                                                        }
                                                        firestore.collection("usercontentversion")
                                                                .add(userContentVersion)
                                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"algebra");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus1");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"geometry");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"other");
                                                                        databaseHandler.updatetestcontentsyncstatus(1,"calculus2");
                                                                        Log.d("HARI", "BGImplementReceiver = Meetings document added - id: " + documentReference.getId());
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        Log.w("HARI", "BGImplementReceiver = Error adding Meetings document", e);
                                                                    }
                                                                });


                                                    }


                                                }else{
                                                    String updatedate= sharedPrefs.getPrefVal(context,"contentupdatedate");
                                                    UserContentVersion userContentVersion = new UserContentVersion();
                                                    userContentVersion.setUseremail(usermail);
                                                    userContentVersion.setUserid(userid);
                                                    userContentVersion.setPhonenumber(phone);
                                                    userContentVersion.setUpdatedtime(updatedate);
                                                    List<TestDownload> testcontentlist = databaseHandler.gettestContent();
                                                    for(int i = 0; i<testcontentlist.size();i++) {
                                                        if (testcontentlist.get(i).getTesttype().equals("algebra")) {
                                                            userContentVersion.setAlgebraversion(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("calculus1")) {
                                                            userContentVersion.setCalculus1version(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("calculus2")) {
                                                            userContentVersion.setCalculus2version(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("geometry")) {
                                                            userContentVersion.setGeometryversion(testcontentlist.get(i).getTestversion());
                                                        }
                                                        if (testcontentlist.get(i).getTesttype().equals("other")) {
                                                            userContentVersion.setOtherversion(testcontentlist.get(i).getTestversion());
                                                        }


                                                    }
                                                    firestore.collection("usercontentversion")
                                                            .add(userContentVersion)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"algebra");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"calculus1");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"geometry");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"other");
                                                                    databaseHandler.updatetestcontentsyncstatus(1,"calculus2");
                                                                    Log.d("HARI", "BGImplementReceiver = Meetings document added - id: " + documentReference.getId());
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("HARI", "BGImplementReceiver = Error adding Meetings document", e);
                                                                }
                                                            });
                                                }
                                            }
                                        }
                                    });

                                }




                            }
                        }



                    }
                }, 1000);





            }
        }catch (Exception e){

        }

    }
}
