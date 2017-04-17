package com.jmr.cmanager;

import java.util.Map;

import net.minecraft.server.v1_8_R3.Tuple;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.jmr.pmanager.PlayerInformation;
import com.jmr.pmanager.PlayerManager;

public class Plugin extends JavaPlugin {
	
	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		addPermission(pm, "money", PermissionDefault.NOT_OP, null, null);
		addPermission(pm, "pay", PermissionDefault.NOT_OP, null, null);
	}
	
	@Override
	public void onDisable() {
	
	}
	
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			PlayerInformation info = PlayerManager.getInstance().getInfo(player);
			if (command.getName().equalsIgnoreCase("money")) {
				player.sendMessage(ChatColor.GREEN + "Money: $" + info.getCoins());
			} else if (command.getName().equalsIgnoreCase("pay")) {
				if (args.length == 2) {
					Tuple<Player, Integer> results = PlayerManager.getInstance().findPlayerAndCount(args[0]);
					if (results.b() == 0) {
						player.sendMessage(ChatColor.RED + "No player with the name \"" + args[0] + "\" found.");
					} else if (results.b() > 1) {
						player.sendMessage(ChatColor.RED + "More than one player was found with the name \"" + args[0] + "\"."); 
					} else if (results.b() == 1) {
						try {
							int amount = Integer.parseInt(args[1]);
							if (amount <= info.getCoins()) {
								CoinManager.getInstance().addCoins(results.a(), amount);
								CoinManager.getInstance().subtractCoins(player, amount);
								player.sendMessage(ChatColor.GREEN + "You have sent $" + amount + " to " + results.a().getDisplayName() + ".");
								results.a().sendMessage(ChatColor.GREEN + player.getDisplayName() + " has sent you $" + amount + ".");
							} else {
								player.sendMessage(ChatColor.RED + "You don't have that much money to send.");
							}
						} catch (Exception e) {
							player.sendMessage(ChatColor.RED + "Please enter an amount to pay the player.");
						}
					}
				} else {
					player.sendMessage(ChatColor.RED + "Please use the command the following way: /pay <player> <amount>");
				}
			}
		}
		return true;
	}
	
	private void addPermission(PluginManager pm, String perm, PermissionDefault permDefault, Map<String,Boolean> children, String description) {
		if (pm.getPermission("money." + perm) == null) {
			if (description == null) {
				pm.addPermission(new Permission("magicspells." + perm, permDefault, children));
			} else {
				pm.addPermission(new Permission("magicspells." + perm, description, permDefault, children));
			}
		}
	}
	
}
