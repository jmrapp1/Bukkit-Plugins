package com.jmr.cmanager;

import org.bukkit.entity.Player;

import com.jmr.pmanager.PlayerInformation;
import com.jmr.pmanager.PlayerManager;

public class CoinManager {

	private static final CoinManager instance = new CoinManager();
	
	private CoinManager() {
	}
	
	public int getCoins(Player player) {
		return PlayerManager.getInstance().getInfo(player).getCoins();
	}
	
	public void subtractCoins(Player player, int amount) {
		PlayerInformation info = PlayerManager.getInstance().getInfo(player);
		int coins = info.getCoins();
		coins -= amount;
		if (coins < 0)
			coins = 0;
		info.setCoins(coins);
		PlayerManager.getInstance().savePlayer(player);
	}
	
	public void addCoins(Player player, int amount) {
		PlayerInformation info = PlayerManager.getInstance().getInfo(player);
		info.setCoins(info.getCoins() + amount);
		PlayerManager.getInstance().savePlayer(player);
	}
	
	public static CoinManager getInstance() {
		return instance;
	}
	
}
