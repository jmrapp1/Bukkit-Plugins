package com.jmr.classes;

import java.util.Map;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new MagicClassEventManager(), this);
		addPermission(getServer().getPluginManager(), "magicclassesmenu", PermissionDefault.TRUE, null, null);
		addPermission(getServer().getPluginManager(), "magicshopmenu", PermissionDefault.TRUE, null, null);
		addPermission(getServer().getPluginManager(), "removeSigns", PermissionDefault.FALSE, null, null);
		MagicClassManager.getInstance();
	}
	
	private void addPermission(PluginManager pm, String perm, PermissionDefault permDefault, Map<String,Boolean> children, String description) {
		if (pm.getPermission("classes." + perm) == null) {
			if (description == null) {
				pm.addPermission(new Permission("classes." + perm, permDefault, children));
			} else {
				pm.addPermission(new Permission("classes." + perm, description, permDefault, children));
			}
		}
	}
	
	@Override
	public void onDisable() {
	}
	
}
