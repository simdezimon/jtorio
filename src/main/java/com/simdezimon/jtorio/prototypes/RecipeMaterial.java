package com.simdezimon.jtorio.prototypes;

public class RecipeMaterial {
	private Item item;
	private double amount;
	
	public RecipeMaterial(Item item, double amount) {
		this.item = item;
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}


	public Item getItem() {
		return item;
	}

	@Override
	public String toString() {
		return item.getName() + "*" + amount;
	}
	
	
	
}
