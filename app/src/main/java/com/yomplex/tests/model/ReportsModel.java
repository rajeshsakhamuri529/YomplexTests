package com.yomplex.tests.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ReportsModel {

    public String useremail;
    public String reportissuetype;
    public String additionalinfo;
    public String phonenumber;
    public String coursename;
    public String questionpath;
    @ServerTimestamp
    public Date createddate;

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getReportissuetype() {
        return reportissuetype;
    }

    public void setReportissuetype(String reportissuetype) {
        this.reportissuetype = reportissuetype;
    }

    public String getAdditionalinfo() {
        return additionalinfo;
    }

    public void setAdditionalinfo(String additionalinfo) {
        this.additionalinfo = additionalinfo;
    }

    public String getCoursename() {
        return coursename;
    }

    public void setCoursename(String coursename) {
        this.coursename = coursename;
    }

    public String getQuestionpath() {
        return questionpath;
    }

    public void setQuestionpath(String questionpath) {
        this.questionpath = questionpath;
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }
}
