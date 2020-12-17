package com.yomplex.tests.entity;

public class DailyChallenge {

    public String qid ="";

    public String qtype;
    public String date;
    public String title;
    public String attempt;
    public String docid;
    public String questionversion;

    public String getDocid() {
        return docid;
    }

    public void setDocid(String docid) {
        this.docid = docid;
    }

    public String getQuestionversion() {
        return questionversion;
    }

    public void setQuestionversion(String questionversion) {
        this.questionversion = questionversion;
    }

    public String getQid() {
        return qid;
    }

    public void setQid(String qid) {
        this.qid = qid;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAttempt() {
        return attempt;
    }

    public void setAttempt(String attempt) {
        this.attempt = attempt;
    }
}
