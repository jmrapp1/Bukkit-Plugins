package com.jmr.region;

import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;


public class Plugin extends JavaPlugin {

	public static Plugin pluginInstance;
	
	@Override
	public void onEnable() {
		pluginInstance = this;
		RegionManager.getInstance().loadRegions();
		PluginManager pm = getServer().getPluginManager();
		
		addPermission(pm, "list", PermissionDefault.FALSE, null, null);
		addPermission(pm, "setname", PermissionDefault.FALSE, null, null);
		addPermission(pm, "delete", PermissionDefault.FALSE, null, null);
		addPermission(pm, "settype", PermissionDefault.FALSE, null, null);
		addPermission(pm, "setvars", PermissionDefault.FALSE, null, null);
		addPermission(pm, "create", PermissionDefault.FALSE, null, null);
		addPermission(pm, "reload", PermissionDefault.FALSE, null, null);
		
		getServer().getPluginManager().registerEvents(new RegionEventManager(), this);
	}

	@Override
	public void onDisable() {
	}
	
	private void addPermission(PluginManager pm, String perm, PermissionDefault permDefault, Map<String,Boolean> children, String description) {
		if (pm.getPermission("regions." + perm) == null) {
			if (description == null) {
				pm.addPermission(new Permission("regions." + perm, permDefault, children));
			} else {
				pm.addPermission(new Permission("regions." + perm, description, permDefault, children));
			}
		}
	}
	
	public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("regions") || command.getName().equalsIgnoreCase("r")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (args != null) {
					if (args.length == 1) {
						if (args[0].equalsIgnoreCase("list") && player.hasPermission("regions.list")) {
							String regions = "Regions: ";
							for (int i = 0; i < RegionManager.getInstance().getRegions().size(); i++) {
								regions += RegionManager.getInstance().getRegions().get(i).getName();
								if (i < RegionManager.getInstance().getRegions().size() - 1)
									regions += ", ";
							}
							player.sendMessage(ChatColor.AQUA + regions);
						} else if (args[0].equalsIgnoreCase("reload") && player.hasPermission("regions.reload")) {
							RegionManager.getInstance().loadRegions();
							player.sendMessage(ChatColor.GREEN + "Regions reloaded successfully.");
						} else {
							player.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
						}
					}
					if (args.length == 2) {
						if (args[0].equalsIgnoreCase("name")) {
							if (player.hasPermission("regions.setname")) {
								String name = args[1];
								player.setMetadata("regionName", new FixedMetadataValue(Plugin.pluginInstance, name));
								player.sendMessage(ChatColor.AQUA + "Region name set to \"" + name + "\".");
							} else {
								player.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
							}
						} else if (args[0].equalsIgnoreCase("delete")) {
							if (player.hasPermission("regions.delete")) {
								String name = args[1];
								if (RegionManager.getInstance().deleteRegion(name)) {
									player.sendMessage(ChatColor.AQUA + "The region \"" + name + "\" was deleted.");
								} else {
									player.sendMessage(ChatColor.RED + "The region by the name \"" + name + "\" doesn't exist.");
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
							}
						} else if (args[0].equalsIgnoreCase("type")) {
							if (player.hasPermission("regions.settype")) {
								String type = args[1];
								player.setMetadata("regionType", new FixedMetadataValue(Plugin.pluginInstance, type));
								player.sendMessage(ChatColor.AQUA + "Region type set to \"" + type + "\".");
							} else {
								player.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
							}
						}
					} else if (args.length == 4) {
						if (args[0].equalsIgnoreCase("set") && player.hasPermission("regions.setvars")) {
							String regionName = args[1];
							AbstractRegion region = RegionManager.getInstance().getRegion(regionName);
							if (region != null) {
								String var = args[2];
								if (var.equalsIgnoreCase("pvp")) {
									String val = args[3];
									if (val.equalsIgnoreCase("true")) {
										region.setAllowPvp(true);
										RegionManager.getInstance().saveRegions();
										player.sendMessage(ChatColor.AQUA + "Pvp has been enabled in the region \"" + regionName + "\".");
									} else if (val.equalsIgnoreCase("false")) {
										region.setAllowPvp(false);
										player.sendMessage(ChatColor.AQUA + "Pvp has been disabled in the region \"" + regionName + "\".");
										RegionManager.getInstance().saveRegions();
									} else {
										player.sendMessage(ChatColor.RED + "Please specify whether to allow pvp or not (true/false).");
									}
								} else if (var.equalsIgnoreCase("breakblocks")) {
									String val = args[3];
									if (val.equalsIgnoreCase("true")) {
										region.setAllowBlockBreaking(true);
										RegionManager.getInstance().saveRegions();
										player.sendMessage(ChatColor.AQUA + "Block breaking has been enabled in the region \"" + regionName + "\".");
									} else if (val.equalsIgnoreCase("false")) {
										region.setAllowBlockBreaking(false);
										player.sendMessage(ChatColor.AQUA + "Block breaking has been disabled in the region \"" + regionName + "\".");
										RegionManager.getInstance().saveRegions();
									} else {
										player.sendMessage(ChatColor.RED + "Please specify whether to allow block breaking or not (true/false).");
									}
								} else if (var.equalsIgnoreCase("placeblocks")) {
									String val = args[3];
									if (val.equalsIgnoreCase("true")) {
										region.setAllowBlockPlacing(true);
										RegionManager.getInstance().saveRegions();
										player.sendMessage(ChatColor.AQUA + "Block placing has been enabled in the region \"" + regionName + "\".");
									} else if (val.equalsIgnoreCase("false")) {
										region.setAllowBlockPlacing(false);
										player.sendMessage(ChatColor.AQUA + "Block placing has been disabled in the region \"" + regionName + "\".");
										RegionManager.getInstance().saveRegions();
									} else {
										player.sendMessage(ChatColor.RED + "Please specify whether to allow block placing or not (true/false).");
									}
								} else if (var.equalsIgnoreCase("leaving")) {
									String val = args[3];
									if (val.equalsIgnoreCase("true")) {
										region.setAllowLeaving(true);
										RegionManager.getInstance().saveRegions();
										player.sendMessage(ChatColor.AQUA + "Leaving the region has been enabled in the region \"" + regionName + "\".");
									} else if (val.equalsIgnoreCase("false")) {
										region.setAllowLeaving(false);
										player.sendMessage(ChatColor.AQUA + "Leaving the region has been disabled in the region \"" + regionName + "\".");
										RegionManager.getInstance().saveRegions();
									} else {
										player.sendMessage(ChatColor.RED + "Please specify whether to allow leaving or not (true/false).");
									}
								} else if (var.equalsIgnoreCase("saveBlocks")) {
									String val = args[3];
									if (val.equalsIgnoreCase("true")) {
										region.setSaveBlocks(true);
										RegionManager.getInstance().saveRegions();
										player.sendMessage(ChatColor.AQUA + "Saving blocks has been enabled in the region \"" + regionName + "\".");
									} else if (val.equalsIgnoreCase("false")) {
										region.setSaveBlocks(false);
										player.sendMessage(ChatColor.AQUA + "Saving blocks has been disabled in the region \"" + regionName + "\".");
										RegionManager.getInstance().saveRegions();
									} else {
										player.sendMessage(ChatColor.RED + "Please specify whether to allow saving blocks or not (true/false).");
									}
								} else if (var.equalsIgnoreCase("ignoreY")) {
									String val = args[3];
									if (val.equalsIgnoreCase("true")) {
										region.setIgnoresY(true);
										RegionManager.getInstance().saveRegions();
										player.sendMessage(ChatColor.AQUA + "Ignoring Y has been enabled in the region \"" + regionName + "\".");
									} else if (val.equalsIgnoreCase("false")) {
										region.setIgnoresY(false);
										player.sendMessage(ChatColor.AQUA + "Ignoring Y has been disabled in the region \"" + regionName + "\".");
										RegionManager.getInstance().saveRegions();
									} else {
										player.sendMessage(ChatColor.RED + "Please specify whether to allow ignoring Y or not (true/false).");
									}
								}
							} else {
								player.sendMessage(ChatColor.DARK_RED + "You don't have permission to use this command.");
							}
						}
					} else if (args.length == 1) {
						if (args[0].equalsIgnoreCase("create")) {
							if (player.hasPermission("regions.create")) {
								//RegionManager.getInstance().createRegion(player);
							} else {
								player.sendMessage(ChatColor.RED + "Please specify whether to allow ignoring Y or not (true/false).");
							}
						} else if (args[0].equalsIgnoreCase("delete")) {
							if (player.hasPermission("regions.delete")) {
								player.sendMessage(ChatColor.RED + "Please enter a region name to delete.");
							} else {
								player.sendMessage(ChatColor.RED + "Please specify whether to allow ignoring Y or not (true/false).");
							}
						} else if (args[0].equalsIgnoreCase("type")) {
							if (player.hasPermission("regions.settype")) {
								player.sendMessage(ChatColor.RED + "Please enter the type of region.");
							} else {
								player.sendMessage(ChatColor.RED + "Please specify whether to allow ignoring Y or not (true/false).");
							}
						}
					}
				}
			}
			return true;
		}
		return false;
	}
	
}
