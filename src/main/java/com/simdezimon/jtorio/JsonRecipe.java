package com.simdezimon.jtorio;

import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.SerializedName;

class JsonRecipe {
	private String name;
	
	@SerializedName("energy_required")
	private Double energyRequired;
	private List<JsonRecipeMaterial> ingredients;
	private List<JsonRecipeMaterial> results;
	private String result;
	@SerializedName("result_count")
	private Double resultCount;
	private String category;
	
	public JsonRecipe() {
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getEnergyRequired() {
		return energyRequired != null ? energyRequired : 1;
	}
	public void setEnergyRequired(double energy_required) {
		this.energyRequired = energy_required;
	}
	
	public String getCategory() {
		return category != null ? category : "crafting";
	}

	public List<JsonRecipeMaterial> getIngredients() {
		if(ingredients == null){
			return Collections.emptyList();
		}
		return ingredients;
	}

	public void setIngredients(List<JsonRecipeMaterial> ingredients) {
		this.ingredients = ingredients;
	}

	public List<JsonRecipeMaterial> getResults() {
		return results != null ? results : Collections.singletonList(new JsonRecipeMaterial(result,resultCount != null ? resultCount : 1));
	}

	public void setResults(List<JsonRecipeMaterial> results) {
		this.results = results;
	}

	@Override
	public String toString() {
		return "Recipe [name=" + name + ", energyRequired=" + energyRequired + ", ingredients=" + ingredients
				+ ", results=" + getResults() + "]";
	}
}
