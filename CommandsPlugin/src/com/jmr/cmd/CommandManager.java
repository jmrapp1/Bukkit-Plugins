package com.jmr.cmd;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.jmr.cmanager.cmds.MoneyCommand;
import com.jmr.cmd.cmds.SpawnCommand;
import com.jmr.pmanager.PlayerManager;
import com.jmr.pmanager.PlayerRank;

public class CommandManager {

	private static final CommandManager instance = new CommandManager();
	private ArrayList<PlayerCommand> commands = new ArrayList<PlayerCommand>();
	
	private CommandManager() {
		commands.add(new SpawnCommand());
	}
	
	public boolean requestCommand(Player player, Command command, String label, String[] args) {
		PlayerRank rank = PlayerManager.getInstance().getRank(player);
		PlayerCommand cmd = getCommand(label);
		if (cmd != null) {
			if (rank.hasPermission(cmd.getPermissionNode())) {
				cmd.onCommand(player, command, args);
			} else {
				player.sendMessage(ChatColor.RED + "You do not have the required permissions to run that command.");
			}
			return true;
		}
		return false;
	}
	
	public void addCommand(PlayerCommand cmd) {
		commands.add(cmd);
	}
	
	public static CommandManager getInstance() {
		return instance;
	}
	
	private PlayerCommand getCommand(String label) {
		for (PlayerCommand pc : commands)
			if (pc.hasName(label))
				return pc;
		return null;
	}
	
}
