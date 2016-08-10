package com.simdezimon.jtorio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.optim.OptimizationData;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.linear.LinearConstraint;
import org.apache.commons.math3.optim.linear.LinearConstraintSet;
import org.apache.commons.math3.optim.linear.LinearObjectiveFunction;
import org.apache.commons.math3.optim.linear.NonNegativeConstraint;
import org.apache.commons.math3.optim.linear.PivotSelectionRule;
import org.apache.commons.math3.optim.linear.Relationship;
import org.apache.commons.math3.optim.linear.SimplexSolver;

import com.simdezimon.jtorio.prototypes.Item;
import com.simdezimon.jtorio.prototypes.Machine;
import com.simdezimon.jtorio.prototypes.Prototypes;
import com.simdezimon.jtorio.prototypes.Recipe;
import com.simdezimon.jtorio.prototypes.RecipeMaterial;
import com.simdezimon.jtorio.prototypes.Resource;

public class BuildSolver {
	private BuildSolver(Prototypes prototypes, Map<Item,Double> results) {
		Map<Item,Integer> itemLookup = new HashMap<>();
		int i = 0;
		
		Collection<Item> items = prototypes.getItems().values();
		Collection<Recipe> recipes = prototypes.getRecipes().values();
		
		
		for(Item item : items){
			itemLookup.put(item, i);
			i++;
		}
		
		int epId = itemLookup.get(prototypes.getItems().get("electric-power"));
		int bpId = itemLookup.get(prototypes.getItems().get("burner-power"));
		int poId = itemLookup.get(prototypes.getItems().get("pollution"));
		double[][] constraints = new double[items.size()][recipes.size()];
		double[] lofds = new double[recipes.size()];
		i = 0;
		for(Recipe recipe : recipes){
			Machine machine = prototypes.getBestMachine(recipe);
			if(machine != null){
				lofds[i] = recipe instanceof Resource ? 100:1;
				for(RecipeMaterial rm : recipe.getIngredients()){
					if(!itemLookup.containsKey(rm.getItem())){
						throw new NullPointerException(rm.getItem().getName()+" not found.");
					}
					constraints[itemLookup.get(rm.getItem())][i] = - rm.getAmount() / recipe.getEnergy() * machine.getCraftingSpeed();
				}
				for(RecipeMaterial rm : recipe.getResults()){
					if(!itemLookup.containsKey(rm.getItem())){
						throw new NullPointerException(rm.getItem().getName()+" not found.");
					}
					constraints[itemLookup.get(rm.getItem())][i] = rm.getAmount()  / recipe.getEnergy() * machine.getCraftingSpeed();
				}
				if(machine.isElectric()){
					constraints[epId][i] -= machine.getEnergyUsage();
				} else {
					constraints[bpId][i] -= machine.getEnergyUsage();
				}
				constraints[poId][i] -= machine.getPollution();
			}else {
				System.err.println(recipe.getName()+" has no machine.");
			}
			i++;
		}
		
		

		i = 0;
		List<LinearConstraint> lc = new ArrayList<>();
		for(Item item : items){
			lc.add(new LinearConstraint(constraints[i],Relationship.GEQ,results.containsKey(item)? results.get(item): 0));
			i++;
		}
 
		LinearObjectiveFunction lof = new LinearObjectiveFunction(lofds,0);
		LinearConstraintSet lcs = new LinearConstraintSet(lc);
		SimplexSolver solver = new SimplexSolver();
		PointValuePair result = solver.optimize(lof,lcs,new NonNegativeConstraint(true),PivotSelectionRule.BLAND);
		
		

		i = 0;
		
		System.out.println("value:" +result.getValue());
		for(Recipe recipe : recipes){
			Machine machine = prototypes.getBestMachine(recipe);
			double r = result.getPoint()[i];
			if(r > 0.00001 ){
				System.out.printf("%s(%s) * %.5f\n" , machine.getName(),recipe.getName(),r);
			}
			i++;
		}
		System.out.println();
		
		i = 0;
		for(Recipe recipe : recipes){
			Machine machine = prototypes.getBestMachine(recipe);
			double r = result.getPoint()[i];
			if(r > 0.00001 ){
				if(recipe instanceof Resource){
					System.out.printf("%s * %.5f\n",recipe.getName(),r / recipe.getEnergy() * machine.getCraftingSpeed());
				}
			}
			i++;
		}
		System.out.println();
		for(Item item : items){
			int itemId = itemLookup.get(item);
			double itemCount = 0;
			for(i = 0; i < recipes.size();i++){
				itemCount += constraints[itemId][i] * result.getPoint()[i];
			}
			if(Math.abs(itemCount)>0.00001){
				System.out.println(item.getName()+"*"+itemCount);
			}
			i++;
		}
	}
	
	public static void solve(Prototypes prototypes, Map<Item,Double> results){
		new BuildSolver(prototypes, results);
	}
}
