package com.baseball435.minigames;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.baseball435.minigames.player.PlayerInfo;

public class PlayerManager {

	private static final HashMap<Player, PlayerInfo> playersInfo = new HashMap<Player, PlayerInfo>();
	
	public static void addPlayerInfo(Player player) {
		playersInfo.put(player, new PlayerInfo());
	}
	
	public static void removePlayerInfo(Player player) {
		//if in minigame, move to quit position then save
		PlayerInfo pi = getPlayerInfo(player);
		if (getPlayerInfo(player).inMinigame()) {
			pi.getTeam().getMinigame().removePlayer(player, true);
			pi.getTeam().moveToQuit(player);
		}
		player.saveData();
		playersInfo.remove(player);
	}
	
	public static PlayerInfo getPlayerInfo(Player player) {
		return playersInfo.get(player);
	}
	
}
