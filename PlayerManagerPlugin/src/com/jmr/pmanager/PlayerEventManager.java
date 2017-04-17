package com.jmr.pmanager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventManager implements Listener {

	@EventHandler
	public void onLogin(PlayerLoginEvent event) {
		PlayerManager.getInstance().loadPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		PlayerManager.getInstance().reloadBackupInventory(event.getPlayer());
		PlayerManager.getInstance().savePlayer(event.getPlayer());
	}
	
}
