package com.simdezimon.jtorio;

import java.util.List;

import com.google.gson.annotations.SerializedName;

class JsonItem {
	private String name;
	private int stackSize;
	private String order;
	private List<String> flags;
	private String icon;
	private String subgroup;
	private String type;
	private String placeResult;
	private String fuelValue;
	
	public JsonItem() {
	}
	
	
	public String getName() {
		return name;
	}
	public int getStackSize() {
		return stackSize;
	}
	public String getOrder() {
		return order;
	}
	public List<String> getFlags() {
		return flags;
	}

	public String getIcon() {
		return icon;
	}
	
	public Double getFuelValue(){
		return fuelValue != null ? Double.parseDouble(fuelValue.replaceAll("[^0-9.]", "")) : null;
		
	}

	public String getType() {
		return type;
	}

	public String getSubgroup() {
		return subgroup;
	}


	public String getPlaceResult() {
		return placeResult;
	}


	@Override
	public String toString() {
		return "Item [name=" + name + "]";
	}
	
	
}
