package com.simdezimon.jtorio;

public class JsonFluid {
	private String name;
	private int defaultTemperature;
	private int maxTemperature;
	private String heatCapacity;
	
	public JsonFluid() {
	}

	public String getName() {
		return name;
	}

	public int getDefaultTemperature() {
		return defaultTemperature;
	}

	public int getMaxTemperature() {
		return maxTemperature;
	}

	public double getHeatCapacity() {
		return heatCapacity != null ? Double.parseDouble(heatCapacity.replaceAll("[^0-9.]", "")) : 0;
	}
	
	
}
