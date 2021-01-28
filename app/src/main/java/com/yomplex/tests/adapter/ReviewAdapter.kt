package com.yomplex.tests.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yomplex.tests.R
import com.yomplex.tests.interfaces.TestQuizReviewClickListener
import com.yomplex.tests.model.TestQuizFinal
import kotlinx.android.synthetic.main.review_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class ReviewAdapter(val context: Context,
                    val branchesItemList: List<TestQuizFinal>,
                    val quiztopicreviewListener: TestQuizReviewClickListener
) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>()  {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ReviewAdapter.ReviewViewHolder {
        return ReviewAdapter.ReviewViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.review_list_item, p0, false)
        )
    }

    override fun getItemCount(): Int {
        return branchesItemList.size
    }

    override fun onBindViewHolder(holder: ReviewAdapter.ReviewViewHolder, position: Int) {
        var count:Int = 0
        var questionanswers:String = branchesItemList[position].questionAnswers
        var queans:List<String> = questionanswers.split(",")
        for(i in 0 until branchesItemList[position].totalQuestions!!){
            var ans1:List<String> = queans.get(i).split("~")
            if(ans1[1].equals("opt1")){
                count++
            }
        }
        if(count == branchesItemList[position].totalQuestions){
            Glide.with(context)
                .load(R.drawable.ic_tick_green)
                .into(holder.iv_progress_icon)
        }else{
            Glide.with(context)
                .load(R.drawable.red_circle)
                .into(holder.iv_progress_icon)
        }

        if(count == 0){
            holder.tv_correct.text = ""+count
            holder.totalLL.background = context.resources.getDrawable(R.drawable.test_summary_0)
            //summaryTxt.text = "seriously?"
            holder.tv_correct.setTextColor(context.resources.getColor(R.color.seriously))
           // summaryTxt.setTextColor(resources.getColor(R.color.seriously))
        }else if(count == 1){
            holder.tv_correct.text = ""+count
            holder.totalLL.background = context.resources.getDrawable(R.drawable.test_summary_0)
            //summaryTxt.text = "not good"
            holder.tv_correct.setTextColor(context.resources.getColor(R.color.not_good))
            //summaryTxt.setTextColor(resources.getColor(R.color.not_good))
        }else if(count == 2){
            holder.tv_correct.text = ""+count
            holder.totalLL.background = context.resources.getDrawable(R.drawable.test_summary_2)
            //summaryTxt.text = "not bad"
            holder.tv_correct.setTextColor(context.resources.getColor(R.color.not_bad))
            //summaryTxt.setTextColor(resources.getColor(R.color.not_bad))
        }else if(count == 3){
            holder.tv_correct.text = ""+count
            holder.totalLL.background = context.resources.getDrawable(R.drawable.test_summary_3)
            //summaryTxt.text = "good"
            holder.tv_correct.setTextColor(context.resources.getColor(R.color.good))
            //summaryTxt.setTextColor(resources.getColor(R.color.good))

        }else if(count == 4){
            holder.tv_correct.text = ""+count
            holder.tv_correct.setTextColor(context.resources.getColor(R.color.white))
            holder.totalLL.background = context.resources.getDrawable(R.drawable.test_summary_4)
            //summaryTxt.text = "superrr!"
            //summaryTxt.setTextColor(resources.getColor(R.color.perfect))

        }


        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        Log.e("test adapter","branchesItemList[position].pdate....."+branchesItemList[position].pdate)
        var oldDate = dateFormat.parse(branchesItemList[position].pdate)

        var current = Date().time
        //Log.e("test adapter","old date....."+oldDate.time)
        //Log.e("test adapter","current....."+current)
        //val days = Days.daysBetween(date1, date2).getDays()
        val diff = current - (oldDate.time)
       // Log.e("tests adapter","diff date...."+diff);
       // Log.e("tests adapter","diff date...."+diff / (1000 * 60 * 60 * 24));
       // Log.e("tests adapter","diff diff.toInt()...."+diff.toInt());
        val diff1 = diff / (1000 * 60 * 60 * 24)
        Log.e("tests adapter","diff date...."+diff1);
        if(diff1 == 0L){
            holder.tv_challenge_date.text = "Today"
        }else if(diff1 == 1L){
            holder.tv_challenge_date.text = "Yesterday"
        }else{
            holder.tv_challenge_date.text = ""+diff1 + "  days ago"
        }

        if(branchesItemList[position].timetaken.equals("0")){
            holder.tv_challenge_time.text =  "1"
            holder.tv_challenge_time_mins.text = "min"
        }else if(branchesItemList[position].timetaken.equals("1")){
            holder.tv_challenge_time.text = branchesItemList[position].timetaken
            holder.tv_challenge_time_mins.text = "min"
        }else{
            holder.tv_challenge_time.text = branchesItemList[position].timetaken
            holder.tv_challenge_time_mins.text = "mins"
        }

        holder.tv_challenge_score.text = ""+count+" / "+branchesItemList[position].totalQuestions

        holder.rootLayout.setOnClickListener(View.OnClickListener {
            quiztopicreviewListener.onClick(branchesItemList[position])
        })
    }
class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
     val iv_progress_icon = itemView.iv_progress_icon
     val tv_challenge_date = itemView.tv_challenge_date
     val tv_challenge_time = itemView.tv_challenge_time
     val tv_challenge_time_mins = itemView.tv_challenge_time_mins

     val tv_challenge_score = itemView.tv_challenge_score
     val rootLayout = itemView.rootLayout;
     val totalLL = itemView.totalLL;
     val tv_correct = itemView.tv_correct;

}
}