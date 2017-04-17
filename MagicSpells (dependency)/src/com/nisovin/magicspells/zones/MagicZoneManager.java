package com.nisovin.magicspells.zones;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;
import com.nisovin.magicspells.util.MagicConfig;
import com.nisovin.magicspells.zones.MagicZone.ZoneCheckResult;

public class MagicZoneManager {
	
	private Map<String, Class<? extends MagicZone>> zoneTypes;
	private Map<String, MagicZone> zones;
	private Set<MagicZone> zonesOrdered;

	public MagicZoneManager() {
		// create zone types
		zoneTypes = new HashMap<String, Class<? extends MagicZone>>();
		zoneTypes.put("cuboid", MagicZoneCuboid.class);
		zoneTypes.put("worldguard", MagicZoneWorldGuard.class);
		zoneTypes.put("residence", MagicZoneResidence.class);
		zoneTypes.put("nomagic", MagicNoDamageZone.class);
	}
	
	public void load(MagicConfig config) {
		// get zones
		zones = new HashMap<String, MagicZone>();
		zonesOrdered = new TreeSet<MagicZone>();
		
		Set<String> zoneNodes = config.getKeys("magic-zones");
		if (zoneNodes != null) {
			for (String node : zoneNodes) {
				ConfigurationSection zoneConfig = config.getSection("magic-zones." + node);
				
				// check enabled
				if (!zoneConfig.getBoolean("enabled", true)) {
					continue;
				}
				
				// get zone type
				String type = zoneConfig.getString("type", "");
				if (type.isEmpty()) {
					MagicSpells.error("Invalid magic zone type '" + type + "' on zone '" + node + "'");
					continue;
				}
				Class<? extends MagicZone> clazz = zoneTypes.get(type);
				if (clazz == null) {
					MagicSpells.error("Invalid magic zone type '" + type + "' on zone '" + node + "'");
					continue;
				}
				
				// create zone
				MagicZone zone;
				try {
					zone = clazz.newInstance();
				} catch (Exception e) {
					MagicSpells.error("Failed to create magic zone '" + node + "'");
					e.printStackTrace();
					continue;
				}
				zone.create(node, zoneConfig);
				zones.put(node, zone);
				zonesOrdered.add(zone);
				MagicSpells.debug(3, "Loaded magic zone: " + node);
			}
		}
		
		MagicSpells.debug(1, "Magic zones loaded: " + zones.size());
	}
	
	public boolean willFizzle(Player player, Spell spell) {
		return willFizzle(player.getLocation(), spell);
	}
	
	public boolean inNoDamageZone(Location location) {
		for (MagicZone zone : zonesOrdered)
			if (zone instanceof MagicNoDamageZone) 
				if (zone.inZone(location))
					return true;
		return false;
	}
	
	public boolean inNoDamageZone(List<Block> blocks) {
		for (Block b : blocks)
			if (inNoDamageZone(b.getLocation()))
				return true;
		return false;
	}
	
	public boolean willFizzle(Location location, Spell spell) {
		for (MagicZone zone : zonesOrdered) {
			ZoneCheckResult result = zone.check(location, spell);
			if (result == ZoneCheckResult.DENY) {
				return true;
			} else if (result == ZoneCheckResult.ALLOW) {
				return false;
			}
		}
		return false;
	}
	
	public boolean inZone(Player player, String zoneName) {
		return inZone(player.getLocation(), zoneName);
	}
	
	public boolean inZone(Location loc, String zoneName) {
		MagicZone zone = zones.get(zoneName);
		if (zone != null && zone.inZone(loc)) {
			return true;
		}
		return false;
	}
	
	public void sendNoMagicMessage(Player player, Spell spell) {
		for (MagicZone zone : zonesOrdered) {
			ZoneCheckResult result = zone.check(player.getLocation(), spell);
			if (result == ZoneCheckResult.DENY) {
				MagicSpells.sendMessage(player, zone.getMessage());
				return;
			}
		}
	}
	
	public MagicZone getZone(String name) {
		return zones.get(name);
	}
	
	public int zoneCount() {
		return zones.size();
	}
	
	public void addZoneType(String name, Class<? extends MagicZone> clazz) {
		zoneTypes.put(name, clazz);
	}
	
	public void turnOff() {
		zoneTypes.clear();
		zones.clear();
		zoneTypes = null;
		zones = null;
	}
	
}
