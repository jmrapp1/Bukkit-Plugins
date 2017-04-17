package com.jmr.pmanager;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

	public static Plugin pluginInstance;
	
	@Override
	public void onEnable() {
		pluginInstance = this;
		getServer().getPluginManager().registerEvents(new PlayerEventManager(), this);
		for (Player player : getServer().getOnlinePlayers()) {
			PlayerManager.getInstance().loadPlayer(player);
		}
	}
	
	@Override
	public void onDisable() {
		for (Player player : getServer().getOnlinePlayers()) {
			PlayerManager.getInstance().savePlayer(player);
		}
	}
	
}
