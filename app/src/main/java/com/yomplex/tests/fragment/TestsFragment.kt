package com.yomplex.tests.fragment

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock

import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.Fill


import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yomplex.tests.R
import com.yomplex.tests.Service.ContentDownloadService
import com.yomplex.tests.Service.JobService
import com.yomplex.tests.Service.ProgressJobService
import com.yomplex.tests.Service.ProgressJobService.SHOW_RESULT
import com.yomplex.tests.Service.ServiceResultReceiver
import com.yomplex.tests.activity.ContentVersionUpdateService
import com.yomplex.tests.activity.DashBoardActivity
import com.yomplex.tests.activity.TestReviewActivity
import com.yomplex.tests.activity.StartTestActivity
import com.yomplex.tests.adapter.TestsAdapter
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.interfaces.TestClickListener
import com.yomplex.tests.interfaces.TestQuizReviewClickListener
import com.yomplex.tests.model.*
import com.yomplex.tests.utils.*
import com.yomplex.tests.utils.ConstantPath.TOPIC_SIZE
import kotlinx.android.synthetic.main.activity_test_quiz.*
import kotlinx.android.synthetic.main.tests_challenge.*
import kotlinx.android.synthetic.main.tests_challenge.view.*
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TestsFragment: Fragment(),View.OnClickListener, TestClickListener,
    ServiceResultReceiver.Receiver {



    private var testItemList:ArrayList<TestsModel>?=null
    lateinit var testmodel:TestsModel
    var adapter: TestsAdapter?= null
    var databaseHandler: QuizGameDataBase?= null
    //lateinit var  mSoundManager: SoundManager;
    var jsonStringBasic: String? =""
    var courseId: String?=""
    var courseName: String?=""
    var localPath: String?= null
    private var branchesItemList:List<BranchesItem>?=null
    var testscorelist: ArrayList<QuizScore>? = null
    var testtypeslist: ArrayList<Course>? = null
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    var gradeTitle: String?= null
    var version : String = ""
    var url : String = ""
    var alertDialog: AlertDialog? = null
    private var mServiceResultReceiver: ServiceResultReceiver? = null
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var isdownload:Boolean = false
    internal var mYear: Int = 0
    internal var mMonth:Int = 0
    internal var mDay:Int = 0
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    var dialog: Dialog? = null;
    protected lateinit var tfRegular: Typeface
    protected lateinit var tfLight: Typeface
    var mLastClickTime:Long = 0;
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tests_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gradeTitle = arguments!!.getString(ConstantPath.TITLE_TOPIC)!!
        view.tests.elevation = 15F
        sharedPrefs = SharedPrefs()
        /*val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings*/

        mServiceResultReceiver = ServiceResultReceiver(Handler())
        mServiceResultReceiver?.setReceiver(this)
        databaseHandler = QuizGameDataBase(context);

        //firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!)
        firebaseAnalytics = Firebase.analytics
       // firebaseAnalytics.setCurrentScreen(activity!!, "Test", null /* class override */)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "TestsTab")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "TestsFragment")
        }
        //view.test_btn.setOnClickListener(this)


        /*var downloadstatus = databaseHandler!!.gettesttopicdownloadstatus()
        if(downloadstatus == 1) {
            readFileLocally()
        }*/
        Log.e("test fragment","getWeekOfYear()..........."+getWeekOfYear());
       // Log.e("test fragment","getWeekStartDate()..........."+getWeekStartDate());
       // Log.e("test fragment","getWeekEndDate()..........."+getWeekEndDate());


        var totalScore:Int = 0
        testscorelist = databaseHandler!!.getScoresForCurrentWeek(getWeekOfYear())
        for(i in 0 until testscorelist!!.size) {
            totalScore = totalScore + (testscorelist!!.get(i).highestscore).toInt()
        }
        view.tv_txt3.setText(" "+totalScore+" / 20")
        //testtypeslist = ArrayList<Course>()


        /*testtypeslist!!.add("ALGEBRA")
        testtypeslist!!.add("GEOMETRY")
        testtypeslist!!.add("CALCULUS 1")
        testtypeslist!!.add("CALCULUS 2")
        testtypeslist!!.add("OTHER")*/
        /*if(testquizlist!!.size == 0){
            view.tv_no_review.visibility = View.VISIBLE
            view.rcv_tests.visibility = View.GONE
        }else{*/
            view.rcv_tests.visibility = View.VISIBLE
            view.tv_no_review.visibility = View.GONE

        testtypeslist = databaseHandler!!.getAllCourses()
        Log.e("test fragment","test type list...size..."+testtypeslist!!.size)
        if(testtypeslist!!.size > 0){
            adapter = TestsAdapter(context!!, testtypeslist!!,testscorelist!!,this)
        }else{
            try{
                var testtypeslist1: ArrayList<String>? = null
                testtypeslist1 = ArrayList<String>()


                testtypeslist1!!.add("ALGEBRA")
                testtypeslist1!!.add("GEOMETRY")
                testtypeslist1!!.add("CALCULUS 1")
                testtypeslist1!!.add("CALCULUS 2")
                testtypeslist1!!.add("OTHER")
                var count = databaseHandler!!.getAllCoursesCount()
                Log.e("test fragment","count..size..."+count)
                if(count == 0){
                    for(i in 0 until testtypeslist1.size){
                        databaseHandler!!.insertCourse(Course(testtypeslist1.get(i),0))
                    }
                }

                testtypeslist = databaseHandler!!.getAllCourses()
                adapter = TestsAdapter(context!!, testtypeslist!!,testscorelist!!,this)
            }catch (e:Exception){

            }
        }





            view.rcv_tests.addItemDecoration(VerticalSpaceItemDecoration(30));
            //rcv_chapter.addItemDecoration(itemDecorator)
            //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
            view.rcv_tests.adapter = adapter
      //  }

        //uncomment it for enable date(testing purpose)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

        view.tv_date.setText(""+format.format(Utils.date))
        view.dateRL.setOnClickListener(this)
        view.infoRl.setOnClickListener(this)

        //code for bar chart
        //BarChart barChart = (BarChart) findViewById(R.id.barchart);

        view.barchart.getDescription().setEnabled(false)

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        view.barchart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        view.barchart.setPinchZoom(false)

        view.barchart.setDrawBarShadow(false)
        view.barchart.setDrawGridBackground(false)

        tfRegular = Typeface.createFromAsset(activity!!.getAssets(), "lato_black.ttf")
        tfLight = Typeface.createFromAsset(activity!!.getAssets(), "lato_black.ttf")




        val xAxisFormatter: IAxisValueFormatter = MyAxisValueFormatter1()

        val xAxis = view.barchart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.labelRotationAngle = 45f
        xAxis.textColor = getResources().getColor(R.color.chart_text_color)
        xAxis.setTypeface(tfLight)
        xAxis.axisLineColor = getResources().getColor(R.color.chart_axis_color)
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f) // only intervals of 1 day
        xAxis.setLabelCount(7)
        xAxis.setValueFormatter(xAxisFormatter)

        val custom = MyAxisValueFormatter()

        val leftAxis = view.barchart.getAxisLeft()
        leftAxis.setTypeface(tfLight)
        leftAxis.textColor = getResources().getColor(R.color.chart_text_color)
        leftAxis.setDrawGridLinesBehindData(true)
        leftAxis.setLabelCount(5, false)
        leftAxis.setValueFormatter(custom)
        leftAxis.axisLineColor = getResources().getColor(R.color.chart_axis_color)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.setSpaceTop(15f)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        leftAxis.axisMaximum = 20f

        val rightAxis = view.barchart.getAxisRight()
        rightAxis.setDrawGridLines(true)
        rightAxis.textColor = getResources().getColor(R.color.chart_text_color)
        rightAxis.axisLineColor = getResources().getColor(R.color.chart_axis_color)
        rightAxis.isGranularityEnabled = false
        rightAxis.setTypeface(tfLight)
        rightAxis.setLabelCount(5, false)
        rightAxis.setValueFormatter(custom)
        rightAxis.setSpaceTop(15f)
        rightAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        rightAxis.axisMaximum = 20f

        val l = view.barchart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT)
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL)
        l.setDrawInside(false)
        l.setForm(Legend.LegendForm.SQUARE)
        l.setFormSize(9f)
        l.setTextSize(11f)
        l.setXEntrySpace(4f)
        var  entries1:ArrayList<LegendEntry> = ArrayList<LegendEntry>();
        l.setCustom(entries1)

         var  entries:ArrayList<BarEntry> = ArrayList<BarEntry>();
        val gradientFills = ArrayList<Fill>()
         var count = 0
         for (n in 5 downTo 1) {
             Log.e("test fragment","n value......."+n);
             count++
             var total = databaseHandler!!.getWeekTotalScore(getWeekOfYear()-(n-1));
             Log.e("test fragment","count value......."+count);
             entries.add(BarEntry( count.toFloat(),total.toFloat()))
             if(total >= 0 && total <=7){
                 //gradientFills
                 gradientFills.add(Fill(getResources().getColor(R.color.chart_orange_color)))
             }else if(total >= 8 && total <=11){
                 gradientFills.add(Fill(getResources().getColor(R.color.chart_purple_color)))
             }else if(total >= 12 && total <=15){
                 gradientFills.add(Fill(getResources().getColor(R.color.chart_blue_color)))
             }else if(total >= 16 && total <=20){
                 gradientFills.add(Fill(getResources().getColor(R.color.chart_green_color)))
             }
             /*else{
                 gradientFills.add(Fill(getResources().getColor(R.color.chart_color)))
             }*/
         }


        var  bardataset: BarDataSet = BarDataSet(entries, "");
        bardataset.setFills(gradientFills)
        val COLORFUL_COLORS = intArrayOf(
            Color.rgb(193, 37, 82),
            Color.rgb(255, 102, 0),
            Color.rgb(245, 199, 0),
            Color.rgb(106, 150, 31),
            Color.rgb(179, 100, 53)
        )
        view.barchart.getAxisRight().setEnabled(false)
        view.barchart.getAxisLeft().setDrawGridLines(true)
        view.barchart.getAxisLeft().gridColor = getResources().getColor(R.color.chart_axis_color)
        //view.barchart.setNoDataText(noDataText)
        val dataSets = java.util.ArrayList<IBarDataSet>()
        dataSets.add(bardataset)

        val data = BarData(dataSets)
        data.setDrawValues(false)
        data.isHighlightEnabled = false
        data.setValueTextColor(getResources().getColor(R.color.chart_color))
        data.setValueTextSize(10f)
        data.setValueTypeface(tfLight)
        data.barWidth = 0.5f

        view.barchart.setBorderWidth(20f)
       // var  data = BarData(bardataset);
        view.barchart.setData(data); // set the data and list of labels into chart
       // view.barchart.setDescription("Set Bar Chart Description Here");  // set the description
        //bardataset.setColors(COLORFUL_COLORS.toMutableList());
        view.barchart.animateY(0);
        try {
            val mSensorService = ContentVersionUpdateService()
            val mServiceIntent = Intent(activity, mSensorService::class.java)
            if (!Utils.isMyServiceRunning(activity, mSensorService::class.java)) {
                activity!!.startService(mServiceIntent)
            }
        } catch (e: Exception) {

        }


        val format1 = SimpleDateFormat("yyyy-MM-dd")

        var dbdate = databaseHandler!!.getContentDate()
        if(dbdate != null){
            try{
                Log.e("tests fragment","db date......"+dbdate)
                Log.e("tests fragment","Utils.date......"+Utils.date)
                Log.e("tests fragment","format1.parse(dbdate)......"+format1.parse(dbdate))
                var s = format1.format(Utils.date)
                Log.e("tests fragment","format1.parse(s)......"+format1.parse(s))


                if(format1.parse(dbdate) < format1.parse(s)){
                    val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                    val isConnected: Boolean = activeNetwork?.isConnected == true
                    Log.d("isConnected",isConnected.toString()+"!")
                    if(isNetworkConnected()) {
                        downloadServiceFromBackground(activity!!,db)
                    }

                }
            }catch (e:Exception){

            }

        }else{
            val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected: Boolean = activeNetwork?.isConnected == true
            Log.d("isConnected",isConnected.toString()+"!")
            if(isNetworkConnected()) {
                downloadServiceFromBackground(activity!!,db)
            }
        }



    }
    fun getWeekOfYear(): Int{
        val calendar = Calendar.getInstance()
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(Utils.date)
        return calendar.get(Calendar.WEEK_OF_YEAR)
    }

    /*fun getWeekStartDate(): Date {
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
    }*/
    private fun readFileFromAssetsNew(topicname: String,filename:String,originalfilename:String){
        //Toast.makeText(activity,"content read from assets",Toast.LENGTH_SHORT).show()
        val courseJsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( topicname+"/"+filename+"/" + "Courses.json")
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
        val jsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( localPath + "topic.json")
        Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel= gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches
        sharedPrefs?.setIntPrefVal(context!!,TOPIC_SIZE, branchesItemList!!.size)




        var playCount = databaseHandler!!.getPlayCountPlayRecord(filename)


        var folderPath = localPath+playCount.getTopic()
        Log.e("test fragment","testQuiz.folderPath......"+folderPath)
        jsonStringBasic = loadJSONFromAsset("$folderPath/${playCount.getLevel()}.json")
        Log.e("test fragment","jsonStringBasic......"+jsonStringBasic)

        //gotoStartScreenThroughAssets(topicname,originalfilename)
        val intent = Intent(context!!, StartTestActivity::class.java)
        //intent.putExtra(ConstantPath.TOPIC, topic)
        intent.putExtra(ConstantPath.TOPIC_NAME, topicname)
        intent.putExtra("topicnameoriginal", originalfilename)
        intent.putExtra(ConstantPath.FOLDER_NAME, playCount.getTopic())
        intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
        intent.putExtra(ConstantPath.COURSE_ID, courseId)
        intent.putExtra(ConstantPath.COURSE_NAME, courseName)
        intent.putExtra(ConstantPath.TOPIC_ID, "")
        //intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
        intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
        intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
        intent.putExtra("LAST_PLAYED", playCount.getLevel())
        intent.putExtra("comingfrom", "Test")
        intent.putExtra("readdata", "assets")
        intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
        intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
        intent.putExtra(ConstantPath.CARD_NO, "")
        startActivity(intent)




    }
    private fun readFileFromAssets(topicname: String,filename:String,originalfilename:String){
        //Toast.makeText(activity,"content read from assets",Toast.LENGTH_SHORT).show()
        val courseJsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( topicname+"/"+filename+"/" + "Courses.json")
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
        val jsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( localPath + "topic.json")
        Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel= gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches
        sharedPrefs?.setIntPrefVal(context!!,TOPIC_SIZE, branchesItemList!!.size)

        gotoStartScreenThroughAssets(topicname,originalfilename)
    }


    private fun readFileLocally(topicname:String,filename:String,originalfilename:String,count:Int) {
        //Toast.makeText(activity,"content read from local storage",Toast.LENGTH_SHORT).show()
        val dirFile = File(activity!!.getCacheDir(),topicname+"/"+filename)
        Log.e("test fragment","dir file....."+dirFile.absolutePath)
        val courseJsonString = Utils.readFromFile( dirFile.absolutePath + "/Courses.json")
        //val courseJsonString = readFromFile("$localBlobcityPath/Courses.json")
        Log.d("courseJsonString....1",courseJsonString+"!");
        //val courseJsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( ConstantPath.localBlobcityPath1 + "Courses.json")
        //val courseJsonString = readFromFile("$localBlobcityPath/Courses.json")
        //Log.d("courseJsonString",courseJsonString+"!");
        /*val jsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( assetTestCoursePath + "topic.json")*/
        val gsonFile = Gson()
        val courseType = object : TypeToken<List<CoursesResponseModel>>() {}.type
        try {
            val courseResponseModel: ArrayList<CoursesResponseModel> = gsonFile.fromJson(courseJsonString, courseType)
            courseId = courseResponseModel[0].id
            courseName = courseResponseModel[0].syllabus.title

        }catch (e:Exception){

        }
        // tv_class.text = courseName
        // tv_class_board.text = courseResponseModel[0].syllabus.displayTitle
        localPath = "${dirFile.absolutePath}/$courseName/"
        // val jsonString = readFromFile(localPath +"topic.json")
        val jsonString = Utils.readFromFile( localPath + "topic.json")
        //Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel = gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches
        sharedPrefs?.setIntPrefVal(context!!, ConstantPath.TOPIC_SIZE, branchesItemList!!.size)

        //gotoStartScreen(topicname,originalfilename)

        var playCount = databaseHandler!!.getPlayCountPlayRecord(filename)
        Log.e("testfragment","playCount.getCourse()......"+playCount.getCourse())
        Log.e("testfragment","playCount.getTopic()......"+playCount.getTopic())
        Log.e("testfragment","playCount.getLevel()......"+playCount.getLevel())

        var folderPath = localPath+playCount.getTopic()
        Log.e("test fragment","testQuiz.folderPath......"+folderPath)
        var pathexist = File(folderPath)
        if(pathexist.exists()){
            jsonStringBasic = Utils.readFromFile("$folderPath/${playCount.getLevel()}.json")
            Log.e("test fragment","jsonStringBasic......"+jsonStringBasic)

            //databaseHandler!!.updatePlayCount(playCount.getPlaycount()+1,playCount.getCourse(),playCount.getTopic(),playCount.getLevel())
            val intent = Intent(context!!, StartTestActivity::class.java)
            //intent.putExtra(ConstantPath.TOPIC, topic)
            intent.putExtra(ConstantPath.TOPIC_NAME, topicname)
            intent.putExtra("topicnameoriginal", originalfilename)
            intent.putExtra(ConstantPath.FOLDER_NAME, playCount.getTopic())
            intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
            intent.putExtra(ConstantPath.COURSE_ID, courseId)
            intent.putExtra(ConstantPath.COURSE_NAME, courseName)
            intent.putExtra(ConstantPath.TOPIC_ID, "")
            //intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
            intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
            intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
            intent.putExtra("LAST_PLAYED", playCount.getLevel())
            intent.putExtra("comingfrom", "Test")
            intent.putExtra("readdata", "files")
            intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
            intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
            intent.putExtra(ConstantPath.CARD_NO, "")
            startActivity(intent)
        }else{

            if(count == 0){
                databaseHandler!!.updateCourseExist(originalfilename,1)
                databaseHandler!!.updatetestcontentdownloadstatus(0,topicname)
                if(isNetworkConnected()) {
                    downloadServiceFromBackground(activity!!,db)
                }

                nocontentDialog("Oops! Something went wrong. Please wait while we update this Topic.")
            }else{
                if(isNetworkConnected()) {
                    databaseHandler!!.updateCourseExist(originalfilename,1)
                    nocontentDialog("Give us another minute.. your content is being updated!")
                }else{
                    nocontentDialog("No internet connection "+ getString(R.string.emoji_disappointment) + " Please connect to the internet and try again.")

                }
            }

            //readFileFromAssetsNew(topicname.toLowerCase(),filename,originalfilename)
        }






        /*var allrecordscount = databaseHandler!!.getAllPlayCountFile();
        if(allrecordscount == 0){

            val dirFile1 = File(dirFile,"/coursetestinfo.json")
            if(dirFile1.exists()){

                try{
                    val dirFile2 = File(activity!!.getCacheDir(),"algebra/ii-algebra/" + "coursetestinfo.json")
                    if(dirFile2.exists()){
                        val courseJsonString = Utils.readFromFile(dirFile2.absolutePath)
                        val gsonFile = Gson()
                        val courseType = object : com.google.common.reflect.TypeToken<List<PlayCount>>() {}.type
                        val playCountmodel: java.util.ArrayList<PlayCount> = gsonFile
                            .fromJson(courseJsonString, courseType)
                        for(i in 0 until playCountmodel.size){
                            val playCount = playCountmodel[i]
                            val count = databaseHandler!!.getPlayCountFile(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                            if (count == 0) {
                                Log.e("dash board","count......."+count)
                                databaseHandler!!.insertPlayCountFile(playCount)
                            }
                        }
                    }


                    val dirFile3 = File(activity!!.getCacheDir(),"calculus1/jee-calculus-1/" + "coursetestinfo.json")
                    if(dirFile3.exists()){
                        val courseJsonString1 = Utils.readFromFile( dirFile3.absolutePath)
                        //val gsonFile1 = Gson()
                        //val courseType1 = object : TypeToken<List<PlayCount>>() {}.type
                        val playCountmodel1: java.util.ArrayList<PlayCount> = gsonFile
                            .fromJson(courseJsonString1, courseType)
                        for(i in 0 until playCountmodel1.size){
                            val playCount = playCountmodel1[i]
                            val count = databaseHandler!!.getPlayCountFile(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                            if (count == 0) {
                                databaseHandler!!.insertPlayCountFile(playCount)
                            }
                        }
                    }


                    val dirFile4 = File(activity!!.getCacheDir(),"calculus2/jee-calculus-2/" + "coursetestinfo.json")
                    if(dirFile4.exists()){
                        val courseJsonString2 = Utils.readFromFile( dirFile4.absolutePath)
                        // val gsonFile = Gson()
                        // val courseType = object : TypeToken<List<PlayCount>>() {}.type
                        val playCountmodel2: java.util.ArrayList<PlayCount> = gsonFile
                            .fromJson(courseJsonString2, courseType)
                        for(i in 0 until playCountmodel2.size){
                            val playCount = playCountmodel2[i]
                            val count = databaseHandler!!.getPlayCountFile(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                            if (count == 0) {
                                databaseHandler!!.insertPlayCountFile(playCount)
                            }
                        }
                    }


                    val dirFile5 = File(activity!!.getCacheDir(),"geometry/iii-geometry/" + "coursetestinfo.json")
                    if(dirFile5.exists()){
                        val courseJsonString3 = Utils.readFromFile( dirFile5.absolutePath)
                        //val gsonFile = Gson()
                        // val courseType = object : TypeToken<List<PlayCount>>() {}.type
                        val playCountmodel3: java.util.ArrayList<PlayCount> = gsonFile
                            .fromJson(courseJsonString3, courseType)
                        for(i in 0 until playCountmodel3.size){
                            val playCount = playCountmodel3[i]
                            val count = databaseHandler!!.getPlayCountFile(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                            if (count == 0) {
                                databaseHandler!!.insertPlayCountFile(playCount)
                            }
                        }
                    }

                    val dirFile6 = File(activity!!.getCacheDir(),"other/other/" + "coursetestinfo.json")
                    if(dirFile6.exists()){
                        val courseJsonString4 = Utils.readFromFile( dirFile6.absolutePath)
                        // val gsonFile = Gson()
                        // val courseType = object : TypeToken<List<PlayCount>>() {}.type
                        val playCountmodel4: java.util.ArrayList<PlayCount> = gsonFile
                            .fromJson(courseJsonString4, courseType)
                        for(i in 0 until playCountmodel4.size){
                            val playCount = playCountmodel4[i]
                            val count = databaseHandler!!.getPlayCountFile(playCount.getCourse(), playCount.getTopic(),playCount.getLevel())
                            if (count == 0) {
                                databaseHandler!!.insertPlayCountFile(playCount)
                            }
                        }
                    }

                    var playCount = databaseHandler!!.getPlayCountPlayRecordFile(filename)
                    Log.e("testfragment","playCount.getCourse()......"+playCount.getCourse())
                    Log.e("testfragment","playCount.getTopic()......"+playCount.getTopic())
                    Log.e("testfragment","playCount.getLevel()......"+playCount.getLevel())

                    var folderPath = localPath+playCount.getTopic()
                    Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                    jsonStringBasic = Utils.readFromFile("$folderPath/${playCount.getLevel()}.json")
                    Log.e("test fragment","jsonStringBasic......"+jsonStringBasic)

                    //databaseHandler!!.updatePlayCount(playCount.getPlaycount()+1,playCount.getCourse(),playCount.getTopic(),playCount.getLevel())
                    val intent = Intent(context!!, StartTestActivity::class.java)
                    //intent.putExtra(ConstantPath.TOPIC, topic)
                    intent.putExtra(ConstantPath.TOPIC_NAME, topicname)
                    intent.putExtra("topicnameoriginal", originalfilename)
                    intent.putExtra(ConstantPath.FOLDER_NAME, playCount.getTopic())
                    intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
                    intent.putExtra(ConstantPath.COURSE_ID, courseId)
                    intent.putExtra(ConstantPath.COURSE_NAME, courseName)
                    intent.putExtra(ConstantPath.TOPIC_ID, "")
                    //intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
                    intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
                    intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
                    intent.putExtra("LAST_PLAYED", playCount.getLevel())
                    intent.putExtra("comingfrom", "Test")
                    intent.putExtra("readdata", "files")
                    intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
                    intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
                    intent.putExtra(ConstantPath.CARD_NO, "")
                    startActivity(intent)


                }catch (e:Exception){

                }

            }else{
                databaseHandler!!.updatetestcontentdownloadstatus(0, "algebra")
                databaseHandler!!.updatetestcontentdownloadstatus(0, "calculus1")
                databaseHandler!!.updatetestcontentdownloadstatus(0, "geometry")
                databaseHandler!!.updatetestcontentdownloadstatus(0, "other")
                databaseHandler!!.updatetestcontentdownloadstatus(0, "calculus2")
                readFileFromAssetsNew(topicname,filename,originalfilename)
            }

        }else{
            var playCount = databaseHandler!!.getPlayCountPlayRecordFile(filename)
            Log.e("testfragment","playCount.getCourse()......"+playCount.getCourse())
            Log.e("testfragment","playCount.getTopic()......"+playCount.getTopic())
            Log.e("testfragment","playCount.getLevel()......"+playCount.getLevel())

            var folderPath = localPath+playCount.getTopic()
            Log.e("test fragment","testQuiz.folderPath......"+folderPath)
            jsonStringBasic = Utils.readFromFile("$folderPath/${playCount.getLevel()}.json")
            Log.e("test fragment","jsonStringBasic......"+jsonStringBasic)

            //databaseHandler!!.updatePlayCount(playCount.getPlaycount()+1,playCount.getCourse(),playCount.getTopic(),playCount.getLevel())
            val intent = Intent(context!!, StartTestActivity::class.java)
            //intent.putExtra(ConstantPath.TOPIC, topic)
            intent.putExtra(ConstantPath.TOPIC_NAME, topicname)
            intent.putExtra("topicnameoriginal", originalfilename)
            intent.putExtra(ConstantPath.FOLDER_NAME, playCount.getTopic())
            intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
            intent.putExtra(ConstantPath.COURSE_ID, courseId)
            intent.putExtra(ConstantPath.COURSE_NAME, courseName)
            intent.putExtra(ConstantPath.TOPIC_ID, "")
            //intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
            intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
            intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
            intent.putExtra("LAST_PLAYED", playCount.getLevel())
            intent.putExtra("comingfrom", "Test")
            intent.putExtra("readdata", "files")
            intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
            intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
            intent.putExtra(ConstantPath.CARD_NO, "")
            startActivity(intent)
        }*/
    }

    private fun nocontentDialog(msg:String) {
        val dialogBuilder = AlertDialog.Builder(activity!!, R.style.mytheme)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.content_not_available, null)
        dialogBuilder.setView(dialogView)

        val tv_message = dialogView.findViewById(R.id.tv_message) as TextView
        val tv_return = dialogView.findViewById(R.id.tv_return1) as Button

        tv_message.text = msg

        val alertDialog = dialogBuilder.create()

        /*val map = takeScreenShot(this);

        val fast = fastblur(map, 10);
        val draw = BitmapDrawable(getResources(), fast);*/
        tv_return.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            alertDialog.dismiss()
            val i = Intent(activity, DashBoardActivity::class.java)
            i.putExtra("fragment", "tests")
            startActivity(i)
            (activity as Activity).overridePendingTransition(0, 0)


        }
        //alertDialog.getWindow().setBackgroundDrawable(draw);
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        alertDialog.show()
    }

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        when (resultCode) {
            SHOW_RESULT -> if (resultData != null) {
                //showData(resultData.getString("data"))
                Log.e("test fragment","onReceiveResult....."+resultData.getString("data"))
                if (resultData.getString("data") == "success") {
                    readFileLocally("","","",0)
                    gotoStartScreen("","")
                }else if(resultData.getString("data") == "failure"){

                }
            }
        }
    }


    override fun onClick(topicName: String,count:Int) {
        var filename = ""
        var firestoreversionkey=""
        var originalfilename = topicName
        if (topicName.equals("CALCULUS 1")) {
            filename = "jee-calculus-1"
            firestoreversionkey = "Calculus1Version"
        } else if (topicName.equals("CALCULUS 2")) {
            filename = "jee-calculus-2"
            firestoreversionkey = "Calculus2Version"
        } else if (topicName.equals("ALGEBRA")) {
            filename = "ii-algebra"
            firestoreversionkey = "AlgebraVersion"
        } else if (topicName.equals("OTHER")) {
            filename = "other"
            firestoreversionkey = "BasicVersion"
        } else if (topicName.equals("GEOMETRY")) {
            filename = "iii-geometry"
            firestoreversionkey = "GeometryVersion"
        }
        Log.e("test fragment","on click filename...."+filename)
        var topicname = topicName.replace("\\s".toRegex(), "")
        /*var version:String = ""
        var testcontentlist1: List<TestDownload>? = databaseHandler!!.gettestContent()

        for(i in 0 until testcontentlist1!!.size) {
            if (testcontentlist1.get(i).testtype.equals(topicname.toLowerCase())) {
                version = testcontentlist1.get(i).testversion
                break
            }
        }
        try{
            val docRef = db.collection("testcontentdownload").document("nJUIWEtshPEmAXjqn7y4")
            docRef.get().addOnSuccessListener {
                if (it != null) {
                    val firestoreversion = it.getData()!!.get(firestoreversionkey)!!.toString()
                    Log.e("test fragment","version....."+version)
                    Log.e("test fragment","firestoreversion....."+firestoreversion)
                    if(version.equals(firestoreversion)){

                    }else{
                        databaseHandler!!.updatetestcontentdownloadstatus(0,topicname.toLowerCase())
                        if(isNetworkConnected()) {
                            downloadServiceFromBackground(activity!!,db)
                        }
                    }

                }
            }


        }catch (e:Exception){

        }*/




        /*var topicname = ""
        if(topicName.equals("OTHER")){
            topicname ="basic"
        }else{*/

       // }

        var downloadstatus:Int = -1
        var testcontentlist: List<TestDownload>? = databaseHandler!!.gettestContent()
        for(i in 0 until testcontentlist!!.size) {
            if (testcontentlist.get(i).testtype.equals(topicname.toLowerCase())) {
                downloadstatus = testcontentlist.get(i).testdownloadstatus
                break
            }
        }
        Log.e("test fragment","on click topicname...."+topicname)
        Log.e("test fragment","on click download status...."+downloadstatus)


        sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
        if(!sound){
            // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
            //  mediaPlayer.start()
            if (Utils.loaded) {
                Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                Log.e("Test", "Played sound...volume..."+ Utils.volume);
                //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
            }
        }
        if(downloadstatus == 1){
           // Toast.makeText(context,"download status 1...read from local...",Toast.LENGTH_SHORT).show()
            val dirFile = File(activity!!.getCacheDir(),topicname.toLowerCase()+"/"+filename)
            val size = File(activity!!.getCacheDir(),topicname.toLowerCase()+"/"+filename)
                .walkTopDown()
                .map { it.length() }
                .sum() // in bytes
            Log.e("test fragment","folder size......."+size);
            Log.e("test fragment","dirFile path......."+dirFile.absolutePath);
            if(dirFile.isDirectory()){
                var files = dirFile.list();
                Log.e("test fragment","files size......."+files.size);
                    if (files.size == 0) {
                        //directory is empty
                        Log.e("test fragment","files.size......empty.");
                        val connectivityManager = activity!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
                        val isConnected: Boolean = activeNetwork?.isConnected == true

                        databaseHandler!!.updatetestcontentdownloadstatus(0,topicname.toLowerCase())
                        if(isNetworkConnected()) {
                            downloadServiceFromBackground(activity!!,db)
                        }
                        readFileFromAssetsNew(topicname.toLowerCase(),filename,originalfilename)

                    }else{
                        Log.e("test fragment","files.size.....not...empty.");
                        readFileLocally(topicname.toLowerCase(),filename,originalfilename,count)
                        //readFileFromAssetsNew(topicname.toLowerCase(),filename,originalfilename)



                    }
            }




        }else{
          //  Toast.makeText(context,"download status 0...read from assets...",Toast.LENGTH_SHORT).show()
           // databaseHandler!!.updatetestcontentdownloadstatus(0,topicname.toLowerCase())
            if(isNetworkConnected()) {
                downloadServiceFromBackground(activity!!,db)
            }
            //readFileLocally(topicname.toLowerCase(),filename,originalfilename,count)
             readFileFromAssetsNew(topicname.toLowerCase(),filename,originalfilename)
            //gotoStartScreenThroughAssets(topicname,originalfilename)
        }

        //gotoReviewScreen(topicname)
    }
    private fun downloadServiceFromBackground(
        mainActivity: Activity,db: FirebaseFirestore
    ) {
        ContentDownloadService.enqueueWork(mainActivity, db)
    }

    fun isNetworkConnected(): Boolean {
        val connectivityManager = activity!!.getSystemService(Context.
            CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        val isConnected: Boolean = activeNetwork?.isConnected == true
        return isConnected
    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id){
            R.id.dateRL -> {
                showCalendarForProcessingDate()
            }
            R.id.infoRl -> {
                Log.e("tests fragment","on click.....info rl.....");
                sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
                if(!sound){
                    // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                    //  mediaPlayer.start()
                    if (Utils.loaded) {
                        Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                        Log.e("Test", "Played sound...volume..."+ Utils.volume);
                        //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                    }
                }
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                    param(FirebaseAnalytics.Param.SCREEN_NAME, "InfoPopup")
                    param(FirebaseAnalytics.Param.SCREEN_CLASS, "TestFragment")
                }
                showDialog()

            }
        }
    }

    private fun showDialog() {
        dialog = Dialog(activity!!)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCancelable(true)
        dialog!!.setTitle("Info")
        dialog!!.setContentView(R.layout.layout_info_dialog)
        // val webview = dialog!!.findViewById(R.id.webview_hint) as WebView
        val close = dialog!!.findViewById(R.id.close) as Button





        //buttonEffect(btn_gotIt,false)
        // alertDialog = dialogBuilder.create()
        close.setOnClickListener {
            sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
            if(!sound){
                // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
                //  mediaPlayer.start()
                if (Utils.loaded) {
                    Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                    Log.e("Test", "Played sound...volume..."+ Utils.volume);
                    //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
                }
            }
            dialog!!.dismiss()
        }
        dialog!!.show()
        var layoutParams =  WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog!!.getWindow()!!.getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog!!.getWindow()!!.setAttributes(layoutParams);


    }
    private fun showCalendarForProcessingDate() {
        // Get Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)
        //processingdateETID.setText(mDay+"/"+(mMonth+1)+"/"+mYear);
        val datePickerDialog = DatePickerDialog(activity!!,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                //cardViewCVID.setVisibility(View.GONE)
                //processingdateETID.setText(dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                try {
                    val birthDate =
                        sdf.parse(year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth)
                    tv_date.setText(sdf.format(birthDate))
                    Log.e("home fragment...","....dayOfMonth...."+dayOfMonth)
                    Log.e("home fragment...","....(monthOfYear + 1)...."+(monthOfYear + 1))
                    Log.e("home fragment...","....year...."+year)
                    Log.e("home fragment...","....birthDate...."+birthDate)
                    val sdf1 = SimpleDateFormat("yyyy-MM-dd")
                    Utils.date = birthDate

                    val i = Intent(activity!!, DashBoardActivity::class.java)
                    //i.putExtra("fragment", "pdf")
                    startActivity(i)
                    //calculateAge(birthDate,"member2",member2age);
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                //getAge(year,monthOfYear+1,dayOfMonth,"member2",member2age);
            }, mYear, mMonth, mDay
        )
        c.add(Calendar.YEAR, -10)
        datePickerDialog.datePicker.minDate = c.timeInMillis
        //datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()

    }
    private fun downloadDataFromBackground(
        mainActivity: Activity,
        url: String, version:String
    ) {
        JobService.enqueueWork(mainActivity, url,version,"")
    }

    fun downloaddialog(msg:String){
        val dialogBuilder = AlertDialog.Builder(activity!!, R.style.mytheme)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.app_download_test_update, null)
        dialogBuilder.setView(dialogView)


        val btn_upgrade = dialogView.findViewById(R.id.btn_upgrade) as Button
        val tv_message1 = dialogView.findViewById(R.id.tv_message1) as TextView
        tv_message1.text = msg
        alertDialog = dialogBuilder.create()
        alertDialog!!.setCancelable(false)
        btn_upgrade.setOnClickListener {
            alertDialog!!.dismiss()




            //navigateToSummaryScreenNew()
            // var status:Int = databaseHandler!!.updatequizplayFinalstatus(testQuiz.title,"1",currentDate,testQuiz.lastplayed);
            // var answers:Int = databaseHandler!!.updatequizplayFinalTimeTaken(testQuiz.title,timetaken.toString(),currentDate,testQuiz.lastplayed);
            // navigateToSummaryScreenNew()
        }

        //alertDialog.getWindow().setBackgroundDrawable(draw);
        alertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        alertDialog!!.show()
    }

    /*override fun onClick(topic: TestQuizFinal) {
        sound = sharedPrefs?.getBooleanPrefVal(activity!!, ConstantPath.SOUNDS) ?: true
        if(!sound){
            // mediaPlayer = MediaPlayer.create(this,R.raw.amount_low)
            //  mediaPlayer.start()
            if (Utils.loaded) {
                Utils.soundPool.play(Utils.soundID, Utils.volume, Utils.volume, 1, 0, 1f);
                Log.e("Test", "Played sound...volume..."+ Utils.volume);
                //Toast.makeText(context,"end",Toast.LENGTH_SHORT).show()
            }
        }
        gotoReviewScreen(topic)
    }*/

    fun gotoReviewScreen(topic: TestQuizFinal){
        val intent = Intent(activity, TestReviewActivity::class.java)
        intent.putExtra("title", topic.title)
        intent.putExtra("playeddate", topic.pdate)
        intent.putExtra("lastplayed", topic.typeofPlay)
        intent.putExtra(ConstantPath.QUIZ_COUNT, topic.totalQuestions)

        startActivity(intent)
    }

    fun gotoStartScreenThroughAssets(topictype:String,originalname:String){

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

         //   databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

          //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
        }else{

            if(branchesItemList!!.size == (testQuiz.serialNo).toInt()){
                topic = branchesItemList!![0].topic
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                //jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                lastplayed = "basic"
             //   databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

             //   databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
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
                //    databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                 //   databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
                }else{
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())].topic
                    folderPath = localPath+topic.folderName
                    Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                    jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                   // jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                    lastplayed = "basic"
                 //   databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                  //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());

                }
            }



        }




        /*val bundle = Bundle()
        bundle.putString("Category", "Test")
        bundle.putString("Action", "Test")
        bundle.putString("Label", topic.title)
        firebaseAnalytics?.logEvent("Test", bundle)*/

        /*val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topic.title)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
        // bundle.putString("Label", "TestGo")
        firebaseAnalytics?.logEvent("Test", bundle)*/


        Log.e("chapter fragment.....","jsonStringBasic......."+jsonStringBasic);

        val intent = Intent(context!!, StartTestActivity::class.java)
        //intent.putExtra(ConstantPath.TOPIC, topic)
        intent.putExtra(ConstantPath.TOPIC_NAME, topictype)
        intent.putExtra("topicnameoriginal", originalname)
        intent.putExtra(ConstantPath.FOLDER_NAME, topic.folderName)
        intent.putExtra(ConstantPath.DYNAMIC_PATH, jsonStringBasic)
        intent.putExtra(ConstantPath.COURSE_ID, courseId)
        intent.putExtra(ConstantPath.COURSE_NAME, courseName)
        intent.putExtra(ConstantPath.TOPIC_ID, "")
        //intent.putExtra(ConstantPath.TOPIC_POSITION, topic.displayNo)
        intent.putExtra(ConstantPath.FOLDER_PATH, localPath)
        intent.putExtra(ConstantPath.TITLE_TOPIC, gradeTitle!!)
        //intent.putExtra("LAST_PLAYED", lastplayed)
        intent.putExtra("comingfrom", "Test")
        intent.putExtra("readdata", "assets")
        intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
        intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
        intent.putExtra(ConstantPath.CARD_NO, "")
        startActivity(intent)
    }

    fun gotoStartScreen(topictype:String,originalname:String){

        //databaseHandler!!.deleteQuizPlayRecord(topic.title)
        var lastplayed:String =""
        var topic:Topic
        var folderPath:String = ""
        var testQuiz:TestQuiz
        testQuiz = databaseHandler!!.getQuizTopicsForTimerLastPlayed(topictype.toLowerCase())
        Log.e("test fragment","testQuiz.lastplayed......"+testQuiz.lastplayed)
        Log.e("test fragment","testQuiz.serialno......"+testQuiz.serialNo)
        Log.e("test fragment","branchesItemList.size......"+branchesItemList!!.size)
        if(testQuiz.lastplayed == null){
            topic = branchesItemList!![0].topic
            folderPath = localPath+topic.folderName
            Log.e("test fragment","testQuiz.folderPath......"+folderPath)
            jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
            lastplayed = "basic"

            //databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

           // databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
        }else{

            if(branchesItemList!!.size == (testQuiz.serialNo).toInt()){
                topic = branchesItemList!![0].topic
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                lastplayed = "basic"
             //   databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

              //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
            }else{
                if(((testQuiz.serialNo).toInt())-1 < branchesItemList!!.size){
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())-1].topic
                }else{
                    topic = branchesItemList!![0].topic
                }

                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                if(testQuiz.lastplayed.equals("basic")){
                    jsonStringBasic =  Utils.readFromFile("$folderPath/intermediate.json")
                    lastplayed = "intermediate"
                 //   databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                  //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
                }else{
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())].topic
                    folderPath = localPath+topic.folderName
                    Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                    jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                    lastplayed = "basic"
                  //  databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                  //  databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());

                }
            }



        }


        /*val bundle = Bundle()
        bundle.putString("Category", "Test")
        bundle.putString("Action", "Test")
        bundle.putString("Label", topic.title)
        firebaseAnalytics?.logEvent("Test", bundle)*/

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, topic.title)
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Test")
        // bundle.putString("Label", "TestGo")
        firebaseAnalytics?.logEvent("Test", bundle)


        Log.e("chapter fragment.....","jsonStringBasic......."+jsonStringBasic);

        val intent = Intent(context!!, StartTestActivity::class.java)
        intent.putExtra(ConstantPath.TOPIC, topic)
        intent.putExtra(ConstantPath.TOPIC_NAME, topictype)
        intent.putExtra("topicnameoriginal", originalname)
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
        intent.putExtra("readdata", "files")
        intent.putExtra(ConstantPath.TOPIC_LEVEL, "")
        intent.putExtra(ConstantPath.LEVEL_COMPLETED, "")
        intent.putExtra(ConstantPath.CARD_NO, "")
        startActivity(intent)
    }

    fun loadJSONFromAsset(path: String): String? {
        val json: String?
        try {
            val `is` = context!!.assets.open(path)
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }
}