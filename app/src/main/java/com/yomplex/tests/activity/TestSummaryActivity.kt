package com.yomplex.tests.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast


import com.blobcity.viewmodel.TopicStatusVM
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yomplex.tests.R
import com.yomplex.tests.Service.ContentDownloadService
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.entity.TopicStatusEntity
import com.yomplex.tests.model.*
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.activity_quiz_time_summary.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class TestSummaryActivity : BaseActivity(), View.OnClickListener {


    var reviewModelList: ArrayList<ReviewModel>? = null
    var topicLevel: String? = ""
    var topicName: String? = ""
    var originaltopicName: String? = ""
    var topicId: String? = ""
    private var totalQuestion: Int? = null
    var topicStatusVM: TopicStatusVM? = null
    var complete: String? = null
    //var position: Int? = null
    var dPath: String? = null
    var paths: String? = null
    var courseId: String? = null
    var courseName: String? = null
    var readdata: String? = null
    var folderName: String? = null
    var level_status: Boolean? = null
    var readyCardNumber = 0
    var gradeTitle: String? = null
    var topicStatusModelList: ArrayList<TopicStatusEntity>? = null
    var topicStatusModelList2: ArrayList<TopicStatusEntity>? = null

    var databaseHandler: QuizGameDataBase?= null
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    lateinit var circles: Array<ImageView?>
    var isallcorrect:Boolean = false
    //var displayno:Int = 0
    var lastplayed:String = ""
    var comingfrom:String = ""
    var count:Int = 0
    var localPath: String?= null
    private var branchesItemList:List<BranchesItem>?=null
    var jsonStringBasic: String? =""
    lateinit var testQuiz: TestQuiz
    lateinit var challenge: Challenge
    var mLastClickTime:Long = 0;
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    override var layoutID: Int = R.layout.activity_quiz_time_summary

    override fun onResume() {
        super.onResume()
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "SummaryScreen "+originaltopicName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "TestSummaryActivity")
        }
    }

    override fun initView() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorbottomnav));
        }
        sharedPrefs = SharedPrefs()
        databaseHandler = QuizGameDataBase(this);
        reviewModelList = intent.getSerializableExtra(ConstantPath.REVIEW_MODEL) as ArrayList<ReviewModel>?
        topicLevel = intent.getStringExtra(ConstantPath.TOPIC_LEVEL)
        topicName = intent.getStringExtra(ConstantPath.TOPIC_NAME)
        topicId = intent.getStringExtra(ConstantPath.TOPIC_ID)
        totalQuestion = intent.getIntExtra(ConstantPath.QUIZ_COUNT, 0)
        complete = intent.getStringExtra(ConstantPath.LEVEL_COMPLETED)
      //  position = intent.getIntExtra(ConstantPath.TOPIC_POSITION, -1)
        dPath = intent.getStringExtra(ConstantPath.DYNAMIC_PATH)
        paths = intent.getStringExtra(ConstantPath.FOLDER_PATH)
        courseId = intent.getStringExtra(ConstantPath.COURSE_ID)
        courseName = intent.getStringExtra(ConstantPath.COURSE_NAME)
        folderName = intent.getStringExtra(ConstantPath.FOLDER_NAME)
        gradeTitle = intent.getStringExtra(ConstantPath.TITLE_TOPIC)
        level_status = intent.getBooleanExtra(ConstantPath.IS_LEVEL_COMPLETE, false)
        readyCardNumber = intent.getIntExtra(ConstantPath.CARD_NO, -1)
     //   displayno = intent.getIntExtra("DISPLAY_NO", -1)
        lastplayed = intent.getStringExtra("LAST_PLAYED") ?: "last"
        comingfrom = intent.getStringExtra("comingfrom")!!
        originaltopicName = intent.getStringExtra("topicnameoriginal")!!

        readdata = intent.getStringExtra("readdata")

        Log.e("test summary","topic name....."+topicName)
        testQuiz = databaseHandler!!.getQuizTopicsForTimerLastPlayed(topicName!!.toLowerCase())
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()

        try{
            db.firestoreSettings = settings
        }catch (e:Exception){

        }
        //firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics = Firebase.analytics
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val currentDate = sdf.format(Date())



        tv_quiz_title.text = originaltopicName

        var questionanswers:String = databaseHandler!!.getQuizQuestionAnswersFinal(testQuiz.title,currentDate,testQuiz.lastplayed,testQuiz.testtype);
        var queans:List<String> = questionanswers.split(",")


        //btn_start.text = "PLAY AGAIN"
        btn_review.visibility = View.VISIBLE
        circles = arrayOfNulls<ImageView>(totalQuestion!!)
        ll_answers.removeAllViews()
        for(i in 0 until totalQuestion!!){
            var ans1:List<String> = queans.get(i).split("~")
            if(ans1[1].equals("opt1")){
                val params = LinearLayout.LayoutParams(getResources().getDimension(R.dimen._18sdp).toInt(), getResources().getDimension(R.dimen._18sdp).toInt())
                if(i != 0){
                    params.leftMargin = getResources().getDimension(R.dimen._17sdp).toInt()
                }

                circles[i] = ImageView(this)
                //isallcorrect = true
                count++
                Glide.with(this)
                    .load(R.drawable.ic_tick_green)
                    .into(circles[i]!!)
                //ImageViewCompat.setImageTintList(circles[i]!!, ColorStateList.valueOf(getResources().getColor(R.color.right_tick)));
                circles[i]!!.layoutParams = params
                ll_answers.addView(circles!![i])
            }else{
                val params = LinearLayout.LayoutParams(getResources().getDimension(R.dimen._18sdp).toInt(), getResources().getDimension(R.dimen._18sdp).toInt())
                if(i != 0){
                    params.leftMargin = getResources().getDimension(R.dimen._17sdp).toInt()
                }

                circles[i] = ImageView(this)
                //isallcorrect = false

                Glide.with(this)
                    .load(R.drawable.wrong_ans_circle)
                    .into(circles[i]!!)

                circles[i]!!.layoutParams = params
                ll_answers.addView(circles!![i])
            }
        }

        Log.e("test summary","count....."+count);
        Log.e("test summary","weekofyear....."+getWeekOfYear());
        var recordexistcount = databaseHandler!!.getQuizScore(getWeekOfYear(),topicName!!.toLowerCase())
        Log.e("test summary","recordexistcount....."+recordexistcount);
        if(recordexistcount < 0){
            var quizScore:QuizScore
            quizScore = QuizScore(""+getWeekOfYear(),""+count,topicName!!.toLowerCase())
            databaseHandler!!.insertQuizPlayScore(quizScore)
        }else{
            if(count > recordexistcount){
                databaseHandler!!.updateQuizPlayScore(getWeekOfYear(),topicName!!.toLowerCase(),count)
            }

        }

        tv_total.text = ""+totalQuestion
        if(count == totalQuestion){
           // firebaseAnalytics.setCurrentScreen(this, "TestSuccess", null /* class override */)
            //successRL.background = resources.getDrawable(R.drawable.quiz_time_success)
            tv_correct.setTextColor(resources.getColor(R.color.button_close_text))

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var recordcount = databaseHandler!!.getChallengeForDate(format.format(Utils.date),testQuiz.testtype)

            if(recordcount == 0){


                challenge = Challenge(format.format(Utils.date),-1,-1,count,1,testQuiz.testtype)
                databaseHandler!!.insertChallenge(challenge)
            }else{
                var teststatus = databaseHandler!!.getChallengeForTestStatus(format.format(Utils.date),testQuiz.testtype)
                if(teststatus != 1){
                    databaseHandler!!.updateChallengeTest(format.format(Utils.date),count,1,testQuiz.testtype)
                }

            }

            var weeklycount = databaseHandler!!.getChallengeForWEEKLY(format.format(getWeekStartDate()),format.format(getWeekEndDate()),testQuiz.testtype)
            if(weeklycount == 0){
                databaseHandler!!.insertChallengeWeekly(format.format(getWeekStartDate()),format.format(getWeekEndDate()),1,testQuiz.testtype)
            }else{
                var passcount = databaseHandler!!.getChallengeWeeklystatus(format.format(getWeekStartDate()),format.format(getWeekEndDate()),testQuiz.testtype)
                databaseHandler!!.updateChallengeweeklystatus(format.format(getWeekStartDate()),format.format(getWeekEndDate()),(passcount+1),testQuiz.testtype)

            }



        }else{
          //  firebaseAnalytics.setCurrentScreen(this, "TestFail", null /* class override */)
            //successRL.background = resources.getDrawable(R.drawable.quiz_time_failure)
            tv_correct.setTextColor(resources.getColor(R.color.test_fail))

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
            var recordcount = databaseHandler!!.getChallengeForDate(format.format(Utils.date),testQuiz.testtype)

            if(recordcount == 0){
                var testStatus:Int = -1
                if(count >= 2){
                    testStatus = 1
                }else{
                    testStatus = 0
                }

                challenge = Challenge(format.format(Utils.date),-1,-1,count,testStatus,testQuiz.testtype)
                databaseHandler!!.insertChallenge(challenge)
            }else{
                var teststatus = databaseHandler!!.getChallengeForTestStatus(format.format(Utils.date),testQuiz.testtype)
                if(teststatus != 1){
                    var testStatus:Int = -1
                    if(count >= 2){
                        testStatus = 1
                    }else{
                        testStatus = 0
                    }
                    databaseHandler!!.updateChallengeTest(format.format(Utils.date),count,testStatus,testQuiz.testtype)
                }

            }

        }

        if(count == 0){
            tv_correct.text = ""+count
            totalLL.background = resources.getDrawable(R.drawable.test_summary_0_0)
            summaryTxt.text = "oops!"
            tv_correct.setTextColor(resources.getColor(R.color.seriously))
            summaryTxt.setTextColor(resources.getColor(R.color.seriously))
        }else if(count == 1){
            tv_correct.text = ""+count
            totalLL.background = resources.getDrawable(R.drawable.test_summary_0_0)
            summaryTxt.text = "better than 0"
            tv_correct.setTextColor(resources.getColor(R.color.not_good))
            summaryTxt.setTextColor(resources.getColor(R.color.not_good))
        }else if(count == 2){
            tv_correct.text = ""+count
            totalLL.background = resources.getDrawable(R.drawable.test_summary_2_2)
            summaryTxt.text = "not bad"
            tv_correct.setTextColor(resources.getColor(R.color.not_bad))
            summaryTxt.setTextColor(resources.getColor(R.color.not_bad))
        }else if(count == 3){
            tv_correct.text = ""+count
            totalLL.background = resources.getDrawable(R.drawable.test_summary_3_3)
            summaryTxt.text = "pretty good!"
            tv_correct.setTextColor(resources.getColor(R.color.good))
            summaryTxt.setTextColor(resources.getColor(R.color.good))

        }else if(count == 4){
            tv_correct.text = ""+count
            tv_correct.setTextColor(resources.getColor(R.color.white))
            totalLL.background = resources.getDrawable(R.drawable.test_summary_4_4)
            summaryTxt.text = "superrr!"
            summaryTxt.setTextColor(resources.getColor(R.color.perfect))

        }



        var time:String = databaseHandler!!.getQuiztimetakens(testQuiz.title,currentDate,testQuiz.lastplayed,testQuiz.testtype);
        if(time.equals("0")){
            tv_time.text = "1 min"
        }else{
            tv_time.text = time+" mins"
        }

        btn_topics!!.setOnClickListener(this)
        btn_new_test!!.setOnClickListener(this)
        btn_play_next!!.setOnClickListener(this)
        btn_review!!.setOnClickListener(this)



    }

    fun getWeekOfYear(): Int{
        val calendar = Calendar.getInstance()
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(Utils.date)
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }

    fun getWeekStartDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.setTime(Utils.date)
        while (calendar.get(Calendar.DAY_OF_WEEK) !== Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }
        return calendar.time
    }

    fun getWeekEndDate(): Date {
        val calendar = Calendar.getInstance()
        calendar.setTime(Utils.date)
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
            calendar.add(Calendar.DATE, 7)
        }else {
            while (calendar.get(Calendar.DAY_OF_WEEK) !== Calendar.MONDAY) {
                calendar.add(Calendar.DATE, 1)
            }
        }

        calendar.add(Calendar.DATE, -1)
        return calendar.time
    }
    private fun downloadServiceFromBackground(
        mainActivity: Activity, db: FirebaseFirestore
    ) {
        ContentDownloadService.enqueueWork(mainActivity, db)
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {

            R.id.btn_topics -> {
                sound = sharedPrefs?.getBooleanPrefVal(this, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }

                if(comingfrom.equals("Home")){
                    val intent = Intent(this, DashBoardActivity::class.java)
                    intent.putExtra("fragment","Home")
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, DashBoardActivity::class.java)
                    intent.putExtra("fragment","tests")
                    startActivity(intent)
                    finish()
                }


            }
            R.id.btn_new_test -> {
                sound = sharedPrefs?.getBooleanPrefVal(this, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                if(count == totalQuestion){
                    /*val bundle = Bundle()
                    bundle.putString("Category", "Test")
                    bundle.putString("Action", "TestSNewTest")
                    bundle.putString("Label", topicName)
                    firebaseAnalytics?.logEvent("TestSuccess", bundle)*/

                   /* val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topicName)
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
                    // bundle.putString("Label", "TestGo")
                    firebaseAnalytics?.logEvent("TestSNewTest", bundle)*/
                    firebaseAnalytics.logEvent("TestSNewTest") {
                        param(FirebaseAnalytics.Param.SCREEN_NAME, "SummaryScreen")
                        //param(FirebaseAnalytics.Param.SCREEN_CLASS, "TestSummaryActivity")
                    }
                }else{
                    /*val bundle = Bundle()
                    bundle.putString("Category", "Test")
                    bundle.putString("Action", "TestFNewTest")
                    bundle.putString("Label", topicName)
                    firebaseAnalytics?.logEvent("TestFail", bundle)*/

                    /*val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topicName)
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
                    // bundle.putString("Label", "TestGo")
                    firebaseAnalytics?.logEvent("TestFNewTest", bundle)*/
                    firebaseAnalytics.logEvent("TestFNewTest") {
                        param(FirebaseAnalytics.Param.SCREEN_NAME, "SummaryScreen")
                        //param(FirebaseAnalytics.Param.SCREEN_CLASS, "TestSummaryActivity")
                    }
                }

                var downloadstatus:Int = -1
                var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
                for(i in 0 until testcontentlist!!.size) {
                    if (testcontentlist.get(i).testtype.equals(topicName!!.toLowerCase())) {
                        downloadstatus = testcontentlist.get(i).testdownloadstatus
                        break
                    }
                }
                var filename = ""
                if (topicName!!.toLowerCase().equals("calculus1")) {
                    filename = "jee-calculus-1"
                } else if (topicName!!.toLowerCase().equals("calculus2")) {
                    filename = "jee-calculus-2"
                } else if (topicName!!.toLowerCase().equals("algebra")) {
                    filename = "ii-algebra"
                } else if (topicName!!.toLowerCase().equals("other")) {
                    filename = "other"
                } else if (topicName!!.toLowerCase().equals("geometry")) {
                    filename = "iii-geometry"
                }

                Log.e("test fragment","on click download status...."+downloadstatus)
                if(downloadstatus == 1){
                    val dirFile = File(getCacheDir(),topicName!!.toLowerCase()+"/"+filename)
                    if(dirFile.isDirectory){
                        var files = dirFile.list();
                        if (files.size == 0) {
                            //directory is empty
                            Log.e("test fragment","files.size......empty.");
                            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                            val isConnected: Boolean = activeNetwork?.isConnected == true
                            //Log.d("isConnected",isConnected.toString()+"!")
                            databaseHandler!!.updatetestcontentdownloadstatus(0,topicName!!.toLowerCase())
                            if(isNetworkConnected()) {
                                downloadServiceFromBackground(this@TestSummaryActivity,db)
                            }
                            readFileFromAssets(topicName!!.toLowerCase(),filename)
                            //gotoStartScreenThroughAssets(topicname)
                        }else{
                            Log.e("test fragment","files.size.....not...empty.");
                            readFileLocally(topicName!!.toLowerCase())
                            //gotoStartScreen(topicname)

                            //for testing tests read from assets
                            //readFileFromAssets(topicName!!.toLowerCase())
                            //gotoStartScreenThroughAssets(topicname)
                        }
                    }
                    //readFileLocally(topicName!!.toLowerCase())
                    //playNextBtnAction(position!!)
                }else{
                    databaseHandler!!.updatetestcontentdownloadstatus(0,topicName!!.toLowerCase())
                    if(isNetworkConnected()) {
                        downloadServiceFromBackground(this@TestSummaryActivity,db)
                    }
                    readFileFromAssets(topicName!!.toLowerCase(),filename)
                    //gotoStartScreenThroughAssets(topicName!!)
                }


            }

            R.id.btn_review -> {
                sound = sharedPrefs?.getBooleanPrefVal(this, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }

                if(count == totalQuestion){
//                    val bundle = Bundle()
//                    bundle.putString("Category", "Test")
//                    bundle.putString("Action", "TestSReview")
//                    bundle.putString("Label", topicName)
//                    firebaseAnalytics?.logEvent("TestSuccess", bundle)

                   /* val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topicName)
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
                    // bundle.putString("Label", "TestGo")
                    firebaseAnalytics?.logEvent("TestSReview", bundle)*/
                }else{
                    /*val bundle = Bundle()
                    bundle.putString("Category", "Test")
                    bundle.putString("Action", "TestFReview")
                    bundle.putString("Label", topicName)
                    firebaseAnalytics?.logEvent("TestFail", bundle)*/

                    /*val bundle = Bundle()
                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topicName)
                    bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
                    // bundle.putString("Label", "TestGo")
                    firebaseAnalytics?.logEvent("TestFNewTest", bundle)*/
                }
                firebaseAnalytics.logEvent("ReviewSummary") {
                    param(FirebaseAnalytics.Param.SCREEN_NAME, "SummaryScreen")
                    //param(FirebaseAnalytics.Param.SCREEN_CLASS, "TestSummaryActivity")
                }
                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val currentDate = sdf.format(Date())

                val intent = Intent(this, TestReviewActivity::class.java)
                intent.putExtra(ConstantPath.DYNAMIC_PATH, dPath)
                intent.putExtra(ConstantPath.COURSE_ID, courseId)
                intent.putExtra(ConstantPath.TOPIC_ID, topicId)
                intent.putExtra(ConstantPath.COURSE_NAME, courseName)
                intent.putExtra(ConstantPath.TOPIC_LEVEL, topicLevel)
                intent.putExtra(ConstantPath.TOPIC_NAME, topicName)
                intent.putExtra(ConstantPath.LEVEL_COMPLETED, complete)
               // intent.putExtra(ConstantPath.TOPIC_POSITION, position!!)
                intent.putExtra(ConstantPath.FOLDER_PATH, paths)
                intent.putExtra(ConstantPath.FOLDER_NAME, folderName)
                intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
                intent.putExtra(ConstantPath.CARD_NO, readyCardNumber)
                intent.putExtra(ConstantPath.QUIZ_COUNT, totalQuestion)

                intent.putExtra("title", testQuiz.title)
                intent.putExtra("playeddate", currentDate)
                intent.putExtra("lastplayed", testQuiz.lastplayed)
                intent.putExtra("readdata", readdata)

                startActivity(intent)
                //finish()
            }

        }
    }




    private fun readFileFromAssets(topicname: String,filename: String){
        //Toast.makeText(activity,"content read from assets", Toast.LENGTH_SHORT).show()
        val courseJsonString = loadJSONFromAsset( topicname+"/"+filename+"/" + "Courses.json")
        Log.d("courseJsonString",courseJsonString+"!");
        /*val jsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( assetTestCoursePath + "topic.json")*/
        val gsonFile = Gson()
        val courseType = object : TypeToken<List<CoursesResponseModel>>() {}.type
        val courseResponseModel: ArrayList<CoursesResponseModel> = gsonFile
            .fromJson(courseJsonString, courseType)
        courseId = courseResponseModel[0].id
        courseName = courseResponseModel[0].syllabus.title
        Log.e("test fragment","readFileFromAssets.....courseName...."+courseName);
        // tv_class.text = courseName
        // tv_class_board.text = courseResponseModel[0].syllabus.displayTitle
        localPath = "$topicname/$filename/$courseName/"
        Log.e("test fragment","readFileFromAssets.....localPath...."+localPath);
        // val jsonString = readFromFile(localPath +"topic.json")
        val jsonString = loadJSONFromAsset( localPath + "topic.json")
        Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel= gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches
        sharedPrefs?.setIntPrefVal(this@TestSummaryActivity, ConstantPath.TOPIC_SIZE, branchesItemList!!.size)

       // gotoStartScreenThroughAssets(topicname)

        var playCount = databaseHandler!!.getPlayCountPlayRecord(filename)


        var folderPath = localPath+playCount.getTopic()
        Log.e("test fragment","testQuiz.folderPath......"+folderPath)
        jsonStringBasic = loadJSONFromAsset("$folderPath/${playCount.getLevel()}.json")
        Log.e("test fragment","jsonStringBasic......"+jsonStringBasic)
        databaseHandler!!.updatePlayCount(playCount.getPlaycount()+1,playCount.getCourse(),playCount.getTopic(),playCount.getLevel())

        val intent = Intent(this!!, StartTestActivity::class.java)
      //  intent.putExtra(ConstantPath.TOPIC, topic)
        intent.putExtra(ConstantPath.TOPIC_NAME, topicname)
        intent.putExtra(ConstantPath.FOLDER_NAME, playCount.getTopic())
        intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
        intent.putExtra(ConstantPath.COURSE_ID, courseId)
        intent.putExtra(ConstantPath.COURSE_NAME, courseName)
        intent.putExtra(ConstantPath.TOPIC_ID, "")
       // intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
        intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
        intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
        intent.putExtra("LAST_PLAYED", lastplayed)
        intent.putExtra("comingfrom", "Test")
        intent.putExtra("readdata", "assets")
        intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
        intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
        intent.putExtra(ConstantPath.CARD_NO, "")
        intent.putExtra("topicnameoriginal", originaltopicName)
        startActivity(intent)
    }

    private fun readFileLocally(topicname:String) {
        Log.e("test fragment","readFileLocally...."+topicname)
        var filename = ""
        if (topicname.equals("calculus1")) {
            filename = "jee-calculus-1"
        } else if (topicname.equals("calculus2")) {
            filename = "jee-calculus-2"
        } else if (topicname.equals("algebra")) {
            filename = "ii-algebra"
        } else if (topicname.equals("other")) {
            filename = "other"
        } else if (topicname.equals("geometry")) {
            filename = "iii-geometry"
        }
        Log.e("test fragment","on click filename...."+filename)
        val dirFile = File(getCacheDir(),topicname+"/"+filename)
        val courseJsonString = Utils.readFromFile( dirFile.absolutePath + "/Courses.json")
        //val courseJsonString = loadJSONFromAsset( ConstantPath.localBlobcityPath1 + "Courses.json")
        //val courseJsonString = readFromFile("$localBlobcityPath/Courses.json")
        Log.d("courseJsonString",courseJsonString+"!");
        /*val jsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( assetTestCoursePath + "topic.json")*/
        val gsonFile = Gson()
        val courseType = object : TypeToken<List<CoursesResponseModel>>() {}.type
        val courseResponseModel: ArrayList<CoursesResponseModel> = gsonFile
            .fromJson(courseJsonString, courseType)
        courseId = courseResponseModel[0].id
        courseName = courseResponseModel[0].syllabus.title
        // tv_class.text = courseName
        // tv_class_board.text = courseResponseModel[0].syllabus.displayTitle
        localPath = "${dirFile.absolutePath}/$courseName/"
        // val jsonString = readFromFile(localPath +"topic.json")
        val jsonString = Utils.readFromFile( localPath + "topic.json")
        Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel = gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches
        sharedPrefs?.setIntPrefVal(this!!, ConstantPath.TOPIC_SIZE, branchesItemList!!.size)

       // gotoStartScreen()

        var playCount = databaseHandler!!.getPlayCountPlayRecord(filename)


        var folderPath = localPath+playCount.getTopic()
        Log.e("test fragment","testQuiz.folderPath......"+folderPath)
        jsonStringBasic = Utils.readFromFile("$folderPath/${playCount.getLevel()}.json")
        Log.e("test fragment","jsonStringBasic......"+jsonStringBasic)
        databaseHandler!!.updatePlayCount(playCount.getPlaycount()+1,playCount.getCourse(),playCount.getTopic(),playCount.getLevel())

        val intent = Intent(this!!, StartTestActivity::class.java)
        //intent.putExtra(ConstantPath.TOPIC, topic)
        intent.putExtra(ConstantPath.TOPIC_NAME, topicName)
        intent.putExtra(ConstantPath.FOLDER_NAME, playCount.getTopic())
        intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
        intent.putExtra(ConstantPath.COURSE_ID, courseId)
        intent.putExtra(ConstantPath.COURSE_NAME, courseName)
        intent.putExtra(ConstantPath.TOPIC_ID, "")
        //intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
        intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
        intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
        intent.putExtra("LAST_PLAYED", lastplayed)
        intent.putExtra("comingfrom", "Test")
        intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
        intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
        intent.putExtra(ConstantPath.CARD_NO, "")
        intent.putExtra("readdata", "files")
        intent.putExtra("topicnameoriginal", originaltopicName)
        startActivity(intent)

        /*val branchesItemList2 = ArrayList<BranchesItem>()
        val index1 = branchesItemList!![0].topic.index.toString()
        tv_topic_number1.text = index1
        tv_topic_name1.text = branchesItemList!![0].topic.title

        val index2 = branchesItemList!![1].topic.index.toString()
        tv_topic_number2.text = index2
        tv_topic_name2.text = branchesItemList!![1].topic.title
        branchesItemList!!.forEachIndexed { index, branchesItem ->
            if (index>1){
                branchesItemList2.add(branchesItem)
            }
        }*/


        /*rl_chapter_one.setOnClickListener(this)
        rl_chapter_two.setOnClickListener(this)*/
    }

    /*private fun playNextBtnAction(position:Int){
        val dirFile = File(getExternalFilesDir(null),"test/")
        val courseJsonString = Utils.readFromFile( dirFile.absolutePath + "/Courses.json")
        //val courseJsonString = loadJSONFromAsset( ConstantPath.localBlobcityPath1 + "Courses.json")
        //val courseJsonString = readFromFile("$localBlobcityPath/Courses.json")
        Log.d("courseJsonString",courseJsonString+"!");
        *//*val jsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( assetTestCoursePath + "topic.json")*//*
        val gsonFile = Gson()
        val courseType = object : TypeToken<List<CoursesResponseModel>>() {}.type
        val courseResponseModel: ArrayList<CoursesResponseModel> = gsonFile
            .fromJson(courseJsonString, courseType)
        courseId = courseResponseModel[0].id
        courseName = courseResponseModel[0].syllabus.title
        // tv_class.text = courseName
        // tv_class_board.text = courseResponseModel[0].syllabus.displayTitle
        localPath = "${dirFile.absolutePath}/$courseName/"
        // val jsonString = readFromFile(localPath +"topic.json")
        val jsonString = Utils.readFromFile( localPath + "topic.json")
        Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel = gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches


        if((position+1) < branchesItemList!!.size){
            var topic = branchesItemList!![(position+1)].topic
            databaseHandler!!.deleteQuizPlayRecord(topic.title)

            var lastplayed:String =""
            var lastdisplayed = databaseHandler!!.getQuizTopicsLastPlayed(topic.title)
            val folderPath = localPath+topic.folderName
            if(lastdisplayed.equals("NA") || lastdisplayed.equals("intermediate")){
                jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                lastplayed = "basic"
            }else{
                jsonStringBasic = loadJSONFromAsset("$folderPath/intermediate.json")
                lastplayed = "intermediate"
            }

            val intent = Intent(this!!, StartQuizActivityNew::class.java)
            intent.putExtra(ConstantPath.TOPIC, topic)
            intent.putExtra(ConstantPath.TOPIC_NAME, topic.title)
            intent.putExtra(ConstantPath.FOLDER_NAME, topic.folderName)
            intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
            intent.putExtra(ConstantPath.COURSE_ID, courseId)
            intent.putExtra(ConstantPath.COURSE_NAME, courseName)
            intent.putExtra(ConstantPath.TOPIC_ID, topicId)
            intent.putExtra(ConstantPath.TOPIC_POSITION, position)
            intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
            intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
            intent.putExtra("LAST_PLAYED", lastplayed)
            intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
            intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
            intent.putExtra(ConstantPath.CARD_NO, "")
            startActivity(intent)
            finish()



        }else{

            var topic = branchesItemList!![0].topic
            databaseHandler!!.deleteQuizPlayRecord(topic.title)

            var lastplayed:String =""
            var lastdisplayed = databaseHandler!!.getQuizTopicsLastPlayed(topic.title)
            val folderPath = localPath+topic.folderName
            if(lastdisplayed.equals("NA") || lastdisplayed.equals("intermediate")){
                jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                lastplayed = "basic"
            }else{
                jsonStringBasic = loadJSONFromAsset("$folderPath/intermediate.json")
                lastplayed = "intermediate"
            }

            val intent = Intent(this!!, StartQuizActivityNew::class.java)
            intent.putExtra(ConstantPath.TOPIC, topic)
            intent.putExtra(ConstantPath.TOPIC_NAME, topic.title)
            intent.putExtra(ConstantPath.FOLDER_NAME, topic.folderName)
            intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
            intent.putExtra(ConstantPath.COURSE_ID, courseId)
            intent.putExtra(ConstantPath.COURSE_NAME, courseName)
            intent.putExtra(ConstantPath.TOPIC_ID, topicId)
            intent.putExtra(ConstantPath.TOPIC_POSITION, position)
            intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
            intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
            intent.putExtra("LAST_PLAYED", lastplayed)
            intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
            intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
            intent.putExtra(ConstantPath.CARD_NO, "")
            startActivity(intent)
            finish()



        }




    }*/

    fun gotoStartScreenThroughAssets(topictype:String){

        //databaseHandler!!.deleteQuizPlayRecord(topic.title)
        var lastplayed:String =""
        var topic:Topic
        var folderPath:String = ""
        var testQuiz:TestQuiz
        testQuiz = databaseHandler!!.getQuizTopicsForTimerLastPlayed(topictype.toLowerCase())
        Log.e("test fragment","testQuiz.lastplayed......"+testQuiz.lastplayed)
        if(testQuiz.lastplayed == null){
            topic = branchesItemList!![0].topic
            folderPath = localPath+topic.folderName
            Log.e("test fragment","testQuiz.folderPath......"+folderPath)
            jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
            // jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
            lastplayed = "basic"

           // databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

           // databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
        }else{

            if(branchesItemList!!.size == (testQuiz.serialNo).toInt()){
                topic = branchesItemList!![0].topic
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                //jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                lastplayed = "basic"
              //  databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                //databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
            }else{
                if(((testQuiz.serialNo).toInt())-1 < branchesItemList!!.size){
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())-1].topic
                }else{
                    topic = branchesItemList!![0].topic
                }
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                if(testQuiz.lastplayed.equals("basic")){
                    jsonStringBasic = loadJSONFromAsset("$folderPath/intermediate.json")
                    //jsonStringBasic =  Utils.readFromFile("$folderPath/intermediate.json")
                    lastplayed = "intermediate"
                  //  databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                  //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
                }else{
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())].topic
                    folderPath = localPath+topic.folderName
                    Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                    jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                    // jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                    lastplayed = "basic"
                 //   databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                   // databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());

                }
            }



        }


        /*val bundle = Bundle()
        bundle.putString("Category", "Test")
        bundle.putString("Action", "Test")
        bundle.putString("Label", topic.title)
        firebaseAnalytics?.logEvent("Test", bundle)*/

       /* val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topic.title)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
        // bundle.putString("Label", "TestGo")
        firebaseAnalytics?.logEvent("Test", bundle)*/


        Log.e("chapter fragment.....","jsonStringBasic......."+jsonStringBasic);

        val intent = Intent(this!!, StartTestActivity::class.java)
        intent.putExtra(ConstantPath.TOPIC, topic)
        intent.putExtra(ConstantPath.TOPIC_NAME, topictype)
        intent.putExtra(ConstantPath.FOLDER_NAME, topic.folderName)
        intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
        intent.putExtra(ConstantPath.COURSE_ID, courseId)
        intent.putExtra(ConstantPath.COURSE_NAME, courseName)
        intent.putExtra(ConstantPath.TOPIC_ID, "")
        intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
        intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
        intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
        intent.putExtra("LAST_PLAYED", lastplayed)
        intent.putExtra("comingfrom", "Test")
        intent.putExtra("readdata", "assets")
        intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
        intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
        intent.putExtra(ConstantPath.CARD_NO, "")
        intent.putExtra("topicnameoriginal", originaltopicName)
        startActivity(intent)
        //finish()
    }


    fun gotoStartScreen(){

        //databaseHandler!!.deleteQuizPlayRecord(topic.title)
        var lastplayed:String =""
        var topic:Topic
        var folderPath:String = ""
        var testQuiz:TestQuiz
        testQuiz = databaseHandler!!.getQuizTopicsForTimerLastPlayed(topicName!!.toLowerCase())
        Log.e("test fragment","testQuiz.lastplayed......"+testQuiz.lastplayed)
        if(testQuiz.lastplayed == null){
            topic = branchesItemList!![0].topic
            folderPath = localPath+topic.folderName
            jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
            lastplayed = "basic"

           // databaseHandler!!.deleteAllQuizTopicsLatPlayed(testQuiz.testtype)

          //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,testQuiz.testtype);
        }else{

            if(branchesItemList!!.size == (testQuiz.serialNo).toInt()){
                topic = branchesItemList!![0].topic
                folderPath = localPath+topic.folderName
                jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                lastplayed = "basic"
             //   databaseHandler!!.deleteAllQuizTopicsLatPlayed(testQuiz.testtype)

              //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,testQuiz.testtype);
            }else{
                if(((testQuiz.serialNo).toInt())-1 < branchesItemList!!.size){
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())-1].topic
                }else{
                    topic = branchesItemList!![0].topic
                }
                folderPath = localPath+topic.folderName
                if(testQuiz.lastplayed.equals("basic")){
                    jsonStringBasic =  Utils.readFromFile("$folderPath/intermediate.json")
                    lastplayed = "intermediate"
                //    databaseHandler!!.deleteAllQuizTopicsLatPlayed(testQuiz.testtype)

                 //   databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,testQuiz.testtype);
                }else{
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())].topic
                    folderPath = localPath+topic.folderName

                    jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                    lastplayed = "basic"
                  //  databaseHandler!!.deleteAllQuizTopicsLatPlayed(testQuiz.testtype)

                  //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,testQuiz.testtype);

                }
            }



        }




        Log.e("chapter fragment.....","jsonStringBasic......."+jsonStringBasic);

        val intent = Intent(this!!, StartTestActivity::class.java)
        intent.putExtra(ConstantPath.TOPIC, topic)
        intent.putExtra(ConstantPath.TOPIC_NAME, topicName)
        intent.putExtra(ConstantPath.FOLDER_NAME, topic.folderName)
        intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
        intent.putExtra(ConstantPath.COURSE_ID, courseId)
        intent.putExtra(ConstantPath.COURSE_NAME, courseName)
        intent.putExtra(ConstantPath.TOPIC_ID, "")
        intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
        intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
        intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
        intent.putExtra("LAST_PLAYED", lastplayed)
        intent.putExtra("comingfrom", "Test")
        intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
        intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
        intent.putExtra(ConstantPath.CARD_NO, "")
        intent.putExtra("readdata", "files")
        intent.putExtra("topicnameoriginal", originaltopicName)
        startActivity(intent)
        //finish()
    }


    override fun onBackPressed() {
        super.onBackPressed()

        if(comingfrom.equals("Home")){
            val intent = Intent(this, DashBoardActivity::class.java)
            intent.putExtra("fragment","Home")
            startActivity(intent)
            finish()
        }else{
            val intent = Intent(this, DashBoardActivity::class.java)
            intent.putExtra("fragment","tests")
            startActivity(intent)
            finish()
        }
    }
}
