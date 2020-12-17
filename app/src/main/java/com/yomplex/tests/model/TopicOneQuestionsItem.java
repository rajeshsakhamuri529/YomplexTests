package com.yomplex.tests.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopicOneQuestionsItem implements Serializable {

	@SerializedName("bank")
	private String bank;

	@SerializedName("level")
	private String level;

	@SerializedName("_id")
	private String id;

	@SerializedName("text")
	private String text;

	@SerializedName("type")
	private int type;

	@SerializedName("funny")
	private boolean funny;

	public void setBank(String bank){
		this.bank = bank;
	}

	public String getBank(){
		return bank;
	}

	public void setLevel(String level){
		this.level = level;
	}

	public String getLevel(){
		return level;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setType(int type){
		this.type = type;
	}

	public int getType(){
		return type;
	}

	public void setFunny(boolean funny){
		this.funny = funny;
	}

	public boolean isFunny(){
		return funny;
	}

	@Override
 	public String toString(){
		return
			"TopicOneQuestionsItem{" +
			"bank = '" + bank + '\'' +
			",level = '" + level + '\'' +
			",_id = '" + id + '\'' +
			",text = '" + text + '\'' +
			",type = '" + type + '\'' +
			",funny = '" + funny + '\'' +
			"}";
		}
}