package com.yomplex.tests.model;

public class TestQuizFinal {

    private String serialNo;
    private String title;
    private String typeofPlay;
    private int totalQuestions;
    private String answerstatus;
    private String questionAnswers;
    private String questionPathType;
    private String pdate;
    private String timetaken;
    private String options;
    private String status;
    private String testtype;
    private String readdata;
    private String originalname;



    public TestQuizFinal(){

    }

    public TestQuizFinal(String serialno, String title, String typeofPlay,
                         int totalQuestions, String answerstatus, String questionAnswers, String questionPathType, String pdate, String timetaken, String options, String status,String testtype,String readdata,String originalname){

        this.serialNo = serialno;
        this.title = title;
        this.typeofPlay = typeofPlay;
        this.totalQuestions = totalQuestions;
        this.answerstatus = answerstatus;
        this.questionAnswers = questionAnswers;
        this.questionPathType = questionPathType;
        this.pdate = pdate;
        this.timetaken = timetaken;
        this.options = options;
        this.status = status;
        this.testtype = testtype;
        this.readdata = readdata;
        this.originalname = originalname;

    }

    public String getReaddata() {
        return readdata;
    }

    public void setReaddata(String readdata) {
        this.readdata = readdata;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public String getTimetaken() {
        return timetaken;
    }

    public void setTimetaken(String timetaken) {
        this.timetaken = timetaken;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTypeofPlay() {
        return typeofPlay;
    }

    public void setTypeofPlay(String typeofPlay) {
        this.typeofPlay = typeofPlay;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getAnswerstatus() {
        return answerstatus;
    }

    public void setAnswerstatus(String answerstatus) {
        this.answerstatus = answerstatus;
    }

    public String getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(String questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public String getQuestionPathType() {
        return questionPathType;
    }

    public void setQuestionPathType(String questionPathType) {
        this.questionPathType = questionPathType;
    }

    public String getPdate() {
        return pdate;
    }

    public void setPdate(String pdate) {
        this.pdate = pdate;
    }

    public String getTesttype() {
        return testtype;
    }

    public void setTesttype(String testtype) {
        this.testtype = testtype;
    }

    public String getOriginalname() {
        return originalname;
    }

    public void setOriginalname(String originalname) {
        this.originalname = originalname;
    }
}
