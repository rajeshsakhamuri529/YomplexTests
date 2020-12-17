package com.yomplex.tests.model;

import com.google.gson.annotations.SerializedName;

public class Syllabus {

	@SerializedName("displayTitle")
	private String displayTitle;

	@SerializedName("cd")
	private String cd;

	@SerializedName("__v")
	private int V;

	@SerializedName("index")
	private int index;

	@SerializedName("_id")
	private String id;

	@SerializedName("title")
	private String title;

	@SerializedName("slug")
	private String slug;

	@SerializedName("itunesid")
	private String itunesid;

	@SerializedName("desc")
	private Object desc;

	@SerializedName("ud")
	private String ud;

	@SerializedName("status")
	private String status;

	public void setDisplayTitle(String displayTitle){
		this.displayTitle = displayTitle;
	}

	public String getDisplayTitle(){
		return displayTitle;
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

	public void setIndex(int index){
		this.index = index;
	}

	public int getIndex(){
		return index;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setSlug(String slug){
		this.slug = slug;
	}

	public String getSlug(){
		return slug;
	}

	public void setItunesid(String itunesid){
		this.itunesid = itunesid;
	}

	public String getItunesid(){
		return itunesid;
	}

	public void setDesc(Object desc){
		this.desc = desc;
	}

	public Object getDesc(){
		return desc;
	}

	public void setUd(String ud){
		this.ud = ud;
	}

	public String getUd(){
		return ud;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"Syllabus{" + 
			"displayTitle = '" + displayTitle + '\'' + 
			",cd = '" + cd + '\'' + 
			",__v = '" + V + '\'' + 
			",index = '" + index + '\'' + 
			",_id = '" + id + '\'' + 
			",title = '" + title + '\'' + 
			",slug = '" + slug + '\'' + 
			",itunesid = '" + itunesid + '\'' + 
			",desc = '" + desc + '\'' + 
			",ud = '" + ud + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}