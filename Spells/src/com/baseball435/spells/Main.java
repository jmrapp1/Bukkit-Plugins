package com.baseball435.spells;

import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;


public class Main extends JavaPlugin {

	public static Logger LOG;
	public static Economy ECON;
	public static Plugin PLUGIN;
	
	@Override
	public void onEnable() {
		PLUGIN = this;
		LOG = getLogger();
		LOG.info("Spells enabled.");
		ClassManager.registerClasses();
		ClassManager.registerSpells();
		getServer().getPluginManager().registerEvents(new EventListener(), this);

		if (!setupEconomy() ) {
            LOG.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
		    public void run() {
		       PlayerManager.updateManas();
		    }
		}, 0, 80);
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
	
	@Override
	public void onDisable() {
		LOG.info("Spells disabled.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			int argCount = args.length;
			if (cmd.getName().equalsIgnoreCase("spells")) {
				if (argCount == 1 && args[0].equalsIgnoreCase("owned")) {
					if (PlayerManager.getPlayerInfo(player).getOwnedSpells().isEmpty()) {
						player.sendMessage(ChatColor.AQUA + "You don't own any spells.");
					} else {
						String all = "";
						for (int i = 0; i < PlayerManager.getPlayerInfo(player).getOwnedSpells().size(); i++) {
							if (i == 0)
								all += String.valueOf(PlayerManager.getPlayerInfo(player).getOwnedSpells().get(i));
							else if (i == PlayerManager.getPlayerInfo(player).getOwnedSpells().size() - 1)
								all += ", and " + PlayerManager.getPlayerInfo(player).getOwnedSpells().get(i);
							else
								all += ", " + PlayerManager.getPlayerInfo(player).getOwnedSpells().get(i);
						}
							
						player.sendMessage(ChatColor.AQUA + "You own these spells: " + all);
					}
					return true;
				} else {
					player.sendMessage(ChatColor.AQUA + "Use one of these commands: /spells owned");
					return true;
				}
			} else if (cmd.getName().equalsIgnoreCase("class")) {
				if (argCount >= 1 && args[0].equalsIgnoreCase("list")) {
					String all = "";
					for (int i = 0; i < ClassManager.classNames.length; i++) {
						if (i != ClassManager.classNames.length - 1)
							all += ClassManager.classNames[i] + ", ";
						else
							all += "and " + ClassManager.classNames[i] + ".";
					}
					player.sendMessage(ChatColor.AQUA + "Classes: " + all);
				} else if (argCount == 2 && args[0].equalsIgnoreCase("set")){
					String className = args[1];
					if (ClassManager.classExists(className)) {
						String first = className.trim().substring(0, 1).toUpperCase();
						String end = className.trim().substring(1);
						PlayerManager.setPlayerClass(player, first.concat(end));
						DatabaseManager.savePlayerClass(player);
						PlayerManager.resetScoreboard(player);
						player.sendMessage(ChatColor.AQUA + "Your class was changed.");
					} else {
						player.sendMessage(ChatColor.AQUA + "Please enter an existing class.");
						return true;
					}
				} else {
					player.sendMessage(ChatColor.AQUA + "Use one of these commands: /class list, /class set <name>");
					return true;
				}
			}
		}
		return false;
	}
	
}
