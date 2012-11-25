package com.FouregoStudio.Models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GSONListSection {
	
	@SerializedName("name")
	public String name;
	
	public List<String> chapters;

}
