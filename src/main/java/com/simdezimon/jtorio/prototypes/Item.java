package com.simdezimon.jtorio.prototypes;

public class Item {

	private String name;
	private String type;
	
	public Item(String name,String type) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Item [name=" + name + "]";
	}

	public String getName() {
		return name;
	}
	
	public String getType() {
		return type;
	}
	
	
}
