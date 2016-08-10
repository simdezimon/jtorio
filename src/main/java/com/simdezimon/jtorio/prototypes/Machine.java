package com.simdezimon.jtorio.prototypes;

import java.util.Set;

public class Machine {
	private final String name;
	private final String type;
	private Set<String> categories;
	private String recipeType;
	private double craftingSpeed;
	private double energyUsage;
	private String energyType;
	private double pollution;
	
	public Machine(String name, String type, Set<String> categories,String recipeType, double craftingSpeed, double energyUsage, String energyType, double pollution) {
		this.name = name;
		this.type = type;
		this.categories = categories;
		this.craftingSpeed = craftingSpeed;
		this.energyType = energyType;
		this.energyUsage = energyUsage;
		this.pollution = pollution;
		this.recipeType = recipeType;
	}
	public Set<String> getCategories() {
		return categories;
	}
	public void setCategories(Set<String> categories) {
		this.categories = categories;
	}
	public double getCraftingSpeed() {
		return craftingSpeed;
	}
	public void setCraftingSpeed(double craftingSpeed) {
		this.craftingSpeed = craftingSpeed;
	}
	public String getName() {
		return name;
	}
	public String getType() {
		return type;
	}
	public double getEnergyUsage() {
		return energyUsage;
	}
	public void setEnergyUsage(double energyUsage) {
		this.energyUsage = energyUsage;
	}
	public String getEnergyType() {
		return energyType;
	}
	public void setEnergyType(String energyType) {
		this.energyType = energyType;
	}
	public double getPollution() {
		return pollution;
	}
	public void setPollution(double pollution) {
		this.pollution = pollution;
	}
	
	
	public String getRecipeType() {
		return recipeType;
	}
	public void setRecipeType(String recipeType) {
		this.recipeType = recipeType;
	}

	
	
	@Override
	public String toString() {
		return "Machine [name=" + name + ", type=" + type + ", categories=" + categories + ", recipeType=" + recipeType
				+ ", craftingSpeed=" + craftingSpeed + ", energyUsage=" + energyUsage + ", energyType=" + energyType
				+ ", pollution=" + pollution + "]";
	}
	public boolean isElectric() {
		return energyType.equals("electric");
	}
	
	
	
	
	
	
}
