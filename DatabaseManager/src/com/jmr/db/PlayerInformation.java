package com.jmr.db;

public class PlayerInformation {

	private String name, rank;
	private int coins;
	
	public PlayerInformation(String name, String rank, int coins) {
		this.name = name;
		this.rank = rank;
		this.coins = coins;
	}
	
	public String getName() {
		return name;
	}
	
	public String getRank() {
		return rank;
	}
	
	public int getCoins() {
		return coins;
	}
	
}
