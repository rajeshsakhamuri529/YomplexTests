package com.yomplex.tests.activity

import android.Manifest
import android.annotation.TargetApi

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Environment

import androidx.core.app.ActivityCompat

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast



import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.yomplex.tests.BuildConfig
import com.yomplex.tests.R
import com.yomplex.tests.Service.ContentDownloadService
import com.yomplex.tests.Service.JobService
import com.yomplex.tests.adapter.GradeAdapter
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.interfaces.GradeClickListener
import com.yomplex.tests.model.Course
import com.yomplex.tests.model.GradeResponseModel
import com.yomplex.tests.model.RevisionModel
import com.yomplex.tests.model.TestDownload
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.ConstantPath.*
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.activity_grade.*

import org.apache.commons.io.FileUtils
import java.io.*
import java.lang.ref.WeakReference
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import kotlin.collections.ArrayList

class GradeActivity : BaseActivity(), GradeClickListener, PermissionListener  {


    fun redirectStore(updateUrl : String) {
        var intent = Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent)
        finish()
    }
    private val SUBSCRIPTION_END_DATE = "subscriptionenddate"
    private val TEST_CONTENT_URL = "TestContentUrl"
    private val TEST_CONTENT_VERSION = "TestContentVersion"
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //val remoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    val storage = FirebaseStorage.getInstance()
    var gradeResponseModelList: ArrayList<GradeResponseModel>?= null
    var gradeVersion: Long?= null
    private val sharedPrefs = SharedPrefs()
    var isBtnIconDownloaded: Boolean = false
    var isIconDownloaded: Boolean = false
    private var listJson: String?= null
    private var auth: FirebaseAuth?= null
    private var mSnackBar: Snackbar? = null
    private var revisionItemList:ArrayList<RevisionModel>?=null
    var revisionModel:RevisionModel? = null

    var databaseHandler: QuizGameDataBase?= null
    var version : String = ""
    var url : String = ""

    var remoteConfig = Firebase.remoteConfig
    override var layoutID: Int = R.layout.activity_splash


    override fun initView() {

        databaseHandler = QuizGameDataBase(this);
        auth = FirebaseAuth.getInstance()

        try{
            val settings = FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                    .build()
            db.firestoreSettings = settings
        }catch (e:Exception){

        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorbottomnav));
        }
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        /*val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()

        remoteConfig!!.setConfigSettings(configSettings)*/

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchVersion()
        //firebaseAnalytics.setCurrentScreen(this, "Signup", null /* class override */)

        TedPermission.with(this)
            .setPermissionListener(this)
            .setDeniedMessage("If you reject permission,you can not use this service\n"
                    + "\nPlease turn on permissions at [Setting] > [Permission]")
            .setPermissions(Manifest.permission.INTERNET)
            .check()
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("grade actvity", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                var token:String = task.result.token
                Log.e("grade activity","token...."+token);
                sharedPrefs?.setPrefVal(this@GradeActivity, "firebasetoken", token)
            })

    }

    fun fetchVersion(){
        /*var cacheExpiration: Long = 3600 // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (remoteConfig!!.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0
        }*/

        /*remoteConfig!!.fetch(cacheExpiration)
            .addOnCompleteListener(this, OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    remoteConfig!!.activateFetched()
                }
                displayUpdateAlert()
            })*/


        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.e("grade activity", "Config params updated: $updated")
                   // Toast.makeText(this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(this, "Fetch failed", Toast.LENGTH_SHORT).show()
                }
                displayUpdateAlert()
            }


    }

    fun displayUpdateAlert() {

        val enddate = remoteConfig!!.getString(SUBSCRIPTION_END_DATE)
        sharedPrefs!!.setPrefVal(this,"enddate", enddate)
        //Toast.makeText(this,"displayUpdateAlert..."+enddate,Toast.LENGTH_LONG).show()
        /*val url = remoteConfig!!.getString(TEST_CONTENT_URL)
        val version = remoteConfig!!.getString(TEST_CONTENT_VERSION)
        Log.e("grade activity","displayUpdateAlert.....version......."+version)
        Log.e("grade activity","displayUpdateAlert......url......."+url)*/

       // databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,0)*/
    }

    override fun onPermissionGranted() {
        //navigateToIntro()
        signin(sharedPrefs)
    }

    override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {

    }

    override fun onResume() {
        super.onResume()
        listJson = sharedPrefs.getPrefVal(this, GRADE_LIST)
        gradeVersion = sharedPrefs.getLongPrefVal(this, GRADE_VERSION)
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 112 ) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                var url = databaseHandler!!.gettesttopicurl()
                var version = databaseHandler!!.gettesttopicversion()
                downloadDataFromBackground(this@GradeActivity,url,version,"")
                if(sharedPrefs.getBooleanPrefVal(this, ConstantPath.IS_FIRST_TIME))
                {
                    navigateToIntro()
                }else if(!sharedPrefs.getBooleanPrefVal(this, ISNOTLOGIN)){
                    navigateToIntro()
                }else{
                    navigateToDashboard("GRADE 6")
                }


            }else {
                Toast.makeText(this,"Permissions are required to view the file!",Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun downloadDataFromBackground(
        mainActivity: GradeActivity,
        url: String,version:String,type:String
    ) {
        JobService.enqueueWork(mainActivity, url,version,type)
    }

    private fun downloadServiceFromBackground(
        mainActivity: GradeActivity,db: FirebaseFirestore
    ) {
        ContentDownloadService.enqueueWork(mainActivity, db)
    }


    @TargetApi(Build.VERSION_CODES.M)
    private fun signin(sharedPrefs: SharedPrefs) {
        if (sharedPrefs.getBooleanPrefVal(this, ConstantPath.IS_LOGGED_IN)) {
            //val uid : String = sharedPrefs.getPrefVal(this, ConstantPath.UID)!!

            Log.d("signin","true")

            if(sharedPrefs.getBooleanPrefVal(this, ConstantPath.IS_FIRST_TIME)){

                /*val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                val isConnected: Boolean = activeNetwork?.isConnected == true
                Log.d("isConnected",isConnected.toString()+"!")
                if(isNetworkConnected()) {
                    downloadServiceFromBackground(this@GradeActivity,db)
                }*/
                navigateToIntro()

                /*var statuslist: List<Int>? = databaseHandler!!.gettesttopicdownloadstatus()

                Log.e("signin","statuslist.size........"+statuslist!!.size)

                    if(statuslist.contains(0)){
                        Log.e("signin","statuslist.contains.....")
                        var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                        for(i in 0 until testcontentlist!!.size){
                            if(testcontentlist.get(i).testdownloadstatus == 0){
                                var url = testcontentlist.get(i).testurl
                                var version = testcontentlist.get(i).testversion
                                var type = testcontentlist.get(i).testtype
                                downloadDataFromBackground(this@GradeActivity,url,version,type)

                            }
                        }

                        navigateToIntro()

                    }else{
                        Log.e("signin","statuslist........"+statuslist)
                        navigateToIntro()
                    }*/

            }else{

                Log.e("grade activity","sharedPrefs.getBooleanPrefVal(this, ISNOTLOGIN)...."+sharedPrefs.getBooleanPrefVal(this, ISNOTLOGIN))
                /*val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                val isConnected: Boolean = activeNetwork?.isConnected == true
                Log.d("isConnected",isConnected.toString()+"!")
                if(isNetworkConnected()) {
                    downloadServiceFromBackground(this@GradeActivity,db)
                }*/
                navigateToDashboard("GRADE 6")
                /*if(!sharedPrefs.getBooleanPrefVal(this, ISNOTLOGIN)){
                    //this block is for not sign in users
                        val docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4")
                        docRef.get().addOnSuccessListener { document ->
                            if (document != null) {
                                Log.e("grade activity", "DocumentSnapshot data: ${document.data}")
                                for(i in 0 until (document.data!!.size - 4)){
                                    if(i == 0){
                                        var version = document.data!!.get("BasicVersion").toString()
                                        var url = document.data!!.get("BasicUrl").toString()

                                        Log.e("grade activity","version......."+version)
                                        Log.e("grade activity","url......."+url)

                                        databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"basic",0)
                                        downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                    }else if(i == 1){
                                        var version = document.data!!.get("AlgebraVersion").toString()
                                        var url = document.data!!.get("AlgebraUrl").toString()

                                        Log.e("grade activity","version......."+version)
                                        Log.e("grade activity","url......."+url)

                                        databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"algebra",0)
                                        downloadDataFromBackground(this@GradeActivity,url,version,"algebra")
                                    }else if(i == 2){
                                        var version = document.data!!.get("CalculusVersion").toString()
                                        var url = document.data!!.get("CalculusUrl").toString()

                                        Log.e("grade activity","version......."+version)
                                        Log.e("grade activity","url......."+url)

                                        databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"calculus",0)
                                        downloadDataFromBackground(this@GradeActivity,url,version,"calculus")
                                    }else if(i == 3){
                                        var version = document.data!!.get("GeometryVersion").toString()
                                        var url = document.data!!.get("GeometryUrl").toString()

                                        Log.e("grade activity","version......."+version)
                                        Log.e("grade activity","url......."+url)

                                        databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"geometry",0)
                                        downloadDataFromBackground(this@GradeActivity,url,version,"geometry")
                                    }
                                }
                                *//*version = document.data!!.get("TestContentVersion").toString()
                                url = document.data!!.get("TestContentUrl").toString()

                                Log.e("grade activity","version......."+version)
                                Log.e("grade activity","url......."+url)

                                databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"",0)*//*


                                navigateToIntro()

                            } else {
                                Log.e("grade activity", "No such document")
                                navigateToIntro()
                            }
                        }
                            .addOnFailureListener { exception ->
                                Log.e("grade activity", "get failed with ", exception)
                                navigateToIntro()

                            }

                }else{

                    //already login user block
                    var dbversion = databaseHandler!!.gettesttopicversion()
                    if(dbversion == null){
                        //this block is for already logged user but content is not downloded
                            val docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4")
                            docRef.get().addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.e("grade activity", "DocumentSnapshot data: ${document.data}")
                                    *//*version = document.data!!.get("TestContentVersion").toString()
                                    url = document.data!!.get("TestContentUrl").toString()

                                    Log.e("grade activity","version......."+version)
                                    Log.e("grade activity","url......."+url)

                                    databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"",0)

                                    downloadDataFromBackground(this@GradeActivity,url,version,"")*//*
                                    for(i in 0 until (document.data!!.size - 4)){
                                        if(i == 0){
                                            var version = document.data!!.get("BasicVersion").toString()
                                            var url = document.data!!.get("BasicUrl").toString()

                                            Log.e("grade activity","version......."+version)
                                            Log.e("grade activity","url......."+url)

                                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"basic",0)
                                            downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                        }else if(i == 1){
                                            var version = document.data!!.get("AlgebraVersion").toString()
                                            var url = document.data!!.get("AlgebraUrl").toString()

                                            Log.e("grade activity","version......."+version)
                                            Log.e("grade activity","url......."+url)

                                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"algebra",0)
                                            downloadDataFromBackground(this@GradeActivity,url,version,"algebra")
                                        }else if(i == 2){
                                            var version = document.data!!.get("CalculusVersion").toString()
                                            var url = document.data!!.get("CalculusUrl").toString()

                                            Log.e("grade activity","version......."+version)
                                            Log.e("grade activity","url......."+url)

                                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"calculus",0)
                                            downloadDataFromBackground(this@GradeActivity,url,version,"calculus")
                                        }else if(i == 3){
                                            var version = document.data!!.get("GeometryVersion").toString()
                                            var url = document.data!!.get("GeometryUrl").toString()

                                            Log.e("grade activity","version......."+version)
                                            Log.e("grade activity","url......."+url)

                                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"geometry",0)
                                            downloadDataFromBackground(this@GradeActivity,url,version,"geometry")
                                        }
                                    }
                                    navigateToDashboard("GRADE 6")

                                } else {
                                    Log.e("grade activity", "No such document")
                                    navigateToDashboard("GRADE 6")

                                }
                            }
                                .addOnFailureListener { exception ->
                                    Log.e("grade activity", "get failed with ", exception)
                                    navigateToDashboard("GRADE 6")

                                }


                    }else{
                       //this block is for already logged user but version checking for content downloaded
                            if(Utils.isOnline(this@GradeActivity)){

                                val docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4")
                                docRef.get().addOnSuccessListener { document ->
                                        if (document != null) {
                                            *//*Log.e("grade activity", "DocumentSnapshot data: ${document.data}")
                                            version = document.data!!.get("TestContentVersion").toString()
                                            url = document.data!!.get("TestContentUrl").toString()

                                            Log.e("grade activity","version......."+version)
                                            Log.e("grade activity","url......."+url)*//*
                                            var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                            for(i in 0 until (document.data!!.size - 4)){
                                                if(i == 0){
                                                    var version = document.data!!.get("BasicVersion").toString()
                                                    var url = document.data!!.get("BasicUrl").toString()

                                                    Log.e("grade activity","version......."+version)
                                                    Log.e("grade activity","url......."+url)
                                                    for(j in 0 until testcontentlist!!.size){
                                                        if(testcontentlist.get(j).testtype.equals("basic")){
                                                            if(testcontentlist.get(j).testversion != version) {
                                                                downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                                            }
                                                            break
                                                        }
                                                    }

                                                    //databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"basic",0)
                                                    //downloadDataFromBackground(this@GradeActivity,url,version,"basic")
                                                }else if(i == 1){
                                                    var version = document.data!!.get("AlgebraVersion").toString()
                                                    var url = document.data!!.get("AlgebraUrl").toString()

                                                    Log.e("grade activity","version......."+version)
                                                    Log.e("grade activity","url......."+url)

                                                    for(j in 0 until testcontentlist!!.size){
                                                        if(testcontentlist.get(j).testtype.equals("algebra")){
                                                            if(testcontentlist.get(j).testversion != version) {
                                                                downloadDataFromBackground(this@GradeActivity,url,version,"algebra")
                                                            }
                                                            break
                                                        }
                                                    }

                                                   // databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"algebra",0)
                                                   // downloadDataFromBackground(this@GradeActivity,url,version,"algebra")
                                                }else if(i == 2){
                                                    var version = document.data!!.get("CalculusVersion").toString()
                                                    var url = document.data!!.get("CalculusUrl").toString()

                                                    Log.e("grade activity","version......."+version)
                                                    Log.e("grade activity","url......."+url)
                                                    for(j in 0 until testcontentlist!!.size){
                                                        if(testcontentlist.get(j).testtype.equals("calculus")){
                                                            if(testcontentlist.get(j).testversion != version) {
                                                                downloadDataFromBackground(this@GradeActivity,url,version,"calculus")
                                                            }
                                                            break
                                                        }
                                                    }

                                                   // databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"calculus",0)
                                                   // downloadDataFromBackground(this@GradeActivity,url,version,"calculus")
                                                }else if(i == 3){
                                                    var version = document.data!!.get("GeometryVersion").toString()
                                                    var url = document.data!!.get("GeometryUrl").toString()

                                                    Log.e("grade activity","version......."+version)
                                                    Log.e("grade activity","url......."+url)
                                                    for(j in 0 until testcontentlist!!.size){
                                                        if(testcontentlist.get(j).testtype.equals("geometry")){
                                                            if(testcontentlist.get(j).testversion != version) {
                                                                downloadDataFromBackground(this@GradeActivity,url,version,"geometry")
                                                            }
                                                            break
                                                        }
                                                    }
                                                  //  databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"geometry",0)
                                                  //  downloadDataFromBackground(this@GradeActivity,url,version,"geometry")
                                                }
                                            }

                                            navigateToDashboard("GRADE 6")
                                           *//* var dbversion = databaseHandler!!.gettesttopicversion()
                                            if(dbversion != version) {

                                                downloadDataFromBackground(this@GradeActivity,url,version,"")
                                                navigateToDashboard("GRADE 6")

                                            }else{

                                                navigateToDashboard("GRADE 6")
                                            }*//*

                                        } else {
                                            Log.e("grade activity", "No such document")
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("grade activity", "get failed with ", exception)
                                        navigateToDashboard("GRADE 6")

                                    }

                        }else {
                                Log.e("grade activity","user offline..........")
                                navigateToDashboard("GRADE 6")
                            }

                    }


                }*/


            }

        } else {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            Log.d("isConnected",isConnected.toString()+"!")
           /* if(isNetworkConnected()) {
                downloadServiceFromBackground(this@GradeActivity,db)
            }*/
            sharedPrefs.setBooleanPrefVal(this@GradeActivity, ConstantPath.IS_LOGGED_IN, true)
            sharedPrefs.setBooleanPrefVal(this@GradeActivity, ConstantPath.IS_FIRST_TIME, true)


            Log.d("anonymous auth done","true")
            TedPermission.with(this@GradeActivity)
                .setPermissionListener(this@GradeActivity)
                .setDeniedMessage(
                    "If you reject permission,you can not use this service\n"
                            + "\nPlease turn on permissions at [Setting] > [Permission]"
                )
                .setPermissions(Manifest.permission.INTERNET)
                .check()

            /*if(isNetworkConnected()) {

                Log.e("grade activity", "task is successful.............")
                val docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4")
                docRef.get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            Log.e("grade activity", "DocumentSnapshot data: ${document.data}")
                            Log.e("grade activity", "DocumentSnapshot data: ${document.data!!.size}")
                            for(i in 0 until (document.data!!.size - 4)){
                                if(i == 0){
                                    var version = document.data!!.get("BasicVersion").toString()
                                    var url = document.data!!.get("BasicUrl").toString()

                                    Log.e("grade activity","version......."+version)
                                    Log.e("grade activity","url......."+url)

                                    databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"basic",0)
                                }else if(i == 1){
                                    var version = document.data!!.get("AlgebraVersion").toString()
                                    var url = document.data!!.get("AlgebraUrl").toString()

                                    Log.e("grade activity","version......."+version)
                                    Log.e("grade activity","url......."+url)

                                    databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"algebra",0)
                                }else if(i == 2){
                                    var version = document.data!!.get("CalculusVersion").toString()
                                    var url = document.data!!.get("CalculusUrl").toString()

                                    Log.e("grade activity","version......."+version)
                                    Log.e("grade activity","url......."+url)

                                    databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"calculus",0)
                                }else if(i == 3){
                                    var version = document.data!!.get("GeometryVersion").toString()
                                    var url = document.data!!.get("GeometryUrl").toString()

                                    Log.e("grade activity","version......."+version)
                                    Log.e("grade activity","url......."+url)

                                    databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"geometry",0)
                                }
                            }



                            sharedPrefs.setBooleanPrefVal(this@GradeActivity, ConstantPath.IS_LOGGED_IN, true)
                            sharedPrefs.setBooleanPrefVal(this@GradeActivity, ConstantPath.IS_FIRST_TIME, true)


                            Log.d("anonymous auth done","true")
                            TedPermission.with(this@GradeActivity)
                                .setPermissionListener(this@GradeActivity)
                                .setDeniedMessage(
                                    "If you reject permission,you can not use this service\n"
                                            + "\nPlease turn on permissions at [Setting] > [Permission]"
                                )
                                .setPermissions(Manifest.permission.INTERNET)
                                .check()



                        } else {
                            Log.e("grade activity", "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("grade activity", "get failed with ", exception)
                        Toast.makeText(this@GradeActivity,"Internet is required!",Toast.LENGTH_LONG).show();

                    }

            }else{
                mSnackBar = Snackbar.make(
                    findViewById(R.id.splash_cl),
                    "No Internet Connection",
                    Snackbar.LENGTH_LONG
                ) //Assume "rootLayout" as the root layout of every activity.
                mSnackBar?.duration = BaseTransientBottomBar.LENGTH_INDEFINITE
                mSnackBar?.setAction("Retry", { signin(sharedPrefs) })
                mSnackBar?.show()
            }*/
        }
    }

    private fun setLocalData(gson: Gson){
        /*val pathStringList: ArrayList<String> = ArrayList()
        for (imagePath in Utils.getListOfFilesFromFolder(ConstantPath.loaclAstraCardPath)){
            if (imagePath.contains("png")){
                pathStringList.add(imagePath)
            }
        }
        *//*Collections.sort(pathStringList)*//*
        reverseListString(pathStringList)
        pathStringList.forEachIndexed { index, s ->
            Log.e("imagename: ", s)
        }*/
        val type = object : TypeToken<List<GradeResponseModel>>() {}.type
        gradeResponseModelList = gson.fromJson(listJson, type)
        rcv_grade.adapter = GradeAdapter(this,
            this, gradeResponseModelList!!)
    }

    private fun getdataFromFirestore(){
        Log.d("getDataFromFirestore","YES");
        remoteConfig.fetch().addOnCompleteListener(object : OnCompleteListener<Void>{
            override fun onComplete(task: Task<Void>) {
                if (task.isSuccessful){
                    //remoteConfig.activateFetched()
                    gradeVersion = remoteConfig.getValue("gradesVer").asLong()
                    Log.d("getDataFromFirestore",gradeVersion.toString()+"!");

                }
            }

        })
        progress_bar.visibility = View.VISIBLE
        db.collection("courses").get()
            .addOnCompleteListener(object : OnCompleteListener<QuerySnapshot>{
                override fun onComplete(task: Task<QuerySnapshot>) {
                    if (task.isSuccessful){
                        gradeResponseModelList = ArrayList()
                        for (document in task.getResult()!!){
                            val gradeMap = document.data
                            val gradeResponseModel = GradeResponseModel()
                            gradeResponseModel.active = gradeMap.get("active") as Boolean?
                            gradeResponseModel.disPos = gradeMap.get("disPos") as Long?
                            gradeResponseModel.btnicon = gradeMap.get("btnicon") as Map<String, String>?
                            gradeResponseModel.icon = gradeMap.get("icon") as Map<String, String>?
                            gradeResponseModel.link = gradeMap.get("link") as String?
                            gradeResponseModel.title = gradeMap.get("title") as String?
                            gradeResponseModel.version = gradeMap.get("version") as Long?

                            gradeResponseModelList!!.add(gradeResponseModel)
                        }
                        reverseList(gradeResponseModelList!!)

                        gradeResponseModelList!!.forEachIndexed { index, grade ->
                            val btnStorageRef = storage
                                .getReferenceFromUrl(grade.btnicon!!.get("mdpi")!!)
                            val iconStorageRef = storage
                                .getReferenceFromUrl(grade.icon!!.get("mdpi")!!)

                            val root = Environment.getExternalStorageDirectory().toString()
                            val btnName = "${grade.title}btn.png"
                            val iconName = "${grade.title}icon.png"
                            val btnPath = "$root/blobcity/images/$btnName"
                            val iconPath = "$root/blobcity/images/$iconName"

                            val btnFile = File(btnPath)
                            val iconFile = File(iconPath)

                            downloadImages(btnStorageRef, btnFile, index,
                                false, true)
                            downloadImages(iconStorageRef, iconFile, index,
                                true, false)
                            grade.iconPath = iconPath
                            grade.btnPath = btnPath
                        }

                    }else{
                        Log.e("data failure: ", task.exception.toString())
                    }
                }
            })
    }

    private fun downloadImages(storageRefs: StorageReference,
                               file: File,
                               index: Int,
                               isIconDownload: Boolean,
                               isBtnDownload: Boolean){
        storageRefs.getFile(file)
            .addOnCompleteListener(object : OnCompleteListener<FileDownloadTask.TaskSnapshot>{
                override fun onComplete(task: Task<FileDownloadTask.TaskSnapshot>) {
                    if (task.isSuccessful){
                        if (isIconDownload == true) {
                            if ((gradeResponseModelList!!.size - 1) == index) {
                                isIconDownloaded = true
                                setAdapter()
                            }
                        }
                        if (isBtnDownload == true) {
                            if ((gradeResponseModelList!!.size - 1) == index) {
                                isBtnIconDownloaded = true
                                setAdapter()
                            }
                        }
                    }
                    else{
                        isBtnIconDownloaded = false
                        isIconDownloaded = false
                        progress_bar.visibility = View.INVISIBLE
                        Log.e("data failure: ", task.exception.toString())
                    }
                }
            })
    }

    private fun setAdapter(){
        if (isBtnIconDownloaded && isIconDownloaded) {
            progress_bar.visibility = View.INVISIBLE
            sharedPrefs.setLongPrefVal(this, GRADE_VERSION, gradeVersion!!)
            val gson = Gson()
            val listJson = gson.toJson(gradeResponseModelList!!)
            sharedPrefs.setPrefVal(this, GRADE_LIST, listJson)
            rcv_grade.adapter = GradeAdapter(this, this, gradeResponseModelList!!)
        }
    }

    private fun reverseListString(pathStrings: List<String>): List<String> {
        Collections.sort(pathStrings) { pathStringList, t1 ->
            val nextPos = t1
            val currentPos = pathStringList
            currentPos!!.compareTo(nextPos!!)
        }
        return pathStrings
    }

    private fun reverseList(gradeResponseModelList: List<GradeResponseModel>)
            : List<GradeResponseModel> {
        Collections.sort(gradeResponseModelList) { gradeModel, t1 ->
            val nextPos = t1.disPos
            val currentPos = gradeModel.disPos
            currentPos!!.compareTo(nextPos!!)
        }
        return gradeResponseModelList
    }

    override fun click(link: String, title: String) {
        val gradeSharedPrefs = sharedPrefs.getPrefVal(this, title)
        if (!TextUtils.isEmpty(gradeSharedPrefs)) {
            if (isNetworkConnected()){
                remoteConfig.fetch().addOnCompleteListener(object : OnCompleteListener<Void> {
                    override fun onComplete(task: Task<Void>) {
                        if (task.isSuccessful) {
                            if (gradeVersion != remoteConfig.getLong("gradesVer")) {
                                downloadFolder(link, title)
                            }else{
                                navigateToDashboard(title)
                            }
                        }
                    }
                })
            }else{
                navigateToDashboard(title)
            }
        }else{
            if (isNetworkConnected()) {
                downloadFolder(link, title)
            }
        }

        /*val connManager : ConnectivityManager= getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mWifi : NetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val network : NetworkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            if (mWifi.isConnected()) {
                // Do whatever
                Log.e("wifi", "connected")
            }else{
                Log.e("wifi", "not connected")
            }
            if (network.isConnected()) {
                // Do whatever
                Log.e("network", "connected")
            }else{
                Log.e("network", "not connected")
            }*/
    }

    private fun downloadFolder(link: String, title: String){

        val folderStorageRef = storage
            .getReferenceFromUrl(link)
        val root = Environment.getExternalStorageDirectory().toString()
        val fileName = link.substring(link.lastIndexOf('/') + 1)
        val filePath = "$root/blobcity/$fileName"
        Log.e("fileName:", fileName)
        val file = File(filePath)
        progress_bar.visibility = View.VISIBLE
        folderStorageRef.getFile(file)
            .addOnCompleteListener(object : OnCompleteListener<FileDownloadTask.TaskSnapshot> {
                override fun onComplete(task: Task<FileDownloadTask.TaskSnapshot>) {
                    if (task.isSuccessful) {
                        Utils.unpackZip("$root/blobcity/", fileName)
                        progress_bar.visibility = View.INVISIBLE
                        sharedPrefs.setPrefVal(this@GradeActivity, title, title)
                        navigateToDashboard(title)
                    }
                }
            })
    }

    fun navigateToSignIn(){
        val intent = Intent(
            this@GradeActivity,
            SignInActivity::class.java
        )
        intent.putExtra(TITLE_TOPIC, title)
        intent.putExtra(FIRST_TIME, "first time")
        startActivity(intent)
    }
    fun navigateToIntro(){
        try{
            var testtypeslist: ArrayList<String>? = null
            testtypeslist = ArrayList<String>()


            testtypeslist!!.add("ALGEBRA")
            testtypeslist!!.add("GEOMETRY")
            testtypeslist!!.add("CALCULUS 1")
            testtypeslist!!.add("CALCULUS 2")
            testtypeslist!!.add("OTHER")
            var count = databaseHandler!!.getAllCoursesCount()
            if(count == 0){
                for(i in 0 until testtypeslist.size){
                    databaseHandler!!.insertCourse(Course(testtypeslist.get(i),0))
                }
            }
        }catch (e:Exception){

        }



        val intent = Intent(
            this@GradeActivity,
            SignInActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(TITLE_TOPIC, title)
        intent.putExtra(FIRST_TIME, "first time")
        startActivity(intent)
        finish()
    }
    fun navigateToDashboard(title: String){
        Log.e("grade activity","navigateToDashboard...title.."+title)
        try {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            Log.d("isConnected",isConnected.toString()+"!")
            if(isNetworkConnected()) {
                downloadServiceFromBackground(this@GradeActivity,db)
            }
            val intent = Intent(
                this@GradeActivity,
                DashBoardActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(TITLE_TOPIC, title)
            startActivity(intent)
            finish()
        }catch (e:Exception){
            val intent = Intent(
                this@GradeActivity,
                DashBoardActivity::class.java
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra(TITLE_TOPIC, title)
            startActivity(intent)
            finish()
        }


    }
}