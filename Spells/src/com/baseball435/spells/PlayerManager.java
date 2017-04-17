package com.baseball435.spells;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class PlayerManager {

	private static HashMap<Player, PlayerInfo> playersInfo = new HashMap<Player, PlayerInfo>();
	
	public static void addPlayerInfo(Player player) {
		playersInfo.put(player, new PlayerInfo(DatabaseManager.loadPlayerClass(player), 100));
	}
	
	public static void removePlayerInfo(Player player) {
		playersInfo.remove(player);
	}
	
	public static PlayerInfo getPlayerInfo(Player player) {
		return playersInfo.get(player);
	}
	
	public static void addMana(Player player, int amount) {
		PlayerInfo pi = getPlayerInfo(player);
		if (pi != null) {
			pi.addMana(amount);
		}
	}
	
	public static void subtractMana(Player player, int amount) {
		addMana(player, -amount);
	}
	
	public static void setMana(Player player, int amount) {
		PlayerInfo pi = getPlayerInfo(player);
		if (pi != null) {
			pi.setMana(amount);
		}
	}
	
	public static int getMana(Player player) {
		PlayerInfo pi = getPlayerInfo(player);
		if (pi != null) {
			return pi.getMana();
		}
		return -1;
	}
	
	public static void setPlayerClass(Player player, String className) {
		PlayerInfo pi = getPlayerInfo(player);
		if (pi != null) {
			pi.setPlayerClass(className);
		}
	}
	
	public static String getPlayerClass(Player player) {
		PlayerInfo pi = getPlayerInfo(player);
		if (pi != null) {
			return pi.getPlayerClass();
		}
		return null;
	}
	
	public static void resetScoreboard(Player player) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		Objective objective = board.registerNewObjective("test", "dummy");
		objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		objective.setDisplayName("Information");
		Score score = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Class: " + PlayerManager.getPlayerClass(player)));
		score.setScore(0);
		Score score2 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.AQUA + "Mana: ")); 
		score2.setScore(PlayerManager.getMana(player));
		player.setScoreboard(board);
	}
	
	@SuppressWarnings("rawtypes")
	public static void updateManas() {
		Iterator it = playersInfo.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pairs = (Map.Entry)it.next();
	        PlayerInfo pc = (PlayerInfo) pairs.getValue();
	        if (pc.getMana() < 100) {
	        	pc.addMana(15);
	        	if (pc.getMana() > 100)
	        		pc.setMana(100);
	        	resetScoreboard((Player)pairs.getKey());
	        }
	    }
	}
	
}
