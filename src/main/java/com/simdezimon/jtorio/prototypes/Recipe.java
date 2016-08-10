package com.simdezimon.jtorio.prototypes;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

	private final String name;
	private double energy;
	private final List<RecipeMaterial> ingredients;
	private final List<RecipeMaterial> results;
	private String category;
	private String type;
	
	public Recipe(String name,double energy, String category, String type) {
		this.name = name;
		this.energy = energy;
		ingredients = new ArrayList<>();
		results = new ArrayList<>();
		this.category = category;
		this.type = type;
	}


	public String getName() {
		return name;
	}

	public List<RecipeMaterial> getIngredients() {
		return ingredients;
	}


	public List<RecipeMaterial> getResults() {
		return results;
	}


	public double getEnergy() {
		return energy;
	}


	public void setEnergy(double energy) {
		this.energy = energy;
	}

	

	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}
	
	


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	@Override
	public String toString() {
		return "Recipe [name=" + name + ", energy=" + energy + ", ingredients=" + ingredients + ", results=" + results
				+ ", category=" + category + ", type=" + type + "]";
	}



	
}
