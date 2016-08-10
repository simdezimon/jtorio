package com.simdezimon.jtorio.prototypes;

public class Resource extends Recipe{


	private double hardness;
	private double miningTime;
	
	public Resource(String name, double hardness,double miningTime, String category) {
		super(name, miningTime / (3-hardness), category,"resource");
		this.hardness = hardness;
		this.miningTime = miningTime;
	}
	
}
