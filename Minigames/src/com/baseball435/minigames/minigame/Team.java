package com.baseball435.minigames.minigame;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.material.Wool;

import com.baseball435.minigames.Main;
import com.baseball435.minigames.PlayerManager;

public class Team {

	private CopyOnWriteArrayList<Player> players = new CopyOnWriteArrayList<Player>();
	private final int maxAmount, minStartingAmount;
	private final boolean friendlyFire;
	private final Location startLocation, quitLocation;
	private final Minigame minigame;
	private DyeColor color;
	private String colorString;
	private int score;
	
	public Team(Minigame minigame, DyeColor color, String colorString, Location startLocation, Location quitLocation, int maxAmount, boolean friendlyFire, int minStartingAmount) {
		this.minigame = minigame;
		this.color = color;
		this.colorString = colorString;
		this.maxAmount = maxAmount;
		this.friendlyFire = friendlyFire;
		this.minStartingAmount = minStartingAmount;
		this.startLocation = startLocation;
		this.quitLocation = quitLocation;
	}
	
	public Minigame getMinigame() {
		return minigame;
	}
	
	public int getScore() {
		return score;
	}
	
	public DyeColor getColor() {
		return color;
	}
	
	public String getColorAsString() {
		return colorString;
	}
	
	public boolean canAttackEachother() {
		return friendlyFire;
	}
	
	public Location getStartLocation() {
		return startLocation;
	}
	
	public Location getEndLocation() {
		return quitLocation;
	}
	
	public void addScore(int amount) {
		score += amount;
	}
	
	public void setColors(String colorString, DyeColor color) {
		this.colorString = colorString;
		this.color = color;
	}
	
	public Player getMostKills() {
		Player player = null;
		int maxScore = 0;
		for (Player p : players) {
			if (PlayerManager.getPlayerInfo(p).getScore() > maxScore) {
				maxScore = PlayerManager.getPlayerInfo(p).getScore();
				player = p;
			}
		}
		return player;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
		PlayerManager.getPlayerInfo(player).setTeam(this);
		PlayerManager.getPlayerInfo(player).setScore(0);
		player.teleport(startLocation);
	}
	
	@SuppressWarnings("deprecation")
	public void setHelmet(Player player) {
		player.getInventory().setHelmet(new Wool(color).toItemStack());
		player.updateInventory();
	}
	
	public boolean isFull() {
		return players.size() == maxAmount;
	}
	
	public boolean removePlayer(Player player) {
		return players.remove(player);
	}
	
	public boolean readyToStart() {
		return players.size() >= minStartingAmount;
	}
	
	public int getSize() {
		return players.size();
	}
	
	public void moveAllToQuit() {
		for (Player player : players) {
			moveToQuit(player);
			PlayerManager.getPlayerInfo(player).setTeam(null);
			minigame.removePlayer(player, false);
		}
	}
	
	public void giveAward(Player player, int amount) {
		Main.ECON.depositPlayer(player.getName(), amount);
	}
	
	public void giveAllAward(int amount, String message) {
		for (Player player : players) {
			giveAward(player, amount);
			player.sendMessage(message);
		}
	}
	
	public void moveToQuit(Player player) {
		player.teleport(quitLocation);
	}
	
	public void clearTeam() {
		players.clear();
		
	}
	
}
