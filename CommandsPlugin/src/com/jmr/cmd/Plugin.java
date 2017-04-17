package com.jmr.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		CommandManager.getInstance().requestCommand(player, command, label, args);
		return true;
	}
	
	@Override
	public void onEnable() {
	}
	
	@Override
	public void onDisable() {
	}
	
}
