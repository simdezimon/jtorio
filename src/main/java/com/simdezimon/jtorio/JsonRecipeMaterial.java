package com.simdezimon.jtorio;

class JsonRecipeMaterial {
	private String name;
	private double amount = 1;
	private String type = "item";
	
	public JsonRecipeMaterial() {
	}

	public JsonRecipeMaterial(String name, double amount) {
		this.name = name;
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RecipeMaterial [name=" + name + ", amount=" + amount + ", type=" + type + "]";
	}
	
	
}
