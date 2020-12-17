package com.yomplex.tests.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Topic implements Serializable {

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("__v")
	private int V;

	@SerializedName("index")
	private int index;
	@SerializedName("display_no")
	private int displayNo;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("_id")
	private String id;

	@SerializedName("folderName")
	private String folderName;

	public int getDisplayNo() {
		return displayNo;
	}

	public void setDisplayNo(int displayNo) {
		this.displayNo = displayNo;
	}

	@SerializedName("category")
	private Object category;

	@SerializedName("title")
	private String title;

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
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

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setFolderName(String folderName){
		this.folderName = folderName;
	}

	public String getFolderName(){
		return folderName;
	}

	public void setCategory(Object category){
		this.category = category;
	}

	public Object getCategory(){
		return category;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	@Override
 	public String toString(){
		return
			"Topic{" +
			"updated_at = '" + updatedAt + '\'' +
			",__v = '" + V + '\'' +
			",index = '" + index + '\'' +
			",created_at = '" + createdAt + '\'' +
			",_id = '" + id + '\'' +
			",folderName = '" + folderName + '\'' +
			",category = '" + category + '\'' +
			",title = '" + title + '\'' +
			"}";
		}
}