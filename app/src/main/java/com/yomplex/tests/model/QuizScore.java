package com.yomplex.tests.model;

public class QuizScore {


    private String weekofyear;
    private String highestscore;
    private String testtype;

    public QuizScore(){

    }
    public QuizScore(String weekofyear,String highestscore,String testtype){
        this.weekofyear = weekofyear;
        this.highestscore = highestscore;
        this.testtype = testtype;

    }
    public String getWeekofyear() {
        return weekofyear;
    }

    public void setWeekofyear(String weekofyear) {
        this.weekofyear = weekofyear;
    }

    public String getHighestscore() {
        return highestscore;
    }

    public void setHighestscore(String highestscore) {
        this.highestscore = highestscore;
    }

    public String getTesttype() {
        return testtype;
    }

    public void setTesttype(String testtype) {
        this.testtype = testtype;
    }
}
