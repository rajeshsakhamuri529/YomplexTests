package com.yomplex.tests.activity


import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.common.reflect.TypeToken
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.yomplex.tests.R
import com.yomplex.tests.Service.BooksDownloadService
import com.yomplex.tests.Service.CopyService
import com.yomplex.tests.Service.JobService
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.fragment.BooksFragment
import com.yomplex.tests.fragment.ReviewFragment
import com.yomplex.tests.fragment.SettingFragment
import com.yomplex.tests.fragment.TestsFragment
import com.yomplex.tests.model.Books
import com.yomplex.tests.model.PlayCount
import com.yomplex.tests.model.TestDownload
import com.yomplex.tests.model.UserContentVersion
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.ConstantPath.TITLE_TOPIC
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import com.yomplex.tests.utils.Utils.getPlayer
import kotlinx.android.synthetic.main.activity_dashboard.*
import java.io.*
import java.util.*


class DashBoardActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private var fragment: Fragment? = null
    lateinit var gradeTitle: String
    private var backPressedTime: Long = 0
    private var backPressToastMessage: Toast? = null
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    var action:String = ""
    var data:String = ""
    var currentVersion:Int = 0
    var databaseHandler: QuizGameDataBase?= null
    // Remote Config keys
    private val VERSION_CODE_CONFIG_KEY = "upgrade"
    private val FEEDBACK_CONFIG_KEY = "feedback"
    private val WRITETOUS_CONFIG_KEY = "writetous"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //var remoteConfig: FirebaseRemoteConfig? = null
    var remoteConfig = Firebase.remoteConfig
    var version : String = ""
    var url : String = ""
    var mTracker: Tracker? = null
    private var sAnalytics: GoogleAnalytics? = null
    private var auth: FirebaseAuth?= null

    override var layoutID: Int = R.layout.activity_dashboard
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }
    override fun initView() {
       // getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        gradeTitle = "GRADE 6"
        getPlayer(this)
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        try{
            db.firestoreSettings = settings
        }catch (e:Exception){

        }
        sharedPrefs = SharedPrefs()
        databaseHandler = QuizGameDataBase(this);

        try{
            var status = databaseHandler!!.getUserSyncStatus()
            if(status.equals("0")){
                var userObj = databaseHandler!!.getUserDetails()

                db!!.collection("users")
                    .add(userObj)
                    .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                        override fun onComplete(task: Task<DocumentReference>) {
                            if (task.isSuccessful) {
                                Log.e("user", "user added successfully")
                                databaseHandler!!.updateUserSyncStatus(userObj.username,1)
                            } else {
                                Log.e("user", task.exception.toString())
                            }
                        }
                    })
            }
        }catch (e:Exception){

        }

        try {
            val mSensorService = ContentVersionUpdateService()
            val mServiceIntent = Intent(this, mSensorService::class.java)
            if (!Utils.isMyServiceRunning(this, mSensorService::class.java)) {
                startService(mServiceIntent)
            }
        } catch (e: Exception) {

        }

        //for adding books into database
        try{

            var statuslist = databaseHandler!!.getbookscopystatusList()
            if(statuslist.size > 0){
                if(statuslist.contains(0)){
                    //startcopyService(this@SplashActivity)
                }else{
                    try{
                        val booksJsonString = Utils.loadJSONFromAsset(this@DashBoardActivity, "books.json")
                        Log.e("dash board", "booksJsonString...$booksJsonString")
                        val gsonFile = Gson()
                        val userListType = object : com.google.gson.reflect.TypeToken<ArrayList<Books?>?>() {}.type
                        val booksCountmodel = gsonFile.fromJson<ArrayList<Books>>(booksJsonString, userListType)

                        for (i in 0 until booksCountmodel.size) {
                            val booksCount = booksCountmodel[i]
                            val count = databaseHandler!!.getBooksCount(booksCount.id)
                            if (count == 0) {
                                Log.e("dash board", "booksJsonString...count.......$count")
                                databaseHandler!!.insertBooks(booksCount)
                                staryCopyAssetsToLocal(booksCount)
                            }else{
                                val dbversion = databaseHandler!!.getBooksVersion(booksCount.id)
                                Log.e("dash board", "dbversion.......$dbversion")
                                Log.e("dash board", "booksCount.version.......$booksCount.version")
                                if(dbversion < booksCount.version){
                                    Log.e("dash board", "dbversion.....else..$dbversion")
                                    staryCopyAssetsToLocal(booksCount)
                                }
                            }
                        }


                    }catch (e:Exception){

                    }





                }
            }else{
                //startcopyService(this@SplashActivity)
            }
           // downloadServiceFromBackground(this@DashBoardActivity,db)


            /*val booksJsonString = loadJSONFromAsset( "books.json")
           Log.e("dashboard","booksJsonString..."+booksJsonString);
               val gsonFile = Gson()
               val courseType = object : TypeToken<List<Books>>() {}.type
               val booksCountmodel: ArrayList<Books> = gsonFile
                   .fromJson(booksJsonString, courseType)
           Log.e("dashboard","booksJsonString...booksCountmodel..."+booksCountmodel.size);
               for(i in 0 until booksCountmodel.size){
                   val booksCount = booksCountmodel[i]
                   val count = databaseHandler!!.getBooksCount(booksCount.title, booksCount.category)
                   if (count == 0) {
                       Log.e("dash board","booksJsonString...count......."+count)
                       databaseHandler!!.insertBooks(booksCount)
                   }
               }*/
        }catch (e:Exception){

        }



        try{
            var syncstatuslist = databaseHandler!!.gettesttopicsyncstatus()

            var uid = sharedPrefs?.getPrefVal(this@DashBoardActivity,ConstantPath.UID)
            var mail = sharedPrefs?.getPrefVal(this@DashBoardActivity,"email")
            var phone = sharedPrefs?.getPrefVal(this@DashBoardActivity,"phonenumber")
            Log.e("dashboard","uid........."+uid)
            Log.e("dashboard","mail........."+mail)
            Log.e("dashboard","phone........."+phone)

            if(syncstatuslist.size > 0){
                if(syncstatuslist.contains(0)){

                    if(mail.equals("")){
                        val docRef = db!!.collection("usercontentversion")
                        docRef.whereEqualTo("phonenumber",phone).whereEqualTo("userid",uid)
                            .get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                                override fun onComplete(task: Task<QuerySnapshot>) {
                                    if (task.isSuccessful){
                                        if(task.getResult().size() > 0){

                                            if(task.getResult().size() == 1){
                                                task.getResult().forEachIndexed { index, document ->
                                                    Log.e("dashboard", "document id......" + document.id)
                                                    var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                                    var algversion = ""
                                                    var cal1version = ""
                                                    var cal2version = ""
                                                    var othversion = ""
                                                    var gemversion = ""
                                                    for(i in 0 until testcontentlist!!.size) {
                                                        if (testcontentlist.get(i).testtype.equals("algebra")) {
                                                            algversion = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("calculus1")) {
                                                            cal1version = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("calculus2")) {
                                                            cal2version = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("geometry")) {
                                                            gemversion = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("other")) {
                                                            othversion = (testcontentlist.get(i).testversion);
                                                        }


                                                    }



                                                    val data = hashMapOf("calculus1version" to cal1version,"algebraversion" to algversion,"calculus2version" to cal2version,"geometryversion" to gemversion,"otherversion" to othversion, "updatedtime" to FieldValue.serverTimestamp())

                                                    db!!.collection("usercontentversion").document(document.id)
                                                        .set(data, SetOptions.merge())

                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"algebra")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"calculus1")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"geometry")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"other")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"calculus2")

                                                }

                                            }else{
                                                for(i in 0 until task.getResult().size()) {
                                                    Log.e("dashboard","doc id......................"+task.getResult().documents.get(i).id)
                                                    db.collection("usercontentversion").document(task.getResult().documents.get(i).id)
                                                        .delete()
                                                }

                                                var userContentVersion = UserContentVersion()
                                                userContentVersion.setUseremail(mail);
                                                userContentVersion.setUserid(uid);
                                                userContentVersion.setPhonenumber(phone);
                                                var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                                for(i in 0 until testcontentlist!!.size) {
                                                    if (testcontentlist.get(i).testtype.equals("algebra")) {
                                                        userContentVersion.setAlgebraversion(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("calculus1")) {
                                                        userContentVersion.setCalculus1version(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("calculus2")) {
                                                        userContentVersion.setCalculus2version(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("geometry")) {
                                                        userContentVersion.setGeometryversion(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("other")) {
                                                        userContentVersion.setOtherversion(testcontentlist.get(i).testversion);
                                                    }


                                                }

                                                db!!.collection("usercontentversion")
                                                    .add(userContentVersion)
                                                    .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                                        override fun onComplete(task: Task<DocumentReference>) {
                                                            if (task.isSuccessful) {
                                                                Log.e("user", "user added successfully")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"algebra")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"calculus1")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"geometry")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"other")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"calculus2")


                                                            } else {
                                                                Log.e("user", task.exception.toString())
                                                            }
                                                        }
                                                    })


                                            }



                                        }else{
                                            var userContentVersion = UserContentVersion()
                                            userContentVersion.setUseremail(mail);
                                            userContentVersion.setUserid(uid);
                                            userContentVersion.setPhonenumber(phone);
                                            var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                            for(i in 0 until testcontentlist!!.size) {
                                                if (testcontentlist.get(i).testtype.equals("algebra")) {
                                                    userContentVersion.setAlgebraversion(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("calculus1")) {
                                                    userContentVersion.setCalculus1version(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("calculus2")) {
                                                    userContentVersion.setCalculus2version(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("geometry")) {
                                                    userContentVersion.setGeometryversion(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("other")) {
                                                    userContentVersion.setOtherversion(testcontentlist.get(i).testversion);
                                                }


                                            }

                                            db!!.collection("usercontentversion")
                                                .add(userContentVersion)
                                                .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                                    override fun onComplete(task: Task<DocumentReference>) {
                                                        if (task.isSuccessful) {
                                                            Log.e("user", "user added successfully")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"algebra")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"calculus1")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"geometry")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"other")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"calculus2")


                                                        } else {
                                                            Log.e("user", task.exception.toString())
                                                        }
                                                    }
                                                })


                                        }
                                    }
                                }
                            })
                    }else{
                        val docRef = db!!.collection("usercontentversion")
                        docRef.whereEqualTo("useremail",mail).whereEqualTo("userid",uid)
                            .get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                                override fun onComplete(task: Task<QuerySnapshot>) {
                                    if (task.isSuccessful){
                                        if(task.getResult().size() > 0){

                                            if(task.getResult().size() == 1){
                                                task.getResult().forEachIndexed { index, document ->
                                                    Log.e("dashboard", "document id......" + document.id)
                                                    var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                                    var algversion = ""
                                                    var cal1version = ""
                                                    var cal2version = ""
                                                    var othversion = ""
                                                    var gemversion = ""
                                                    for(i in 0 until testcontentlist!!.size) {
                                                        if (testcontentlist.get(i).testtype.equals("algebra")) {
                                                            algversion = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("calculus1")) {
                                                            cal1version = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("calculus2")) {
                                                            cal2version = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("geometry")) {
                                                            gemversion = (testcontentlist.get(i).testversion);
                                                        }
                                                        if (testcontentlist.get(i).testtype.equals("other")) {
                                                            othversion = (testcontentlist.get(i).testversion);
                                                        }


                                                    }



                                                    val data = hashMapOf("calculus1version" to cal1version,"algebraversion" to algversion,"calculus2version" to cal2version,"geometryversion" to gemversion,"otherversion" to othversion, "updatedtime" to FieldValue.serverTimestamp())

                                                    db!!.collection("usercontentversion").document(document.id)
                                                        .set(data, SetOptions.merge())

                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"algebra")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"calculus1")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"geometry")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"other")
                                                    databaseHandler!!.updatetestcontentsyncstatus(1,"calculus2")

                                                }

                                            }else{
                                                for(i in 0 until task.getResult().size()) {
                                                    db.collection("usercontentversion").document(task.getResult().documents.get(i).id)
                                                        .delete()
                                                }

                                                var userContentVersion = UserContentVersion()
                                                userContentVersion.setUseremail(mail);
                                                userContentVersion.setUserid(uid);
                                                userContentVersion.setPhonenumber(phone);
                                                var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                                for(i in 0 until testcontentlist!!.size) {
                                                    if (testcontentlist.get(i).testtype.equals("algebra")) {
                                                        userContentVersion.setAlgebraversion(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("calculus1")) {
                                                        userContentVersion.setCalculus1version(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("calculus2")) {
                                                        userContentVersion.setCalculus2version(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("geometry")) {
                                                        userContentVersion.setGeometryversion(testcontentlist.get(i).testversion);
                                                    }
                                                    if (testcontentlist.get(i).testtype.equals("other")) {
                                                        userContentVersion.setOtherversion(testcontentlist.get(i).testversion);
                                                    }


                                                }

                                                db!!.collection("usercontentversion")
                                                    .add(userContentVersion)
                                                    .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                                        override fun onComplete(task: Task<DocumentReference>) {
                                                            if (task.isSuccessful) {
                                                                Log.e("user", "user added successfully")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"algebra")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"calculus1")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"geometry")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"other")
                                                                databaseHandler!!.updatetestcontentsyncstatus(1,"calculus2")


                                                            } else {
                                                                Log.e("user", task.exception.toString())
                                                            }
                                                        }
                                                    })


                                            }



                                        }else{
                                            var userContentVersion = UserContentVersion()
                                            userContentVersion.setUseremail(mail);
                                            userContentVersion.setUserid(uid);
                                            userContentVersion.setPhonenumber(phone);
                                            var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                                            for(i in 0 until testcontentlist!!.size) {
                                                if (testcontentlist.get(i).testtype.equals("algebra")) {
                                                    userContentVersion.setAlgebraversion(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("calculus1")) {
                                                    userContentVersion.setCalculus1version(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("calculus2")) {
                                                    userContentVersion.setCalculus2version(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("geometry")) {
                                                    userContentVersion.setGeometryversion(testcontentlist.get(i).testversion);
                                                }
                                                if (testcontentlist.get(i).testtype.equals("other")) {
                                                    userContentVersion.setOtherversion(testcontentlist.get(i).testversion);
                                                }


                                            }

                                            db!!.collection("usercontentversion")
                                                .add(userContentVersion)
                                                .addOnCompleteListener(object : OnCompleteListener<DocumentReference> {
                                                    override fun onComplete(task: Task<DocumentReference>) {
                                                        if (task.isSuccessful) {
                                                            Log.e("user", "user added successfully")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"algebra")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"calculus1")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"geometry")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"other")
                                                            databaseHandler!!.updatetestcontentsyncstatus(1,"calculus2")


                                                        } else {
                                                            Log.e("user", task.exception.toString())
                                                        }
                                                    }
                                                })


                                        }
                                    }
                                }
                            })
                    }



                }
            }

        } catch (e:Exception){

        }



        try{



            val allcount = databaseHandler!!.getAllPlayCount()
            Log.e("dashboard","allcount......"+allcount);
            if(allcount == 0){
                val courseJsonString = loadJSONFromAsset( "algebra/ii-algebra/" + "coursetestinfo.json")
                val gsonFile = Gson()
                val courseType = object : TypeToken<List<PlayCount>>() {}.type
                val playCountmodel: ArrayList<PlayCount> = gsonFile
                    .fromJson(courseJsonString, courseType)
                for(i in 0 until playCountmodel.size){
                    val playCount = playCountmodel[i]
                    val count = databaseHandler!!.getPlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                    if (count == 0) {
                        Log.e("dash board","count......."+count)
                        databaseHandler!!.insertPlayCount(playCount)
                    }
                }

                try{
                    val courseJsonStr = loadJSONFromAsset( "algebra/ii-algebra/" + "coursetestdelete.json")
                    //val gsonFile = Gson()
                    // val courseType = object : TypeToken<List<PlayCount>>() {}.type
                    val playCountmodelDelete: ArrayList<PlayCount> = gsonFile
                        .fromJson(courseJsonStr, courseType)

                    Log.e("sign in","playCountmodelDelete..........."+playCountmodelDelete);
                    Log.e("sign in","playCountmodelDelete.size.........."+playCountmodelDelete.size);
                    if(playCountmodelDelete.size > 0){
                        for(i in 0 until playCountmodelDelete.size){
                            val playCount = playCountmodelDelete[i]
                            val count = databaseHandler!!.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())

                        }

                    }
                }catch (e:Exception){

                }


                val courseJsonString1 = loadJSONFromAsset( "calculus1/jee-calculus-1/" + "coursetestinfo.json")
                //val gsonFile1 = Gson()
                //val courseType1 = object : TypeToken<List<PlayCount>>() {}.type
                val playCountmodel1: ArrayList<PlayCount> = gsonFile
                    .fromJson(courseJsonString1, courseType)
                for(i in 0 until playCountmodel1.size){
                    val playCount = playCountmodel1[i]
                    val count = databaseHandler!!.getPlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                    if (count == 0) {
                        databaseHandler!!.insertPlayCount(playCount)
                    }
                }

                try{
                    val courseJsonStr1 = loadJSONFromAsset( "calculus1/jee-calculus-1/" + "coursetestdelete.json")
                    val playCountmodelDelete1: ArrayList<PlayCount> = gsonFile
                        .fromJson(courseJsonStr1, courseType)
                    if(playCountmodelDelete1.size > 0){
                        for(i in 0 until playCountmodelDelete1.size){
                            val playCount = playCountmodelDelete1[i]
                            val count = databaseHandler!!.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())

                        }

                    }
                }catch (e:Exception){

                }



                val courseJsonString2 = loadJSONFromAsset( "calculus2/jee-calculus-2/" + "coursetestinfo.json")
                // val gsonFile = Gson()
                // val courseType = object : TypeToken<List<PlayCount>>() {}.type
                val playCountmodel2: ArrayList<PlayCount> = gsonFile
                    .fromJson(courseJsonString2, courseType)
                for(i in 0 until playCountmodel2.size){
                    val playCount = playCountmodel2[i]
                    val count = databaseHandler!!.getPlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                    if (count == 0) {
                        databaseHandler!!.insertPlayCount(playCount)
                    }
                }

                try{
                    val courseJsonStr2 = loadJSONFromAsset( "calculus2/jee-calculus-2/" + "coursetestdelete.json")
                    val playCountmodelDelete2: ArrayList<PlayCount> = gsonFile
                        .fromJson(courseJsonStr2, courseType)
                    if(playCountmodelDelete2.size > 0){
                        for(i in 0 until playCountmodelDelete2.size){
                            val playCount = playCountmodelDelete2[i]
                            val count = databaseHandler!!.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())

                        }

                    }
                }catch (e:Exception){

                }



                val courseJsonString3 = loadJSONFromAsset( "geometry/iii-geometry/" + "coursetestinfo.json")
                //val gsonFile = Gson()
                // val courseType = object : TypeToken<List<PlayCount>>() {}.type
                val playCountmodel3: ArrayList<PlayCount> = gsonFile
                    .fromJson(courseJsonString3, courseType)
                for(i in 0 until playCountmodel3.size){
                    val playCount = playCountmodel3[i]
                    val count = databaseHandler!!.getPlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                    if (count == 0) {
                        databaseHandler!!.insertPlayCount(playCount)
                    }
                }

                try{
                    val courseJsonStr3 = loadJSONFromAsset( "geometry/iii-geometry/" + "coursetestdelete.json")
                    val playCountmodelDelete3: ArrayList<PlayCount> = gsonFile
                        .fromJson(courseJsonStr3, courseType)
                    if(playCountmodelDelete3.size > 0){
                        for(i in 0 until playCountmodelDelete3.size){
                            val playCount = playCountmodelDelete3[i]
                            val count = databaseHandler!!.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())

                        }

                    }
                }catch (e:Exception){

                }


                val courseJsonString4 = loadJSONFromAsset( "other/other/" + "coursetestinfo.json")
                // val gsonFile = Gson()
                // val courseType = object : TypeToken<List<PlayCount>>() {}.type
                val playCountmodel4: ArrayList<PlayCount> = gsonFile
                    .fromJson(courseJsonString4, courseType)
                for(i in 0 until playCountmodel4.size){
                    val playCount = playCountmodel4[i]
                    val count = databaseHandler!!.getPlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                    if (count == 0) {
                        databaseHandler!!.insertPlayCount(playCount)
                    }
                }

                try{
                    val courseJsonStr4 = loadJSONFromAsset( "other/other/" + "coursetestdelete.json")
                    val playCountmodelDelete4: ArrayList<PlayCount> = gsonFile
                        .fromJson(courseJsonStr4, courseType)
                    if(playCountmodelDelete4.size > 0){
                        for(i in 0 until playCountmodelDelete4.size){
                            val playCount = playCountmodelDelete4[i]
                            val count = databaseHandler!!.deletePlayCount(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())

                        }

                    }
                }catch (e:Exception){

                }

            }


        }catch (e:Exception){

        }


        //getPlayerForCorrect(this)
        //getPlayerForwrong(this)
        /*gradeTitle = intent.getStringExtra(TITLE_TOPIC)*/
       /* sAnalytics = GoogleAnalytics.getInstance(this);
        mTracker = Utils.getDefaultTracker(sAnalytics);

        mTracker!!.setScreenName("Dashboard")
        mTracker!!.send(HitBuilders.ScreenViewBuilder().build())*/

        /*var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
        if(testcontentlist!!.size <= 0){

            *//*version = sharedPrefs!!.getPrefVal(this,"testversion")!!
            url = sharedPrefs!!.getPrefVal(this,"testurl")!!

            Log.e("dashboard activity","version....."+version);
            Log.e("dashboard activity","url....."+url);
            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"",0)
            downloadDataFromBackground(this@DashBoardActivity,url,version)*//*

            val docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4")
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.e("grade activity", "DocumentSnapshot data: ${document.data}")
                    *//*version = document.data!!.get("TestContentVersion").toString()
                    url = document.data!!.get("TestContentUrl").toString()


                    databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"",0)
                    //sharedPrefs.setBooleanPrefVal(this@GradeActivity, ConstantPath.IS_FIRST_TIME, true)
                    //if(hasPermissions(this@GradeActivity, *PERMISSIONS)){
                        // var url = databaseHandler!!.gettesttopicurl()
                        downloadDataFromBackground(this@DashBoardActivity,url,version)*//*
                    for(i in 0 until (document.data!!.size - 4)){
                        if(i == 0){
                            var version = document.data!!.get("BasicVersion").toString()
                            var url = document.data!!.get("BasicUrl").toString()

                            Log.e("grade activity","version......."+version)
                            Log.e("grade activity","url......."+url)

                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"basic",0)
                            downloadDataFromBackground(this@DashBoardActivity,url,version,"basic")
                        }else if(i == 1){
                            var version = document.data!!.get("AlgebraVersion").toString()
                            var url = document.data!!.get("AlgebraUrl").toString()

                            Log.e("grade activity","version......."+version)
                            Log.e("grade activity","url......."+url)

                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"algebra",0)
                            downloadDataFromBackground(this@DashBoardActivity,url,version,"algebra")
                        }else if(i == 2){
                            var version = document.data!!.get("CalculusVersion").toString()
                            var url = document.data!!.get("CalculusUrl").toString()

                            Log.e("grade activity","version......."+version)
                            Log.e("grade activity","url......."+url)

                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"calculus",0)
                            downloadDataFromBackground(this@DashBoardActivity,url,version,"calculus")
                        }else if(i == 3){
                            var version = document.data!!.get("GeometryVersion").toString()
                            var url = document.data!!.get("GeometryUrl").toString()

                            Log.e("grade activity","version......."+version)
                            Log.e("grade activity","url......."+url)

                            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,"geometry",0)
                            downloadDataFromBackground(this@DashBoardActivity,url,version,"geometry")
                        }
                    }

                } else {
                    Log.e("grade activity", "No such document")
                    //navigateToDashboard("GRADE 6")

                }
            }
                .addOnFailureListener { exception ->
                    Log.e("grade activity", "get failed with ", exception)
                    //navigateToDashboard("GRADE 6")

                }
        }*/


        /*try{
            auth = FirebaseAuth.getInstance()
            val docRef = db.collection("users")
            docRef.whereEqualTo("username",auth!!.currentUser!!.email)
                .get().addOnCompleteListener(object : OnCompleteListener<QuerySnapshot> {
                    override fun onComplete(task: Task<QuerySnapshot>) {
                        if (task.isSuccessful){

                            task.getResult().forEachIndexed { index, document ->
                                Log.e("dashboard","document id......"+document.id)
                                if(document.contains("firebaseToken")){

                                }else{
                                    var token:String = sharedPrefs!!.getPrefVal(this@DashBoardActivity,"firebasetoken")!!
                                    val data = hashMapOf("firebaseToken" to token)

                                    db.collection("users").document(document.id)
                                        .set(data, SetOptions.merge())
                                }
                            }

                        }
                    }
                })

        }catch (e:Exception){

        }*/



        try {
            currentVersion = packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }





        firebaseAnalytics = Firebase.analytics
        firebaseAnalytics.setUserProperty("email", sharedPrefs!!.getPrefVal(this,"email"))
     //   remoteConfig = FirebaseRemoteConfig.getInstance()

       /* val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()

        remoteConfig!!.setConfigSettings(configSettings)

        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console. See Best Practices in the README for more
        // information.
        remoteConfig!!.setDefaults(R.xml.remote_config_defaults)*/
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        remoteConfig!!.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchVersion()
     //   action = sharedPrefs!!.getPrefVal(this,"action")!!
        data = sharedPrefs!!.getPrefVal(this,"screen")!!
        /*val action: String? = intent?.action
        val data1: Uri? = intent?.data
            Log.e("dashboard activity","action......"+action);
        Log.e("dashboard activity","data1......"+data1.toString());
        data = data1.toString()*/
        Log.e("dashboard activity","data........"+data)
        if(data.equals("null") || data.equals("")){
            val fragment = intent.getStringExtra("fragment")
            Log.e("dashboard activity","fragment........"+fragment)
            if(fragment == "tests"){
                loadFragment(TestsFragment())
                val revisionItem = navigation.getMenu().getItem(0)
                // Select home item
                navigation.setSelectedItemId(revisionItem.getItemId());
            }else if(fragment == "books"){
                loadFragment(BooksFragment())
                val revisionItem = navigation.getMenu().getItem(2)
                // Select home item
                navigation.setSelectedItemId(revisionItem.getItemId());
            }
            else if(fragment == "Settings"){
                loadFragment(SettingFragment())
                val revisionItem = navigation.getMenu().getItem(3)
                // Select home item
                navigation.setSelectedItemId(revisionItem.getItemId());
            }else if(fragment == "review"){
                loadFragment(ReviewFragment())
                val revisionItem = navigation.getMenu().getItem(1)
                // Select home item
                navigation.setSelectedItemId(revisionItem.getItemId());
            }else{
                Log.e("dashboard activity","load chapters......")
                loadFragment(TestsFragment())
            }
            /*else if(fragment == "dailychallenge"){
                loadFragment(DailyChallengeFragment())
                val revisionItem = navigation.getMenu().getItem(2)
                // Select home item
                navigation.setSelectedItemId(revisionItem.getItemId());
            }*/
        }else{
            Log.e("dashboard activity","data......else.."+data)
            sharedPrefs!!.setPrefVal(this,"screen", "")
            /*if(data.contains("books")){
                loadFragment(RevisionFragment())
                val revisionItem = navigation.getMenu().getItem(3)
                // Select home item
                navigation.setSelectedItemId(revisionItem.getItemId());
            }else if(data.contains("challenge")){
                loadFragment(DailyChallengeFragment())
                val revisionItem = navigation.getMenu().getItem(2)
                // Select home item
                navigation.setSelectedItemId(revisionItem.getItemId());
            }else if(data.contains("practice")){
                loadFragment(ChapterFragment())
            }*/
        }


        //loadFragment(ChapterFragment())
        //navigation.clearAnimation()
        //BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setItemIconTintList(null);
        navigation.setOnNavigationItemSelectedListener(this)
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorbottomnav));
        }
    }

    private fun staryCopyAssetsToLocal(book:Books){
        Log.e("dash board","staryCopyAssetsToLocal.......")


                try {
                    val assetManager = assets

                    var inputstream : InputStream?
                    var out: OutputStream?
                    inputstream = assetManager.open("Books/"+book.folderName+".zip")
                    out = FileOutputStream(cacheDir.absolutePath + "/Books" + "/"+book.category+"/"+book.folderName+".zip")
                    //Log.i("WEBVIEW", cacheDir.absolutePath + "/Books" + "/" + filename)
                    //copyFile(inputstream, out, filename)

                    val buffer = ByteArray(1024)
                    var read: Int = 0
                    while (inputstream.read(buffer).also({ read = it }) != -1) {
                        out.write(buffer, 0, read)
                    }

                    inputstream.close()
                    inputstream = null
                    out.flush()
                    out.close()
                    out = null


                    //dataBase.updatebooksreadfilestatusfromlocal(0,filename.replace(".zip",""));
                    val iszip = Utils.unpackZip(cacheDir.absolutePath + "/Books/" + book.category + "/", book.folderName+".zip")
                    if(iszip){
                        val dirFile = File(cacheDir.absolutePath,"/Books/" + book.category + "/"+book.folderName+".zip")
                        dirFile.delete()

                        // File dirFile1 = new File(context1.getCacheDir(),"Books/"+category+"/"+title.toLowerCase().replace(" ","-").replace("'","")+"/thumbnail.svg");
                        //dataBase.updatebooksversionFromLocal("1",filename.replace(".zip",""));
                        val path = cacheDir.absolutePath + "/Books/" + book.category + "/" + book.folderName
                        Log.e("Files", "Path: $path")

                        val p = "$path/thumbnail.svg"
                        databaseHandler!!.updatebooksValues(
                            book.id,
                            book.version,
                            1,
                            1,
                            p,
                            book.title,
                            book.sourceUrl,
                            book.sortorder,
                            book.publishedOn,
                            book.category,
                            book.visibility
                        )
                    }

                } catch (e: IOException) {
                    Log.e("ERROR", "Failed to copy asset file:....", e)
                } finally {
                    // Edit 3 (after MMs comment)
                    /*try {
                                        in.close();
                                        in = null;
                                        out.flush();
                                        out.close();
                                        out = null;
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }*/
                }


    }
    private fun downloadServiceFromBackground(
        mainActivity: Activity, db: FirebaseFirestore
    ) {
        BooksDownloadService.enqueueWork(mainActivity, db)
    }
    private fun downloadDataFromBackground(
        mainActivity: DashBoardActivity,
        url: String,version:String,type:String
    ) {
        JobService.enqueueWork(mainActivity, url,version,type)
    }



    fun fetchVersion(){
        /*var cacheExpiration: Long = 3600 // 1 hour in seconds.
        // If your app is using developer mode, cacheExpiration is set to 0, so each fetch will
        // retrieve values from the service.
        if (remoteConfig!!.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0
        }

        remoteConfig!!.fetch(cacheExpiration)
            .addOnCompleteListener(this, OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    remoteConfig!!.activateFetched()
                }
                displayUpdateAlert()
            })*/




        remoteConfig.fetch(0)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    remoteConfig.activate()
                    Log.e("grade activity", "Config params updated: $updated")
                    //Toast.makeText(this, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show()
                } else {
                    //Toast.makeText(this, "Fetch failed", Toast.LENGTH_SHORT).show()
                }
                displayUpdateAlert()
            }
    }

    fun displayUpdateAlert(){
        val feedback = remoteConfig!!.getString(FEEDBACK_CONFIG_KEY)
        val writetous = remoteConfig!!.getString(WRITETOUS_CONFIG_KEY)
        val play = remoteConfig!!.getString(VERSION_CODE_CONFIG_KEY)
        var playStoreVersionCode = 0
        if (play != null && !play!!.isEmpty() && !play!!.equals("null", ignoreCase = true))
            playStoreVersionCode = Integer.parseInt(play!!)

        Log.e("dashboard activity","....feedback...."+feedback);
        Log.e("dashboard activity","....writetous...."+writetous);
        sharedPrefs!!.setPrefVal(this,"feedback", feedback)
        sharedPrefs!!.setPrefVal(this,"writetous", writetous)
        Log.e("dashboard activity","playStoreVersionCode......."+playStoreVersionCode)
        Log.e("dashboard activity","currentVersion......."+currentVersion)

        if(playStoreVersionCode > currentVersion){
            val dialogBuilder = AlertDialog.Builder(this, R.style.mytheme)
            val inflater = this.layoutInflater
            val dialogView = inflater.inflate(R.layout.app_update_dialog, null)
            dialogBuilder.setView(dialogView)


            val btn_upgrade = dialogView.findViewById(R.id.btn_upgrade) as Button

            val alertDialog = dialogBuilder.create()
            alertDialog.setCancelable(false)
            btn_upgrade.setOnClickListener {
                alertDialog.dismiss()

                var packagename:String = packageName

                try {
                    startActivity(
                        Intent(
                            "android.intent.action.VIEW",
                            Uri.parse("market://details?id=$packagename")
                        )
                    )
                    finish()
                } catch (e: ActivityNotFoundException) {
                    try {
                        startActivity(
                            Intent(
                                "android.intent.action.VIEW",
                                Uri.parse("http://play.google.com/store/apps/details?id=$packagename")
                            )
                        )
                        finish()
                    } catch (e3: Exception) {
                        Log.w("Hari-->", e3)
                    }

                }


                //navigateToSummaryScreenNew()
                // var status:Int = databaseHandler!!.updatequizplayFinalstatus(testQuiz.title,"1",currentDate,testQuiz.lastplayed);
                // var answers:Int = databaseHandler!!.updatequizplayFinalTimeTaken(testQuiz.title,timetaken.toString(),currentDate,testQuiz.lastplayed);
                // navigateToSummaryScreenNew()
            }

            //alertDialog.getWindow().setBackgroundDrawable(draw);
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
            alertDialog.show()
        }





    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.nav_tests -> {
                val fragmentManager = supportFragmentManager
                val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
                sound = sharedPrefs?.getBooleanPrefVal(this!!, ConstantPath.SOUNDS) ?: true
                if(!sound) {
                    //MusicManager.getInstance().play(context, R.raw.amount_low);
                    // Is the sound loaded already?
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..." + Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                fetchVersion()
                if(currentFragment!! is TestsFragment){
                    fragment = currentFragment
                }else{

                    fragment = TestsFragment()

                }


            }
            R.id.nav_review -> {
                val fragmentManager = supportFragmentManager
                val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
                sound = sharedPrefs?.getBooleanPrefVal(this!!, ConstantPath.SOUNDS) ?: true
                if(!sound) {
                    //MusicManager.getInstance().play(context, R.raw.amount_low);
                    // Is the sound loaded already?
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..." + Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                fetchVersion()
                if(currentFragment!! is ReviewFragment){
                    fragment = currentFragment
                }else{

                    fragment = ReviewFragment()

                }


            }
            /*R.id.nav_videos -> {
                val fragmentManager = supportFragmentManager
                val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
                sound = sharedPrefs?.getBooleanPrefVal(this!!, ConstantPath.SOUNDS) ?: true
                if(!sound) {
                    //MusicManager.getInstance().play(context, R.raw.amount_low);
                    // Is the sound loaded already?
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..." + Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                if(currentFragment!! is VideoFragment){
                    fragment = currentFragment
                }else{

                    fragment = VideoFragment()

                }


            }*/
            R.id.nav_books -> {
                val fragmentManager = supportFragmentManager
                val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
                sound = sharedPrefs?.getBooleanPrefVal(this!!, ConstantPath.SOUNDS) ?: true
                if(!sound) {
                    //MusicManager.getInstance().play(context, R.raw.amount_low);
                    // Is the sound loaded already?
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..." + Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                fetchVersion()
                if(currentFragment!! is BooksFragment){
                    fragment = currentFragment
                }else{

                    fragment = BooksFragment()

                }


            }

            R.id.nav_settings -> {
                val fragmentManager = supportFragmentManager
                val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
                sound = sharedPrefs?.getBooleanPrefVal(this!!, ConstantPath.SOUNDS) ?: true
                if(!sound) {
                    //MusicManager.getInstance().play(context, R.raw.amount_low);
                    // Is the sound loaded already?
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..." + Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                fetchVersion()
                if(currentFragment!! is SettingFragment){
                    fragment = currentFragment
                }else{

                    fragment = SettingFragment()

                }


            }
        }
        Log.e("dash board activity","on naviagtion item")
        return loadFragment(fragment!!)
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)

        if(currentFragment!! is TestsFragment) {
            Log.e("dash board","on back pressed.....test fragment");
            if(backPressedTime+2000>System.currentTimeMillis()){
                backPressToastMessage!!.cancel()
                finishAffinity()
                return
            }
            else{
                backPressToastMessage = Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT)
                backPressToastMessage!!.show()
            }
            backPressedTime=System.currentTimeMillis()
        }else if(currentFragment!! is BooksFragment) {
            Log.e("dash board","on back pressed.....settings fragment");
            /*if(backPressedTime+2000>System.currentTimeMillis()){
                backPressToastMessage!!.cancel()
                finishAffinity()
                return
            }
            else{
                backPressToastMessage = Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT)
                backPressToastMessage!!.show()
            }
            backPressedTime=System.currentTimeMillis()*/
            loadFragment(TestsFragment())
            val revisionItem = navigation.getMenu().getItem(0)
            // Select home item
            navigation.setSelectedItemId(revisionItem.getItemId());
        }else if(currentFragment!! is SettingFragment) {
            Log.e("dash board","on back pressed.....settings fragment");
            /*if(backPressedTime+2000>System.currentTimeMillis()){
                backPressToastMessage!!.cancel()
                finishAffinity()
                return
            }
            else{
                backPressToastMessage = Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT)
                backPressToastMessage!!.show()
            }
            backPressedTime=System.currentTimeMillis()*/
            loadFragment(TestsFragment())
            val revisionItem = navigation.getMenu().getItem(0)
            // Select home item
            navigation.setSelectedItemId(revisionItem.getItemId());
        }else if(currentFragment!! is ReviewFragment) {
            Log.e("dash board","on back pressed.....review fragment");
            /*if(backPressedTime+2000>System.currentTimeMillis()){
                backPressToastMessage!!.cancel()
                finishAffinity()

                return
            }
            else{
                backPressToastMessage = Toast.makeText(this, R.string.exit_message, Toast.LENGTH_SHORT)
                backPressToastMessage!!.show()
            }
            backPressedTime=System.currentTimeMillis()*/
            loadFragment(TestsFragment())
            val revisionItem = navigation.getMenu().getItem(0)
            // Select home item
            navigation.setSelectedItemId(revisionItem.getItemId());
        }

    }

    private fun loadFragment(fragment: Fragment): Boolean {
        Log.e("dash borad activity","......load fragment....."+fragment)
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
        Log.e("dash borad activity","...... fragment....."+fragment)
        Log.e("dash borad activity","......currentFragment fragment....."+currentFragment)
        if(fragment!!.equals(currentFragment)){
            Log.e("dash borad activity","......same fragment.....")
            return true
        }else{
            Log.e("dash borad activity","......different fragment.....")
            if (fragment != null) {
                val bundle = Bundle()
                bundle.putString(TITLE_TOPIC, gradeTitle)
                fragment.arguments = bundle
                getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit()

                return true
            }
        }
        //switching fragment

        return false
    }

    lateinit var myDir: File

    companion object {
        private val ENC = "enc"
        private val DEC = "dec.png"

        private val key = "PDY8o0tPHNYz1FG7"
        private val specString = "yoe6Nd84MOZCzbb0"
    }

    /*private fun signin(sharedPrefs: SharedPrefs) {
        if (sharedPrefs.getBooleanPrefVal(this, IS_LOGGED_IN)) {
            Log.d(TAG, "signInAnonymously:Already Logged In")
            val uid : String = sharedPrefs.getPrefVal(this, ConstantPath.UID)!!
            Log.d(TAG,uid)
            Toast.makeText(baseContext, "UID "+uid, Toast.LENGTH_SHORT).show()

            *//*val storage = FirebaseStorage.getInstance()
            val storageRef = storage.getReference().child("astra-quiz-v.1.0.zip");

            val imageFile = File.createTempFile("test", "zip");

            Log.d(TAG,"Temp file : " + imageFile.getAbsolutePath());

            storageRef.getFile(imageFile)
                .addOnSuccessListener(OnSuccessListener<FileDownloadTask.TaskSnapshot> {
                    Toast.makeText(applicationContext, "file created", Toast.LENGTH_SHORT).show()
                    Log.d("file","created :  "+it.toString()+"!"+it.totalByteCount);

                    //startApp()
                }).addOnFailureListener(OnFailureListener {
                    Log.d("file","not created : "+it.toString());
                    Toast.makeText(applicationContext, "An error accoured", Toast.LENGTH_SHORT).show()
                })

            val user = auth.currentUser
            TedPermission.with(this)
                .setPermissionListener(this)
                .setDeniedMessage("If you reject permission,you can not use this service\n"
                        + "\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()*//*

            //TODO: encryption
           *//* Dexter.withActivity(this)
                .withPermissions(*arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ))
                .withListener(object : MultiplePermissionsListener{

                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        Log.d(TAG,"onPermmissionChecked");
                        Toast.makeText(this@DashBoardActivity,"You should ",Toast.LENGTH_LONG).show()
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        Log.d(TAG,"onPermissionRationaleShouldBeShown");
                        Toast.makeText(this@DashBoardActivity,"You should accept permission",Toast.LENGTH_LONG).show()
                    }

                })
                .check()

            val root = Environment.getExternalStorageDirectory().toString()
            myDir = File("$root/saved_images")
            if(!myDir.exists()){
                myDir.mkdirs()

                val drawable = ContextCompat.getDrawable(this,R.drawable.rectangle_tab)
                val bitmapDrawable = drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
                val input = ByteArrayInputStream(stream.toByteArray())

                val outputFileEnc = File(myDir, ENC)

                try{
                    MyEncrypter.encryptToFile(key, specString,input,FileOutputStream(outputFileEnc))
                    Toast.makeText(this,"ENCRYPTED",Toast.LENGTH_LONG).show()
                }catch (e:Exception)
                {
                    Log.d("EXCEPTION : ",e.toString()+"!")
                }
            }*//*

        } else {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            Log.d("isConnected",isConnected.toString()+"!")
            if(isNetworkConnected()) {
                auth.signInAnonymously()
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(baseContext, "Logged In", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "signInAnonymously:success")
                            val user = auth.currentUser
                            sharedPrefs.setBooleanPrefVal(this, IS_LOGGED_IN, true)
                            sharedPrefs.setPrefVal(this, UID, user!!.uid)

                            TedPermission.with(this)
                                .setPermissionListener(this)
                                .setDeniedMessage(
                                    "If you reject permission,you can not use this service\n"
                                            + "\nPlease turn on permissions at [Setting] > [Permission]"
                                )
                                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .check()
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.exception)
                            //.makeText(baseContext, "Authentication failed. Check Internet Connection", Toast.LENGTH_SHORT).show()
                            mSnackBar = Snackbar.make(
                                findViewById(com.blobcity.R.id.rl_dashboard),
                                "Auth Failed :(",
                                Snackbar.LENGTH_LONG
                            ) //Assume "rootLayout" as the root layout of every activity.
                            mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
                            mSnackBar?.setAction("Retry", { signin(sharedPrefs) })
                            mSnackBar?.show()
                        }
                    }
            }else{
                mSnackBar = Snackbar.make(
                    findViewById(R.id.rl_dashboard),
                    "No Internet Connection",
                    Snackbar.LENGTH_LONG
                ) //Assume "rootLayout" as the root layout of every activity.
                mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
                mSnackBar?.setAction("Retry", { signin(sharedPrefs) })
                mSnackBar?.show()
            }
        }
    }*/

    /*override fun onPermissionGranted() {
        ;

        *//*databaseRefrence!!.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                topicStatusModelList = ArrayList()
                for (postSnapshot in dataSnapshot.children) {
                    Log.e("snap", postSnapshot.value.toString())
                    val topicStatusModel: TopicStatusModel = postSnapshot!!.getValue(TopicStatusModel::class.java)!!
                    topicStatusModelList!!.add(topicStatusModel)
                    if (topicStatusModelList != null){
                        if (topicStatusModelList!!.size > 0){
                            for (branchItem in branchesItemList!!) {
                                val branchId = branchItem.id
                                branchItem.basic = 0
                                branchItem.intermediate = 0
                                branchItem.advance = 0
                                for (topicStatusModels in topicStatusModelList!!) {
                                    val id = topicStatusModels.topicId
                                    val level = topicStatusModels.topicLevel

                                    if (id!!.contains(branchId)) {
                                        if (level!!.contains("basic")) {
                                            branchItem.basic = 1
                                        }
                                        if (level.contains("intermediate")) {
                                            branchItem.intermediate = 1
                                        }
                                        if (level.contains("advance")) {
                                            branchItem.advance = 1
                                        }
                                    }

                                }
                            }
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                    Log.e("topicStatusDb", topicStatusModel.topicLevel)
                }
            }
        })*//*

    }

    override fun onPermissionDenied(deniedPermissions: List<String>) {

    }*/
}