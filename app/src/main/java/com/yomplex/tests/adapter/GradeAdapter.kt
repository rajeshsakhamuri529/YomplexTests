package com.yomplex.tests.adapter

import android.app.Activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.yomplex.tests.R
import com.yomplex.tests.interfaces.GradeClickListener
import com.yomplex.tests.model.GradeResponseModel
import kotlinx.android.synthetic.main.grade_single_layout.view.*
import java.util.ArrayList

class GradeAdapter(var context: Activity,
                   var clickListener: GradeClickListener,
                   var gradeResponseModelList: ArrayList<GradeResponseModel>)
    : RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): GradeViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.grade_single_layout, null, false)
        return GradeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return gradeResponseModelList.size
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        loadImage( gradeResponseModelList[position].btnPath!!, holder.btnIcon)
        loadImage( gradeResponseModelList[position].iconPath!!, holder.iv_grade)
        holder.tv_grade_title.text = gradeResponseModelList[position].title
        if (position == (gradeResponseModelList.size - 1)){
            holder.rl_grade.setPadding(0, 60, 0, 200)
        }else{
            holder.rl_grade.setPadding(0, 60, 0, 0)
        }
        holder.iv_grade.setOnClickListener {
            clickListener.click(gradeResponseModelList[position].link!!,
                gradeResponseModelList[position].title!!)
        }
    }

    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val iv_grade = itemView.iv_grade
        val btnIcon = itemView.btnIcon
        val tv_grade_title = itemView.tv_grade_title
        val rl_grade = itemView.rl_grade
    }

    fun loadImage(imagePath: String, imageView: ImageView){
        Glide.with(context)
            .load(imagePath)
            .into(imageView)
    }
}