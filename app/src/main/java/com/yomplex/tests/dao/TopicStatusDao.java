package com.yomplex.tests.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


import com.yomplex.tests.entity.TopicStatusEntity;

import java.util.List;

@Dao
public interface TopicStatusDao {
    @Insert
    void insert(TopicStatusEntity topicStatusEntity);

    @Query("SELECT * FROM topic_status WHERE grade_title = :gradeTitle")
    LiveData<List<TopicStatusEntity>> getAllTopicStatus(String gradeTitle);

    @Query("SELECT * FROM topic_status WHERE Topic_Id = :topicId AND grade_title = :gradeTitle")
    LiveData<List<TopicStatusEntity>> getSingleTopicStatus(String topicId, String gradeTitle);

    @Query("SELECT * FROM topic_status WHERE Topic_Level = :topic_level AND grade_title = :gradeTitle")
    LiveData<List<TopicStatusEntity>> getTopicByLevel(String topic_level, String gradeTitle);
}
