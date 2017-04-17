package com.baseball435.minigames.minigame;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.baseball435.minigames.PlayerManager;

public abstract class TeamMinigame extends Minigame {

	protected final Team team1, team2;
	
	public TeamMinigame(String name, long timeLimit, int scoreObjective, Location quitLocation, Location area1, Location area2, Location start1, Location start2, int maxPlayerPerTeam) {
		super(name, timeLimit, scoreObjective, quitLocation, area1, area2);
		team1 = new Team(this, DyeColor.BLUE, "Blue", start1, quitLocation, maxPlayerPerTeam, false, 2);
		team2 = new Team(this, DyeColor.RED, "Red", start2, quitLocation, maxPlayerPerTeam, false, 2);
	}

	public boolean readyToStart() {
		return team1.readyToStart() && team2.readyToStart();
	}
	
	@Override
	public void addPlayer(Player player) {
		if (!team1.isFull() && !team2.isFull()) {
			if (!PlayerManager.getPlayerInfo(player).inMinigame()) {
				if (team1.getSize() < team2.getSize()) {
					team1.addPlayer(player);
					onAddPlayer(player);
				} else {
					team2.addPlayer(player);
					onAddPlayer(player);
				}
			} else {
				player.kickPlayer("Kicked to reset minigame information.");
			}
		} else {
			player.sendMessage(ChatColor.RED + "Both teams are full! Wait till next round.");
		}
	}
	
	@Override
	public void removePlayer(Player player, boolean noPlayers) {
		if (team1.removePlayer(player)) {
			player.teleport(team1.getEndLocation());
			onRemovePlayer(player);
			if (noPlayers)
				checkNoPlayers();
		} else if (team2.removePlayer(player)) {
			player.teleport(team2.getEndLocation());
			onRemovePlayer(player);
			if (noPlayers)
				checkNoPlayers();
		}
	}
	
}
