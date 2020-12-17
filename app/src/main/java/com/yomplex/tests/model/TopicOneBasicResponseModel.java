package com.yomplex.tests.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicOneBasicResponseModel {

	@SerializedName("questionCount")
	private int questionCount;

	@SerializedName("questions")
	private List<TopicOneQuestionsItem> questions;

	public void setQuestionCount(int questionCount){
		this.questionCount = questionCount;
	}

	public int getQuestionCount(){
		return questionCount;
	}

	public void setQuestions(List<TopicOneQuestionsItem> questions){
		this.questions = questions;
	}

	public List<TopicOneQuestionsItem> getQuestions(){
		return questions;
	}

	@Override
 	public String toString(){
		return
			"TopicOneBasicResponseModel{" +
			"questionCount = '" + questionCount + '\'' +
			",questions = '" + questions + '\'' +
			"}";
		}
}