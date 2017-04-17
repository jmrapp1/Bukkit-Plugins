package com.jmr.buyable;

import org.bukkit.material.MaterialData;

public class BuyableItem {

	private MaterialData item;
	private int cost;
	
	public BuyableItem(MaterialData item, int cost) {
		this.item = item;
		this.cost = cost;
	}
	
	public boolean ownsItem() {
		return false;
	}
	
	public int getCost() {
		return cost;
	}
	
}
