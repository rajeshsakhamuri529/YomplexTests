package com.blobcity.repository

import android.app.Application

import android.os.AsyncTask
import androidx.lifecycle.LiveData

import com.yomplex.tests.dao.TopicStatusDao
import com.yomplex.tests.database.QuizDatabase
import com.yomplex.tests.entity.TopicStatusEntity

class TopicStatusRepository(application: Application) {
    private var topicStatusDao: TopicStatusDao?= null
    init {
        val db : QuizDatabase = QuizDatabase.getDatabase(application)
        topicStatusDao = db.topicStatusDao

    }

    fun getAllTopicStatus(gradeTitle: String) : LiveData<List<TopicStatusEntity>> {
        return topicStatusDao!!.getAllTopicStatus(gradeTitle)
    }

    fun getSingleTopicStatus(topicID: String, gradeTitle: String) : LiveData<List<TopicStatusEntity>>{
        return topicStatusDao!!.getSingleTopicStatus(topicID, gradeTitle)
    }

    fun getTopicsByLevel(topicLevel: String, gradeTitle: String) : LiveData<List<TopicStatusEntity>>{
        return topicStatusDao!!.getTopicByLevel(topicLevel, gradeTitle)
    }

    fun insert(topicStatusEntity: TopicStatusEntity){
        insertAsyncTask(topicStatusDao!!).execute(topicStatusEntity)
    }

    private class insertAsyncTask(topicStatusDao: TopicStatusDao)
        : AsyncTask<TopicStatusEntity, Void, Void>(){
        var topicStatusDao: TopicStatusDao ?= null
        init {
            this.topicStatusDao = topicStatusDao
        }
        override fun doInBackground(vararg params: TopicStatusEntity?): Void? {
            topicStatusDao!!.insert(params[0]!!)
            return null
        }

    }
}