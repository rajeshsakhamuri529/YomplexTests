package com.yomplex.tests.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yomplex.tests.R
import com.yomplex.tests.interfaces.TestClickListener
import com.yomplex.tests.model.Course
import com.yomplex.tests.model.QuizScore
import kotlinx.android.synthetic.main.tests_list_item.view.*
import java.util.ArrayList

class CourseAdapter(val context: Context,
                    val testtypeslist: List<Course>,
                    val quiztopicreviewListener: TestClickListener
) :
    RecyclerView.Adapter<CourseAdapter.TestsViewHolder>()   {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CourseAdapter.TestsViewHolder {
        return CourseAdapter.TestsViewHolder(
            LayoutInflater.from(context)
                .inflate(R.layout.tiny_test_list_item, p0, false)
        )
    }

    override fun getItemCount(): Int {

        return testtypeslist.size
    }

    override fun onBindViewHolder(holder: CourseAdapter.TestsViewHolder, position: Int) {
        holder.tv_topic_name.text = testtypeslist[position].coursename
        if(testtypeslist[position].coursename.equals("ALGEBRA")){
            holder.tv_score.text = "A"
            holder.tv_topic_name.text = "Algebra"
        }else if(testtypeslist[position].coursename.equals("GEOMETRY")){
            holder.tv_score.text = "G"
            holder.tv_topic_name.text = "Geometry"
        }else if(testtypeslist[position].coursename.equals("CALCULUS 1")){
            holder.tv_score.text = "C1"
            holder.tv_topic_name.text = "Calculus 1"
        }else if(testtypeslist[position].coursename.equals("CALCULUS 2")){
            holder.tv_score.text = "C2"
            holder.tv_topic_name.text = "Calculus 2"
        }else if(testtypeslist[position].coursename.equals("OTHER")){
            holder.tv_score.text = "OT"
            holder.tv_topic_name.text = "Other\nTopics"
        }

        holder.rl_single_topics.setOnClickListener(View.OnClickListener {
            quiztopicreviewListener.onClick(testtypeslist[position].coursename,testtypeslist[position].courseexist)
        })

    }

    class TestsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_topic_name = itemView.tv_topic_name
        val rl_single_topics = itemView.rl_single_topics
        //val totalRL = itemView.totalRL;
        val tv_score = itemView.tv_score;
        /* val iv_progress_icon = itemView.iv_progress_icon
         val tv_challenge_date = itemView.tv_challenge_date
         val tv_challenge_time = itemView.tv_challenge_time
         val tv_challenge_time_mins = itemView.tv_challenge_time_mins

         val tv_challenge_score = itemView.tv_challenge_score
         val rootLayout = itemView.rootLayout;
         val totalLL = itemView.totalLL;
         val tv_correct = itemView.tv_correct;*/


    }


}