package com.simdezimon.jtorio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

class JsonPrototypes {
	private final List<JsonRecipe> recipes = new ArrayList<>();
	private final List<JsonItem> items  = new ArrayList<>();
	private  final List<JsonResource> resources = new ArrayList<>();
	private List<JsonMachine> machines = new ArrayList<>();
	private List<JsonFluid> fluids = new ArrayList<>();
	
	
	public JsonPrototypes() {
	}
	
	public List< JsonRecipe> getRecipes() {
		return recipes;
	}
	
	public void addItem(JsonItem item){
		items.add( item);
	}

	public void addRecipe(JsonRecipe recipe){
		recipes.add( recipe);
	}
	

	public List< JsonItem> getItems() {
		return items;
	}

	public List< JsonResource> getResources() {
		return resources;
	}
	
	public void addResources(JsonResource resource){
		resources.add(resource);
	}

	public void addMachine(JsonMachine machine){
		machines.add(machine);
	}
	
	public List<JsonMachine> getMachines() {
		return machines ;
	}
	
	public void addFluid(JsonFluid fluid){
		fluids.add(fluid);
	}
	
	public List<JsonFluid> getFluids() {
		return fluids;
	}
	
	@Override
	public String toString() {
		return "DataTable [recipes=" + recipes.size() + ", items="+items.size()+"]";
	}
	
	
}
