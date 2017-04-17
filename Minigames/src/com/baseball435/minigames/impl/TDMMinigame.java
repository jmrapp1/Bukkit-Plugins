package com.baseball435.minigames.impl;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.baseball435.minigames.Main;
import com.baseball435.minigames.MinigameManager;
import com.baseball435.minigames.PlayerManager;
import com.baseball435.minigames.minigame.TeamMinigame;

public abstract class TDMMinigame extends TeamMinigame {
	
	public TDMMinigame(String name, long timeLimit, int scoreObjective, Location quitLocation, Location area1, Location area2, Location start1, Location start2, int maxPlayerPerTeam) {
		super(name, timeLimit, scoreObjective, quitLocation, area1, area2, start1, start2, maxPlayerPerTeam);
	}

	@Override
	public void onStart() {
		Bukkit.getServer().broadcastMessage(ChatColor.DARK_AQUA + "The " + name + " minigame has just started!");
	}
	
	@Override
	public void onTick() {
		checkEnd();
	}

	@Override
	public void onEnd() {
		reloadDestroyedBlocks();
		if (team1.getScore() > team2.getScore()) {
			Main.getBukkitServer().broadcastMessage(ChatColor.GREEN + "The " + team1.getColorAsString() + " team won the " + name + " minigame with " + ChatColor.RED + team1.getScore() + ChatColor.GREEN + " kill(s)!");
			if (PlayerManager.getPlayerInfo(team1.getMostKills()) != null) {
				Main.getBukkitServer().broadcastMessage(team1.getMostKills().getDisplayName() + ChatColor.GREEN + " was the MVP with " + ChatColor.RED + PlayerManager.getPlayerInfo(team1.getMostKills()).getScore() + ChatColor.GREEN + " kill(s)");
				team1.giveAward(team1.getMostKills(), 10);
				team1.getMostKills().sendMessage(ChatColor.GOLD + "You were awarded an extra 10 " + Main.ECON.currencyNamePlural() + " for being the MVP on your team!");
			}
			team1.giveAllAward(25, ChatColor.GOLD + "You were awarded 25 " + Main.ECON.currencyNamePlural() + " for winning the minigame!");
		} else if (team2.getScore() > team1.getScore()){
			Main.getBukkitServer().broadcastMessage(ChatColor.GREEN + "The " + team2.getColorAsString() + " team won the " + name + " minigame with " + ChatColor.RED + team2.getScore() + ChatColor.GREEN + " kill(s)!");
			if (PlayerManager.getPlayerInfo(team2.getMostKills()) != null) {
				Main.getBukkitServer().broadcastMessage(ChatColor.GOLD + team2.getMostKills().getDisplayName() + ChatColor.GREEN + " was the MVP with " + ChatColor.RED + PlayerManager.getPlayerInfo(team2.getMostKills()).getScore() + ChatColor.GREEN + " kill(s)");
				team2.giveAward(team2.getMostKills(), 10);
				team2.getMostKills().sendMessage(ChatColor.GOLD + "You were awarded an extra 10 " + Main.ECON.currencyNamePlural() + " for being the MVP on your team!");
			}
			team2.giveAllAward(25, ChatColor.GOLD + "You were awarded 25 " + Main.ECON.currencyNamePlural() + " for winning the minigame!");
		} else {
			Main.getBukkitServer().broadcastMessage(ChatColor.GREEN + "Both teams tied the " + name + " minigame with " + ChatColor.RED + team1.getScore() + ChatColor.GREEN + " kill(s)!");
		}
		team1.moveAllToQuit();
		team2.moveAllToQuit();
		team1.clearTeam();
		team2.clearTeam();
	}

	@Override
	public void checkNoPlayers() {
		if (team1.getSize() == 0 && team2.getSize() == 0) {
			MinigameManager.endMinigame(this);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onAddPlayer(Player player) {
		giveSpellItems(player, true);
		PlayerManager.getPlayerInfo(player).getTeam().setHelmet(player);
		((Damageable)player).setHealth(20);
		com.baseball435.spells.PlayerManager.getPlayerInfo(player).setMana(100);
		com.baseball435.spells.PlayerManager.resetScoreboard(player);
	}

	@Override
	public void onRemovePlayer(Player player) {
		giveSavedInventory(player);
		((Damageable)player).setHealth(20);
		com.baseball435.spells.PlayerInfo pi = com.baseball435.spells.PlayerManager.getPlayerInfo(player);
		if (pi != null)
			pi.setMana(100);
	}

	@Override
	public void onPlayerRespawn(Player player) {
		giveSpellItems(player, false);
		PlayerManager.getPlayerInfo(player).getTeam().setHelmet(player);
	}
	

	@Override
	public void checkScoreObjective() {
		if (team1.getScore() >= scoreObjective || team2.getScore() >= scoreObjective)
			MinigameManager.endMinigame(this);
	}

}
