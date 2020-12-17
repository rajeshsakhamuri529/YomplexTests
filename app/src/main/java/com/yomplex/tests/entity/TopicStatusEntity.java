package com.yomplex.tests.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "topic_status")
public class TopicStatusEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id = 0;

    @ColumnInfo(name = "U_Id")
    private String uid ="";

    @ColumnInfo(name = "Course_Id")
    private String courseId ="";

    @ColumnInfo(name = "Topic_Id")
    private String topicId ="";

    @ColumnInfo(name = "Topic_Level")
    private String topicLevel ="";

    @ColumnInfo(name = "is_level_completed")
    private int isLevelComplete = 0;

    @ColumnInfo(name = "topic_position")
    private int topicPosition = -1;

    @ColumnInfo(name = "grade_title")
    private String gradeTitle;

    public String getGradeTitle() {
        return gradeTitle;
    }

    public void setGradeTitle(String gradeTitle) {
        this.gradeTitle = gradeTitle;
    }

    public int getTopicPosition() {
        return topicPosition;
    }

    public void setTopicPosition(int topicPosition) {
        this.topicPosition = topicPosition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicLevel() {
        return topicLevel;
    }

    public void setTopicLevel(String topicLevel) {
        this.topicLevel = topicLevel;
    }

    public int getIsLevelComplete() {
        return isLevelComplete;
    }

    public void setIsLevelComplete(int isLevelComplete) {
        this.isLevelComplete = isLevelComplete;
    }

    @Override
    public String toString() {
        return "TopicStatusEntity{" +
                "id=" + id +
                ", uid='" + uid + '\'' +
                ", courseId='" + courseId + '\'' +
                ", topicId='" + topicId + '\'' +
                ", topicLevel='" + topicLevel + '\'' +
                ", isLevelComplete=" + isLevelComplete +
                ", topicPosition=" + topicPosition +
                ", gradeTitle='" + gradeTitle + '\'' +
                '}';
    }
}
