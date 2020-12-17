package com.yomplex.tests.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BranchesItem implements Serializable {

	@SerializedName("cd")
	private String cd;

	@SerializedName("__v")
	private int V;

	@SerializedName("topic")
	private Topic topic;

	@SerializedName("_id")
	private String id;

	@SerializedName("ud")
	private String ud;

	private int basic = 0;

	private int intermediate = 0;

	private int advance = 0;

	public int getBasic() {
		return basic;
	}

	public void setBasic(int basic) {
		this.basic = basic;
	}

	public int getIntermediate() {
		return intermediate;
	}

	public void setIntermediate(int intermediate) {
		this.intermediate = intermediate;
	}

	public int getAdvance() {
		return advance;
	}

	public void setAdvance(int advance) {
		this.advance = advance;
	}

	public void setCd(String cd){
		this.cd = cd;
	}

	public String getCd(){
		return cd;
	}

	public void setV(int V){
		this.V = V;
	}

	public int getV(){
		return V;
	}

	public void setTopic(Topic topic){
		this.topic = topic;
	}

	public Topic getTopic(){
		return topic;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setUd(String ud){
		this.ud = ud;
	}

	public String getUd(){
		return ud;
	}

	@Override
	public String toString() {
		return "BranchesItem{" +
				"cd='" + cd + '\'' +
				", V=" + V +
				", topic=" + topic +
				", id='" + id + '\'' +
				", ud='" + ud + '\'' +
				", basic=" + basic +
				", intermediate=" + intermediate +
				", advance=" + advance +
				'}';
	}
}