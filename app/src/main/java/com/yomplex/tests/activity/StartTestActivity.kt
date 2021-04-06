package com.yomplex.tests.activity

import android.content.Intent
import android.os.Build

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView


import com.blobcity.viewmodel.TopicStatusVM
import com.bumptech.glide.Glide
import com.google.gson.Gson

import android.widget.LinearLayout

import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.ViewModelProviders


import com.google.firebase.analytics.FirebaseAnalytics
import com.yomplex.tests.R
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.model.Topic
import com.yomplex.tests.model.TopicOneBasicResponseModel
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import kotlinx.android.synthetic.main.activity_start_quiz_timer.*


class StartTestActivity : BaseActivity(), View.OnClickListener {


    var courseId: String? = ""
    var topicId: String? = ""
    var topicLevel: String? = ""
    var complete: String? = ""
    var topicStatusVM: TopicStatusVM? = null
    var dynamicPath: String? = null
    var folderName: String? = null
    var gradeTitle: String? = null
    var readyCardNumber = 0
    var courseName: String? = ""
    var topicName: String? = ""
    var readdata: String? = null
    var originaltopicName: String? = ""
    var lastplayed:String = ""
    var comingfrom:String = ""
    //var dbPosition: Int? = null
    var oPath: String? = null
    private var totalQuestion: Int? = null
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    //lateinit var topic: Topic
    lateinit var circles: Array<ImageView?>
    var mLastClickTime:Long = 0;
    //private lateinit var firebaseAnalytics: FirebaseAnalytics
    var databaseHandler: QuizGameDataBase?= null
    override var layoutID: Int = R.layout.activity_start_quiz_timer

    override fun initView() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorbottomnav));
        }
        sharedPrefs = SharedPrefs()
       // topic = intent.getSerializableExtra(ConstantPath.TOPIC) as Topic
        topicStatusVM = ViewModelProviders.of(this).get(TopicStatusVM::class.java)
        dynamicPath = intent.getStringExtra(ConstantPath.DYNAMIC_PATH)
        courseId = intent.getStringExtra(ConstantPath.COURSE_ID)
        topicId = intent.getStringExtra(ConstantPath.TOPIC_ID)
        courseName = intent.getStringExtra(ConstantPath.COURSE_NAME)
        topicLevel = intent.getStringExtra(ConstantPath.TOPIC_LEVEL)
        topicName = intent.getStringExtra(ConstantPath.TOPIC_NAME)
        complete = intent.getStringExtra(ConstantPath.LEVEL_COMPLETED)
       // dbPosition = intent.getIntExtra(ConstantPath.TOPIC_POSITION, -1)
        oPath = intent.getStringExtra(ConstantPath.FOLDER_PATH)
        folderName = intent.getStringExtra(ConstantPath.FOLDER_NAME)
        gradeTitle = intent.getStringExtra(ConstantPath.TITLE_TOPIC)
      //  readyCardNumber = intent.getIntExtra(ConstantPath.CARD_NO, 0)
        lastplayed = intent.getStringExtra("LAST_PLAYED")!!
        comingfrom = intent.getStringExtra("comingfrom")!!
        originaltopicName = intent.getStringExtra("topicnameoriginal")!!
        readdata = intent.getStringExtra("readdata")
        val gsonFile = Gson()
        val questionResponseModel = gsonFile.fromJson(dynamicPath, TopicOneBasicResponseModel::class.java)
        val questionsItems = questionResponseModel.questions
        val listWithDuplicateKeys = ArrayList<String>()

        totalQuestion = questionResponseModel.questionCount
        //firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        /*Log.e("start test","last played....."+lastplayed)
        Log.e("start test","topic.title....."+topic.title)
        Log.e("start test","topic.displayNo....."+topic.displayNo)*/
        Log.e("start test","topicName....."+topicName)


        databaseHandler = QuizGameDataBase(this);

        tv_quiz_title.text = originaltopicName

        circles = arrayOfNulls<ImageView>(totalQuestion!!)
        ll_answers.removeAllViews()
        for(i in 0 until totalQuestion!!){
            val params = LinearLayout.LayoutParams(getResources().getDimension(R.dimen._18sdp).toInt(), getResources().getDimension(R.dimen._18sdp).toInt())
            if(i != 0){
                params.leftMargin = getResources().getDimension(R.dimen._17sdp).toInt()
            }

            circles[i] = ImageView(this)

            Glide.with(this)
                .load(R.drawable.empty_circle)
                .into(circles[i]!!)

            circles[i]!!.layoutParams = params
            ll_answers.addView(circles!![i])
        }

        btn_topics!!.setOnClickListener(this)
        btn_start!!.setOnClickListener(this)


    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(comingfrom.equals("Test")){
            val intent = Intent(this, DashBoardActivity::class.java)
            intent.putExtra("fragment","tests")
            startActivity(intent)
            finish()
        }else{
            finish()
        }
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
                if(comingfrom.equals("Test")){
                    val intent = Intent(this, DashBoardActivity::class.java)
                    intent.putExtra("fragment","tests")
                    startActivity(intent)
                    finish()
                }else{
                    finish()
                }

            }
            R.id.btn_start -> {
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

                Log.e("start test","on click...topicname...."+topicName);
                databaseHandler!!.deleteAllQuizTopicsLatPlayed(topicName!!.toLowerCase())

               databaseHandler!!.insertquiztopiclastplayed(folderName,0,lastplayed,topicName!!.toLowerCase());
                var filename = ""
                if (originaltopicName.equals("CALCULUS 1")) {
                    filename = "jee-calculus-1"
                    //firestoreversionkey = "Calculus1Version"
                } else if (originaltopicName.equals("CALCULUS 2")) {
                    filename = "jee-calculus-2"
                    //firestoreversionkey = "Calculus2Version"
                } else if (originaltopicName.equals("ALGEBRA")) {
                    filename = "ii-algebra"
                    //firestoreversionkey = "AlgebraVersion"
                } else if (originaltopicName.equals("OTHER")) {
                    filename = "other"
                    //firestoreversionkey = "BasicVersion"
                } else if (originaltopicName.equals("GEOMETRY")) {
                    filename = "iii-geometry"
                    //firestoreversionkey = "GeometryVersion"
                }
                Log.e("start test","readdata......"+readdata)
                if(readdata.equals("files")){
                    var playCount = databaseHandler!!.getPlayCountPlayRecord(filename)
                    Log.e("start test","playCount.getCourse()......"+playCount.getCourse())
                    Log.e("start test","playCount.getTopic()......"+playCount.getTopic())
                    Log.e("start test","playCount.getLevel()......"+playCount.getLevel())
                    databaseHandler!!.updatePlayCount(playCount.getPlaycount()+1,playCount.getCourse(),playCount.getTopic(),playCount.getLevel())
                }else{
                    var playCount = databaseHandler!!.getPlayCountPlayRecord(filename)
                    databaseHandler!!.updatePlayCount(playCount.getPlaycount()+1,playCount.getCourse(),playCount.getTopic(),playCount.getLevel())
                }

                /*val bundle = Bundle()
                bundle.putString("Category", "Test")
                bundle.putString("Action", "Launch")
                bundle.putString("Label", topic.title)
                firebaseAnalytics?.logEvent("TestLaunch", bundle)*/

                /*val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topic.title)
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
                // bundle.putString("Label", "TestGo")
                firebaseAnalytics?.logEvent("Launch", bundle)*/

                val intent = Intent(this!!, TestQuizActivity::class.java)
                //intent.putExtra(ConstantPath.TOPIC, topic)
                intent.putExtra(ConstantPath.TOPIC_NAME, topicName)
                intent.putExtra(ConstantPath.FOLDER_NAME, folderName)
                intent.putExtra(ConstantPath.DYNAMIC_PATH, dynamicPath)
                intent.putExtra(ConstantPath.COURSE_ID, courseId)
                intent.putExtra(ConstantPath.COURSE_NAME, courseName)
                intent.putExtra(ConstantPath.TOPIC_ID, topicId)
               // intent.putExtra(ConstantPath.TOPIC_POSITION, dbPosition)
                intent.putExtra(ConstantPath.FOLDER_PATH, oPath)
                intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
                intent.putExtra("LAST_PLAYED", lastplayed)
                intent.putExtra("comingfrom", comingfrom)
                intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
                intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
                intent.putExtra(ConstantPath.CARD_NO, "")
                intent.putExtra("readdata", readdata)
               // intent.putExtra("DISPLAY_NO", topic.displayNo)
                intent.putExtra("topicnameoriginal", originaltopicName)
                startActivity(intent)


            }
        }
    }

}
