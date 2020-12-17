package com.yomplex.tests.model;

import com.google.gson.annotations.SerializedName;

public class CoursesResponseModel {

	@SerializedName("syllabus")
	private Syllabus syllabus;

	@SerializedName("cd")
	private String cd;

	@SerializedName("__v")
	private int V;

	@SerializedName("_id")
	private String id;

	@SerializedName("ud")
	private String ud;

	public void setSyllabus(Syllabus syllabus){
		this.syllabus = syllabus;
	}

	public Syllabus getSyllabus(){
		return syllabus;
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
 	public String toString(){
		return 
			"CoursesResponseModel{" + 
			"syllabus = '" + syllabus + '\'' + 
			",cd = '" + cd + '\'' + 
			",__v = '" + V + '\'' + 
			",_id = '" + id + '\'' + 
			",ud = '" + ud + '\'' + 
			"}";
		}
}