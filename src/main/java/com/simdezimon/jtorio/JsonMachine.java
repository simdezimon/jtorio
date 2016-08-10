package com.simdezimon.jtorio;

import java.util.Collections;
import java.util.Set;

import com.google.gson.annotations.SerializedName;

public class JsonMachine {
	private String name;
	private String type;
	@SerializedName(value = "crafting_categories", alternate = { "resource_categories" })
	private Set<String> categories;
	
	@SerializedName(value = "fluid")
	private String category;
	
	@SerializedName(value = "crafting_speed", alternate = { "mining_speed", "pumping_speed", "fluid_usage_per_tick"})
	private Double craftingSpeed;
	@SerializedName("mining_power")
	private Double miningPower;
	@SerializedName(value = "energy_usage", alternate = {"energy_consumption"})
	private String energyUsage;
	@SerializedName(value = "energy_source", alternate = {"burner"})
	private EnergySource energySource;

	public static class EnergySource {

		private String type;
		private Double effectivity;
		private Double emissions;
		@SerializedName("usage_priority")
		private String priority;

		public EnergySource() {
		}

		public EnergySource(String type, Double effectivity, Double emissions) {
			this.type = type;
			this.effectivity = effectivity;
			this.emissions = emissions;
		}

		public String getType() {
			return type != null ? type : "burner";
		}

		public double getEffectivity() {
			return effectivity != null ? effectivity : 1;
		}

		public double getEmissions() {
			return emissions != null ? emissions : 0;
		}
		
		public boolean isOutput(){
			return priority != null && (priority.split("-")[1].equals("output"));
		}

	}

	public double getEnergyUsage() {
		return energyUsage != null ? Double.parseDouble(energyUsage.replaceAll("[^0-9.]", "")) : 0;
	}

	public JsonMachine() {
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Set<String> getCategories() {
		return  categories != null ? categories : category != null ? Collections.singleton(category) : null;
	}

	public double getCraftingSpeed() {
		if(type.equals("boiler")){
			return getEnergyUsage()/1000.;
		}
		if(type.equals("generator") || type.equals("offshore-pump")){
			return craftingSpeed*60. ;
		}
		return craftingSpeed != null ? craftingSpeed : 1;
	}

	public double getMiningPower() {
		return miningPower;
	}

public EnergySource getEnergySource() {
	return energySource != null ? energySource : new EnergySource("electric",1.,0.);
}

}
