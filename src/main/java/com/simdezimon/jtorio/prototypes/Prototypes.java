package com.simdezimon.jtorio.prototypes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Prototypes {

	private final Map<String, Recipe> recipes = new LinkedHashMap<>();
	private final Map<String, Item> items = new LinkedHashMap<>();
	private final Map<String, Machine> machines = new LinkedHashMap<>();

	public Prototypes() {

	}

	public Map<String, Item> getItems() {
		return items;
	}

	public Map<String, Recipe> getRecipes() {
		return recipes;
	}
	

	public void addItem(Item item){
		items.put(item.getName(), item);
	}

	public void addRecipe(Recipe recipe){
		recipes.put(recipe.getName(), recipe);
	}
	
	public Map<String, Machine> getMachines() {
		return machines;
	}
	
	public void addMachine(Machine machine){
		machines.put(machine.getName(), machine);
	}
	
	public Machine getBestMachine(Recipe recipe){
		Machine best = null;
		for(Machine machine: machines.values()){
			if(machine.getRecipeType().equals(recipe.getType()) &&( recipe.getCategory() == null ||  machine.getCategories().contains(recipe.getCategory()))){
				if(best == null || machine.getCraftingSpeed() > best.getCraftingSpeed()){
					best = machine;
				}
			}
		}
		return best;
	}
	
	@Override
	public String toString() {
		return "Prototypes [#recipes=" + recipes.size() + ", #items="+items.size()+", #,machines"+machines.size()+"]";
	}
}
