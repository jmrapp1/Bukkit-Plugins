package com.jmr.cmd;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public abstract class PlayerCommand {

	private String[] names;
	private String permissionNode;
	
	public PlayerCommand(String[] names, String permissionNode) {
		this.names = names;
		this.permissionNode = permissionNode;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public boolean hasName(String name) {
		for (String s : names)
			if (s.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public String getPermissionNode() {
		return permissionNode;
	}
	
	public abstract void onCommand(Player player, Command command, String[] args);
	
}
