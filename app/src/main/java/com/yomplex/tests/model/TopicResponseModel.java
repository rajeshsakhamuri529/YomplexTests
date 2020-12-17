package com.yomplex.tests.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TopicResponseModel {

	@SerializedName("branches")
	private List<BranchesItem> branches;

	public void setBranches(List<BranchesItem> branches){
		this.branches = branches;
	}

	public List<BranchesItem> getBranches(){
		return branches;
	}

	@Override
 	public String toString(){
		return
			"TopicResponseModel{" +
			"branches = '" + branches + '\'' +
			"}";
		}
}