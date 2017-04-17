package com.jmr.region;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import com.jmr.region.regions.DefaultRegion;
import com.jmr.region.regions.MagicKitRegion;
import com.jmr.region.regions.WelcomeRegion;

public class RegionLoader {

	public ArrayList<AbstractRegion> loadRegions(String file) throws Exception {
		File f = new File(file);
		if (f.exists()) {
			ArrayList<AbstractRegion> regions = new ArrayList<AbstractRegion>();
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			for (String regionName : yml.getKeys(false)) {
				ConfigurationSection section = yml.getConfigurationSection(regionName);
				regions.add(handleRegionsSection(section));
			}
			return regions;
		} else {
			throw new Exception("Region File \"" + file + "\" does not exist.");
		}
	}
	
	private AbstractRegion handleRegionsSection(ConfigurationSection section) throws Exception {
		String type = "", displayName = "";
		Location point1 = null, point2 = null;
		boolean allowPvp = false, allowBlockBreaking = false, allowBlockPlacing = false, allowLeaving = false, saveBlocks = false, ignoreY = false;

		if (section.contains("displayName")) {
			displayName = section.getString("displayName");
		}
		if (section.contains("type")) {
			type = section.getString("type");
		}
		if (section.contains("allowPvp")) {
			allowPvp = section.getBoolean("allowPvp");
		}
		if (section.contains("allowBlockBreaking")) {
			allowBlockBreaking = section.getBoolean("allowBlockBreaking");
		}
		if (section.contains("allowBlockPlacing")) {
			allowBlockPlacing = section.getBoolean("allowBlockPlacing");
		}
		if (section.contains("allowLeaving")) {
			allowLeaving = section.getBoolean("allowLeaving");
		}
		if (section.contains("saveBlocks")) {
			saveBlocks = section.getBoolean("saveBlocks");
		}
		if (section.contains("ignoreY")) {
			ignoreY = section.getBoolean("ignoreY");
		}
		if (section.contains("point1")) {
			point1 = getLocation(section.getString("point1"));
		}
		if (section.contains("point2")) {
			point2 = getLocation(section.getString("point2"));
		}
		
		if (type.equalsIgnoreCase("welcome")) {
			String enterMsg = "", leaveMsg = "";
			boolean broadcast = false;

			if (section.contains("broadcast")) {
				broadcast = section.getBoolean("broadcast");
			}
			if (section.contains("enterMsg")) {
				enterMsg = section.getString("enterMsg");
			}
			if (section.contains("leaveMsg")) {
				leaveMsg = section.getString("leaveMsg");
			}
			return new WelcomeRegion(section.getName(), displayName, enterMsg, leaveMsg, broadcast, point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ(), allowPvp, allowBlockBreaking, allowBlockPlacing, allowLeaving, saveBlocks, ignoreY);
		} else if (type.equalsIgnoreCase("magickitregion")) {
			int killPay = 3;
			if (section.contains("killPay")) {
				killPay = section.getInt("killPay");
				return new MagicKitRegion(section.getName(), displayName, killPay, point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ());
			}
		}
		return new DefaultRegion(section.getName(), displayName, point1.getX(), point1.getY(), point1.getZ(), point2.getX(), point2.getY(), point2.getZ(), allowPvp, allowBlockBreaking, allowBlockPlacing, allowLeaving, saveBlocks, ignoreY);
	}
	
	private Location getLocation(String locStr) {
		String[] coords = locStr.trim().split(",");
		return new Location(Plugin.pluginInstance.getServer().getWorld("world"), Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
	}
	
}
