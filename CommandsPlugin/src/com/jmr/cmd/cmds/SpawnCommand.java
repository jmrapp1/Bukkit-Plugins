package com.jmr.cmd.cmds;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jmr.cmd.PlayerCommand;

public class SpawnCommand extends PlayerCommand {

	public SpawnCommand() {
		super(new String[] { "spawn" }, "player.spawn");
	}

	@Override
	public void onCommand(Player player, Command command, String[] args) {
		teleportToSpawn(player);
		player.sendMessage(ChatColor.RED + "You have been teleported to the spawn.");
	}
	
	public static void teleportToSpawn(Player player) {
		player.teleport(new Location(player.getWorld(), 183, 105, 607));
	}

}
