package com.simdezimon.jtorio;

import java.util.List;

import com.google.common.collect.Lists;

public class JsonResource {
	private String name;
	private Minable minable;
	private String category;
	
	public static class Minable{
		private double hardness;
		private double miningTime;
		private String result;
		private List<JsonRecipeMaterial> results;
		public double getHardness() {
			return hardness;
		}
		public double getMiningTime() {
			return miningTime;
		}
		public List<JsonRecipeMaterial> getResults() {
			return results != null ? results : Lists.newArrayList(new JsonRecipeMaterial(result, 1));
		}
		
		
	}

	public String getName() {
		return name;
	}

	public Minable getMinable() {
		return minable;
	}
	
	public String getCategory() {
		return category != null ? category : "basic-solid";
	}
	
}


