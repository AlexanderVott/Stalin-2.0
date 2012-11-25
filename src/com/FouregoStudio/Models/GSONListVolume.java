package com.FouregoStudio.Models;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GSONListVolume {
	
	public List<GSONListSection> sections;
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("time_period")
	public String time_period;
	
	@SerializedName("warning")
	public boolean warning;
	
}
