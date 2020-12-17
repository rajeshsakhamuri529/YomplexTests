package com.yomplex.tests.model;

public class Challenge {

    private String date;
    private int challengeQuizCorrectAnswers;
    private int challengeTestCorrectAnswers;
    private int challengeQuizStatus;
    private int challengeTestStatus;


    public Challenge(String date, int quizanswers, int quizstatus, int testanswers, int teststatus){
        this.date = date;
        this.challengeQuizCorrectAnswers = quizanswers;
        this.challengeTestCorrectAnswers = testanswers;
        this.challengeQuizStatus = quizstatus;
        this.challengeTestStatus = teststatus;
    }

    public Challenge() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getChallengeQuizCorrectAnswers() {
        return challengeQuizCorrectAnswers;
    }

    public void setChallengeQuizCorrectAnswers(int challengeQuizCorrectAnswers) {
        this.challengeQuizCorrectAnswers = challengeQuizCorrectAnswers;
    }

    public int getChallengeTestCorrectAnswers() {
        return challengeTestCorrectAnswers;
    }

    public void setChallengeTestCorrectAnswers(int challengeTestCorrectAnswers) {
        this.challengeTestCorrectAnswers = challengeTestCorrectAnswers;
    }

    public int getChallengeQuizStatus() {
        return challengeQuizStatus;
    }

    public void setChallengeQuizStatus(int challengeQuizStatus) {
        this.challengeQuizStatus = challengeQuizStatus;
    }

    public int getChallengeTestStatus() {
        return challengeTestStatus;
    }

    public void setChallengeTestStatus(int challengeTestStatus) {
        this.challengeTestStatus = challengeTestStatus;
    }
}
