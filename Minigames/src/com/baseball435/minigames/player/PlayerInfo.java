package com.baseball435.minigames.player;

import org.bukkit.inventory.ItemStack;

import com.baseball435.minigames.minigame.Team;

public class PlayerInfo {

	private Team team;
	private int score;
	private ItemStack[] savedInventory, savedArmor;
	
	public PlayerInfo() {
	}
	
	public int getScore() {
		return score;
	}
	
	public ItemStack[] getSavedInventory() {
		return savedInventory;
	}
	
	public ItemStack[] getSavedArmor() {
		return savedArmor;
	}
	
	public void saveArmor(ItemStack[] savedArmor) {
		this.savedArmor = savedArmor;
	}
	
	public void saveInventory(ItemStack[] savedInventory) {
		this.savedInventory = savedInventory;
	}
	
	public void addScore(int amount) {
		score += amount;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	public boolean inMinigame() {
		return team != null;
	}
	
	public void setTeam(Team team) {
		this.team = team;
	}
	
	public Team getTeam() {
		return team;
	}
	
}
