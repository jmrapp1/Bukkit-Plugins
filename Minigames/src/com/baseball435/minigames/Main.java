package com.baseball435.minigames;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.baseball435.minigames.minigame.Minigame;
import com.baseball435.minigames.player.PlayerInfo;

public class Main extends JavaPlugin {

	public static Logger LOG;
	private static World WORLD;
	private static Server SERVER;
	private static Plugin PLUGIN;
	public static Economy ECON;
	
	@Override
	public void onEnable() {
		LOG = getLogger();
		LOG.info("Baseball435's Minigames enabled.");
		getServer().getPluginManager().registerEvents(new EventListener(), this);
		
		if (!setupEconomy() ) {
		    LOG.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
		    getServer().getPluginManager().disablePlugin(this);
		    return;
		}
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    public void run() {
		    	MinigameManager.updateMinigames();
		    }
		}, 0, 20);
		WORLD = getServer().getWorlds().get(0);
		SERVER = getServer();
		PLUGIN = this;
	}
	
	@Override
	public void onDisable() {
		for (Minigame mg : MinigameManager.currentMinigames)
			MinigameManager.endMinigame(mg);
		LOG.info("Baseball435's Minigames disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			//int argCount = args.length;
			if (cmd.getName().equalsIgnoreCase("leave")) {
				PlayerInfo pi = PlayerManager.getPlayerInfo(player);
				if (pi.inMinigame()) {
					pi.getTeam().getMinigame().removePlayer(player, true);
					pi.setTeam(null);
					player.sendMessage(ChatColor.GREEN + "You left the minigame.");
				} else {
					player.sendMessage(ChatColor.RED + "You are not in a minigame.");
				}
			}
		}
		return false;
	}
	
	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
		    return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
		    return false;
		}
		ECON = rsp.getProvider();
		return ECON != null;
	}
	
	public static World getWorld() {
		return WORLD;
	}
	
	public static Server getBukkitServer() {
		return SERVER;
	}
	
	public static Plugin getBukkitPlugin() {
		return PLUGIN;
	}
	
}
