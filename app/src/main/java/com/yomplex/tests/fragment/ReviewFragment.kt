package com.yomplex.tests.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.yomplex.tests.R
import com.yomplex.tests.activity.ContentVersionUpdateService
import com.yomplex.tests.activity.DashBoardActivity
import com.yomplex.tests.activity.TestReviewActivity
import com.yomplex.tests.adapter.ReviewAdapter
import com.yomplex.tests.database.QuizGameDataBase
import com.yomplex.tests.interfaces.TestQuizReviewClickListener
import com.yomplex.tests.model.TestQuizFinal
import com.yomplex.tests.utils.ConstantPath
import com.yomplex.tests.utils.SharedPrefs
import com.yomplex.tests.utils.Utils
import com.yomplex.tests.utils.VerticalSpaceItemDecoration
import kotlinx.android.synthetic.main.review_ll.*
import kotlinx.android.synthetic.main.review_ll.view.*
import java.io.File


class ReviewFragment: Fragment(), View.OnClickListener, TestQuizReviewClickListener {

    var mLastClickTime:Long = 0;
    var adapter: ReviewAdapter?= null
    var databaseHandler: QuizGameDataBase?= null
    var testquizlist: List<TestQuizFinal>? = null
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.review_ll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tests.elevation = 15F
        databaseHandler = QuizGameDataBase(context);
        sharedPrefs = SharedPrefs()
        testquizlist = databaseHandler!!.getTestQuizList()

        firebaseAnalytics = Firebase.analytics
        // firebaseAnalytics.setCurrentScreen(activity!!, "Test", null /* class override */)

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "ReviewsTab")
            param(FirebaseAnalytics.Param.SCREEN_CLASS, "ReviewFragment")
        }

        if(testquizlist!!.size == 0){
            view.rl_no_review.visibility = View.VISIBLE
            view.rcv_review.visibility = View.GONE
        }else{
            view.rcv_review.visibility = View.VISIBLE
            view.rl_no_review.visibility = View.GONE


            adapter = ReviewAdapter(context!!, testquizlist!!,this)


            view.rcv_review.addItemDecoration(VerticalSpaceItemDecoration(30));
            //rcv_chapter.addItemDecoration(itemDecorator)
            //rcv_chapter.addItemDecoration(DividerItemDecoration(context,))
            view.rcv_review.adapter = adapter

        }

        try {
            val mSensorService = ContentVersionUpdateService()
            val mServiceIntent = Intent(activity, mSensorService::class.java)
            if (!Utils.isMyServiceRunning(activity, mSensorService::class.java)) {
                activity!!.startService(mServiceIntent)
            }
        } catch (e: Exception) {

        }

        view.tv_no_review.setOnClickListener(this)
        view.tv_no_review2.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime()
        when (v!!.id) {
            R.id.tv_no_review -> {
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

                val i = Intent(activity, DashBoardActivity::class.java)
                i.putExtra("fragment", "tests")
                startActivity(i)
                (activity as Activity).overridePendingTransition(0, 0)

            }
            R.id.tv_no_review2 -> {
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

                val i = Intent(activity, DashBoardActivity::class.java)
                i.putExtra("fragment", "tests")
                startActivity(i)
                (activity as Activity).overridePendingTransition(0, 0)

            }
        }
    }

    override fun onClick(topic: TestQuizFinal) {
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

        try{
            val paths: String = topic.questionPathType
            var ans:List<String> = paths.split(",")
            var ans1:List<String> = ans.get(0).split("~")

            var quepath = File(ans1[0])
            if(quepath.exists()){
                Log.e("review fragment","quepath...if....."+quepath)
                gotoReviewScreen(topic)
            }else{
                Log.e("review fragment","quepath....else...."+quepath)

                databaseHandler!!.updatequizplayReviewstatus(topic.title,1,topic.pdate,topic.typeofPlay,topic.testtype)
                noReviewDialog()
               // testquizlist = databaseHandler!!.getTestQuizList()
               // adapter!!.notifyDataSetChanged()


            }
        }catch (e:Exception){

        }




    }

    fun gotoReviewScreen(topic: TestQuizFinal){
        val intent = Intent(activity, TestReviewActivity::class.java)
        intent.putExtra(ConstantPath.TOPIC_NAME, topic.testtype)
        intent.putExtra("title", topic.title)
        intent.putExtra("playeddate", topic.pdate)
        intent.putExtra("lastplayed", topic.typeofPlay)
        intent.putExtra("readdata", topic.readdata)
        intent.putExtra(ConstantPath.QUIZ_COUNT, topic.totalQuestions)

        startActivity(intent)
    }

    private fun noReviewDialog() {
        val dialogBuilder = AlertDialog.Builder(activity!!, R.style.mytheme)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.no_review_layout, null)
        dialogBuilder.setView(dialogView)

        //val tv_quit = dialogView.findViewById(R.id.tv_quit1) as Button
        val tv_return = dialogView.findViewById(R.id.tv_return1) as Button



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
            i.putExtra("fragment", "review")
            startActivity(i)
            (activity as Activity).overridePendingTransition(0, 0)


        }
        //alertDialog.getWindow().setBackgroundDrawable(draw);
        alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        alertDialog.show()
    }
}

