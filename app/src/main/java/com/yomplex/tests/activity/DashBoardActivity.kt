package com.yomplex.tests.activity

import android.app.PendingIntent.getActivity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle

import androidx.core.app.ActivityCompat

import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_dashboard.*
import java.io.File
import java.sql.Time

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.PersistableBundle

import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment


import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions


import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.yomplex.tests.BuildConfig
import com.yomplex.tests.R
import com.yomplex.tests.Service.JobService
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.fragment.SettingFragment
import com.yomplex.tests.fragment.TestsFragment
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.ConstantPath.TITLE_TOPIC
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import com.yomplex.tests.utils.Utils.getPlayer
import java.text.SimpleDateFormat
import java.util.*


class DashBoardActivity : BaseActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

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
    var remoteConfig: FirebaseRemoteConfig? = null
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

        databaseHandler = QuizGameDataBase(this);
        //getPlayerForCorrect(this)
        //getPlayerForwrong(this)
        /*gradeTitle = intent.getStringExtra(TITLE_TOPIC)*/
        sAnalytics = GoogleAnalytics.getInstance(this);
        mTracker = Utils.getDefaultTracker(sAnalytics);

        mTracker!!.setScreenName("Dashboard")
        mTracker!!.send(HitBuilders.ScreenViewBuilder().build())

        var dburl = databaseHandler!!.gettesttopicurl()
        if(dburl == null){

            version = sharedPrefs!!.getPrefVal(this,"testversion")!!
            url = sharedPrefs!!.getPrefVal(this,"testurl")!!

            Log.e("dashboard activity","version....."+version);
            Log.e("dashboard activity","url....."+url);
            databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,0)
            downloadDataFromBackground(this@DashBoardActivity,url,version)

            val docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4")
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    Log.e("grade activity", "DocumentSnapshot data: ${document.data}")
                    version = document.data!!.get("TestContentVersion").toString()
                    url = document.data!!.get("TestContentUrl").toString()


                    databaseHandler!!.insertTESTCONTENTDOWNLOAD(version,url,0)
                    //sharedPrefs.setBooleanPrefVal(this@GradeActivity, ConstantPath.IS_FIRST_TIME, true)
                    //if(hasPermissions(this@GradeActivity, *PERMISSIONS)){
                        // var url = databaseHandler!!.gettesttopicurl()
                        downloadDataFromBackground(this@DashBoardActivity,url,version)





                } else {
                    Log.e("grade activity", "No such document")
                    //navigateToDashboard("GRADE 6")

                }
            }
                .addOnFailureListener { exception ->
                    Log.e("grade activity", "get failed with ", exception)
                    //navigateToDashboard("GRADE 6")

                }
        }


        try{
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

        }



        try {
            currentVersion = packageManager.getPackageInfo(packageName, 0).versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }



        sharedPrefs = SharedPrefs()
        remoteConfig = FirebaseRemoteConfig.getInstance()

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .build()

        remoteConfig!!.setConfigSettings(configSettings)

        // Set default Remote Config parameter values. An app uses the in-app default values, and
        // when you need to adjust those defaults, you set an updated value for only the values you
        // want to change in the Firebase console. See Best Practices in the README for more
        // information.
        remoteConfig!!.setDefaults(R.xml.remote_config_defaults)
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
            }
            else if(fragment == "Settings"){
                loadFragment(SettingFragment())
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

    private fun downloadDataFromBackground(
        mainActivity: DashBoardActivity,
        url: String,version:String
    ) {
        JobService.enqueueWork(mainActivity, url,version)
    }



    fun fetchVersion(){
        var cacheExpiration: Long = 3600 // 1 hour in seconds.
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
            })
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
                if(currentFragment!! is TestsFragment){
                    fragment = currentFragment
                }else{

                    fragment = TestsFragment()

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
        }else if(currentFragment!! is SettingFragment) {
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