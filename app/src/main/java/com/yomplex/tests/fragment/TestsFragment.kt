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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.yomplex.tests.R
import com.yomplex.tests.Service.ContentDownloadService
import com.yomplex.tests.Service.JobService
import com.yomplex.tests.Service.ProgressJobService
import com.yomplex.tests.Service.ProgressJobService.SHOW_RESULT
import com.yomplex.tests.Service.ServiceResultReceiver
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
    var testtypeslist: ArrayList<String>? = null
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
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
            .build()
        db.firestoreSettings = settings

        mServiceResultReceiver = ServiceResultReceiver(Handler())
        mServiceResultReceiver?.setReceiver(this)
        databaseHandler = QuizGameDataBase(context);

        firebaseAnalytics = FirebaseAnalytics.getInstance(activity!!)

        firebaseAnalytics.setCurrentScreen(activity!!, "Test", null /* class override */)
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
        testtypeslist = ArrayList<String>()


        testtypeslist!!.add("ALGEBRA")
        testtypeslist!!.add("GEOMETRY")
        testtypeslist!!.add("CALCULUS 1")
        testtypeslist!!.add("CALCULUS 2")
        testtypeslist!!.add("OTHER")
        /*if(testquizlist!!.size == 0){
            view.tv_no_review.visibility = View.VISIBLE
            view.rcv_tests.visibility = View.GONE
        }else{*/
            view.rcv_tests.visibility = View.VISIBLE
            view.tv_no_review.visibility = View.GONE




            adapter = TestsAdapter(context!!, testtypeslist!!,testscorelist!!,this)


            view.rcv_tests.addItemDecoration(VerticalSpaceItemDecoration(30));
            //rcv_chapter.addItemDecoration(itemDecorator)
            //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
            view.rcv_tests.adapter = adapter
      //  }
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

        /*val xAxis = view.barchart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        xAxis.setDrawGridLines(false)

        view.barchart.getAxisLeft().setDrawGridLines(false)*/

        val xAxisFormatter: IAxisValueFormatter = MyAxisValueFormatter1()

        val xAxis = view.barchart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM)
        //xAxis.labelRotationAngle = 90f
        xAxis.setTypeface(tfLight)
        xAxis.setDrawGridLines(false)
        xAxis.setGranularity(1f) // only intervals of 1 day
        xAxis.setLabelCount(7)
        xAxis.setValueFormatter(xAxisFormatter)

        val custom = MyAxisValueFormatter()

        val leftAxis = view.barchart.getAxisLeft()
        leftAxis.setTypeface(tfLight)
        leftAxis.setDrawGridLinesBehindData(true)
        leftAxis.setLabelCount(5, false)
        leftAxis.setValueFormatter(custom)
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.setSpaceTop(15f)
        leftAxis.setAxisMinimum(0f) // this replaces setStartAtZero(true)
        leftAxis.axisMaximum = 20f

        val rightAxis = view.barchart.getAxisRight()
        rightAxis.setDrawGridLines(true)
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
             if(total <= 15){
                 //gradientFills
                 gradientFills.add(Fill(getResources().getColor(R.color.button_close_text)))
             }else{
                 gradientFills.add(Fill(getResources().getColor(R.color.chart_color)))
             }
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

        view.barchart.getAxisLeft().setDrawGridLines(false)
        val dataSets = java.util.ArrayList<IBarDataSet>()
        dataSets.add(bardataset)

        val data = BarData(dataSets)
        data.setDrawValues(false)
        data.setValueTextSize(10f)
        data.setValueTypeface(tfLight)
        data.barWidth = 0.5f


       // var  data = BarData(bardataset);
        view.barchart.setData(data); // set the data and list of labels into chart
       // view.barchart.setDescription("Set Bar Chart Description Here");  // set the description
        //bardataset.setColors(COLORFUL_COLORS.toMutableList());
        view.barchart.animateY(0);




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
    private fun readFileFromAssets(topicname: String){
        //Toast.makeText(activity,"content read from assets",Toast.LENGTH_SHORT).show()
        val courseJsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( topicname+"/test/" + "Courses.json")
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
        localPath = "$topicname/test/$courseName/"
        Log.e("test fragment","readFileFromAssets.....localPath...."+localPath);
        // val jsonString = readFromFile(localPath +"topic.json")
        val jsonString = (activity!! as DashBoardActivity).loadJSONFromAsset( localPath + "topic.json")
        Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel= gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches
        sharedPrefs?.setIntPrefVal(context!!,TOPIC_SIZE, branchesItemList!!.size)
    }


    private fun readFileLocally(topicname:String,filename:String) {
        //Toast.makeText(activity,"content read from local storage",Toast.LENGTH_SHORT).show()
        val dirFile = File(activity!!.getCacheDir(),topicname+filename)
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
        Log.d("jsonString",jsonString);
        val topicType = object : TypeToken<TopicResponseModel>() {}.type
        val topicResponseModel: TopicResponseModel = gsonFile.fromJson(jsonString, topicType )

        branchesItemList = topicResponseModel.branches
        sharedPrefs?.setIntPrefVal(context!!, ConstantPath.TOPIC_SIZE, branchesItemList!!.size)
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

    override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
        when (resultCode) {
            SHOW_RESULT -> if (resultData != null) {
                //showData(resultData.getString("data"))
                Log.e("test fragment","onReceiveResult....."+resultData.getString("data"))
                if (resultData.getString("data") == "success") {
                    readFileLocally("","")
                    gotoStartScreen("","")
                }else if(resultData.getString("data") == "failure"){

                }
            }
        }
    }
    /*private fun showDataFromBackground(
        mainActivity: Activity,
        url:String,
        version:String,
        mResultReceiver: ServiceResultReceiver
    ) {
        ProgressJobService.enqueueWork(mainActivity, url,version, mResultReceiver)
    }*/

    /*private fun downdata(url:String){
        val dirpath = File((activity!!.getExternalFilesDir(null))!!.absolutePath)

        val downloadId = PRDownloader.download(url, dirpath.absolutePath, "/testcontent.rar")
            .build()
            .setOnStartOrResumeListener {
                Log.e("downdata", "onStartOrResume.....")
                isdownload = true

            }
            .setOnPauseListener { Log.e("downdata", "onPause.....") }
            .setOnCancelListener { Log.e("downdata", "onCancel.....") }
            .setOnProgressListener { progress ->
                Log.e(
                    "downdata",
                    "onProgress.....$progress")
                isdownload = true
            }
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Log.e("downdata", "onDownloadComplete.....")
                    try {
                        val dirFile = File(activity!!.getExternalFilesDir(null), "test")
                        FileUtils.deleteDirectory(dirFile)
                    } catch (e: Exception) {

                    }

                    val iszip = Utils.unpackZip(dirpath.absolutePath, "/testcontent.rar")
                    if (iszip) {
                        val dirFile = File(activity!!.getExternalFilesDir(null), "testcontent.rar")
                        dirFile.delete()

                        databaseHandler!!.updatetestcontentversion(version,"")
                        databaseHandler!!.updatetestcontentdownloadstatus(1,"")

                        if(alertDialog != null){
                           alertDialog!!.dismiss()
                        }
                      //  test_btn.isEnabled = true
                        isdownload = false
                        readFileLocally()
                        gotoStartScreen("")
                    }

                }


                override fun onError(error: Error?) {

                    Log.e("downdata", "onerror.....$error")
                    // JobService.enqueueWork(context1,url,version);
                  //  test_btn.isEnabled = true
                    isdownload = false
                    if(alertDialog != null){
                        alertDialog!!.dismiss()
                    }
                    //if(activity != null){
                        Toast.makeText(activity,"Please check your network connection.",Toast.LENGTH_LONG).show()
                //}

                }




            })
    }*/
    override fun onClick(topicName: String) {
        var filename = ""
        var originalfilename = topicName
        if (topicName.equals("CALCULUS 1")) {
            filename = "/jee-calculus-1"
        } else if (topicName.equals("CALCULUS 2")) {
            filename = "/jee-calculus-2"
        } else if (topicName.equals("ALGEBRA")) {
            filename = "/ii-algebra"
        } else if (topicName.equals("OTHER")) {
            filename = "/other"
        } else if (topicName.equals("GEOMETRY")) {
            filename = "/iii-geometry"
        }
        Log.e("test fragment","on click filename...."+filename)

        /*var topicname = ""
        if(topicName.equals("OTHER")){
            topicname ="basic"
        }else{*/
           var topicname = topicName.replace("\\s".toRegex(), "")
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

            val dirFile = File(activity!!.getCacheDir(),topicname.toLowerCase()+filename)
            val size = File(activity!!.getCacheDir(),topicname.toLowerCase()+filename)
                .walkTopDown()
                .map { it.length() }
                .sum() // in bytes
            Log.e("test fragment","folder size......."+size);
            Log.e("test fragment","dirFile path......."+dirFile.absolutePath);
            if(dirFile.isDirectory){
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
                        readFileFromAssets(topicname.toLowerCase())
                        gotoStartScreenThroughAssets(topicname,originalfilename)
                    }else{
                        Log.e("test fragment","files.size.....not...empty.");
                        readFileLocally(topicname.toLowerCase(),filename)
                        gotoStartScreen(topicname,originalfilename)


                    }
            }



        }else{
            databaseHandler!!.updatetestcontentdownloadstatus(0,topicname.toLowerCase())
            if(isNetworkConnected()) {
                downloadServiceFromBackground(activity!!,db)
            }
            readFileFromAssets(topicname.toLowerCase())
            gotoStartScreenThroughAssets(topicname,originalfilename)
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

            databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

            databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
        }else{

            if(branchesItemList!!.size == (testQuiz.serialNo).toInt()){
                topic = branchesItemList!![0].topic
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                //jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                lastplayed = "basic"
                databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
            }else{
                topic = branchesItemList!![((testQuiz.serialNo).toInt())-1].topic
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                if(testQuiz.lastplayed.equals("basic")){
                    jsonStringBasic = loadJSONFromAsset("$folderPath/intermediate.json")
                    //jsonStringBasic =  Utils.readFromFile("$folderPath/intermediate.json")
                    lastplayed = "intermediate"
                    databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                    databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
                }else{
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())].topic
                    folderPath = localPath+topic.folderName
                    Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                    jsonStringBasic = loadJSONFromAsset("$folderPath/basic.json")
                   // jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                    lastplayed = "basic"
                    databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                    databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());

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
        if(testQuiz.lastplayed == null){
            topic = branchesItemList!![0].topic
            folderPath = localPath+topic.folderName
            Log.e("test fragment","testQuiz.folderPath......"+folderPath)
            jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
            lastplayed = "basic"

            databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

            databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
        }else{

            if(branchesItemList!!.size == (testQuiz.serialNo).toInt()){
                topic = branchesItemList!![0].topic
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                lastplayed = "basic"
                databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
            }else{
                topic = branchesItemList!![((testQuiz.serialNo).toInt())-1].topic
                folderPath = localPath+topic.folderName
                Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                if(testQuiz.lastplayed.equals("basic")){
                    jsonStringBasic =  Utils.readFromFile("$folderPath/intermediate.json")
                    lastplayed = "intermediate"
                    databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                    databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());
                }else{
                    topic = branchesItemList!![((testQuiz.serialNo).toInt())].topic
                    folderPath = localPath+topic.folderName
                    Log.e("test fragment","testQuiz.folderPath......"+folderPath)
                    jsonStringBasic =  Utils.readFromFile("$folderPath/basic.json")
                    lastplayed = "basic"
                    databaseHandler!!.deleteAllQuizTopicsLatPlayed(topictype.toLowerCase())

                    databaseHandler!!.insertquiztopiclastplayed(topic.title,topic.displayNo,lastplayed,topictype.toLowerCase());

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