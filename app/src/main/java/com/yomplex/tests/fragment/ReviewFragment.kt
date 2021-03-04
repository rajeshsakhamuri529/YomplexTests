package com.yomplex.tests.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.yomplex.tests.R
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
import kotlinx.android.synthetic.main.review_ll.view.*


class ReviewFragment: Fragment(), View.OnClickListener, TestQuizReviewClickListener {

    var mLastClickTime:Long = 0;
    var adapter: ReviewAdapter?= null
    var databaseHandler: QuizGameDataBase?= null
    var testquizlist: List<TestQuizFinal>? = null
    var sharedPrefs: SharedPrefs? = null
    var sound: Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.review_ll, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.tests.elevation = 15F
        databaseHandler = QuizGameDataBase(context);
        sharedPrefs = SharedPrefs()
        testquizlist = databaseHandler!!.getTestQuizList()

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
        gotoReviewScreen(topic)
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
}

