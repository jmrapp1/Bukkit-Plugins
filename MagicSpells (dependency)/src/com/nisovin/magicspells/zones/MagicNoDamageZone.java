package com.nisovin.magicspells.zones;

import org.bukkit.Location;

import com.nisovin.magicspells.Spell;

public class MagicNoDamageZone extends MagicZoneCuboid {

	public final ZoneCheckResult check(Location location, Spell spell) {
		if (!inZone(location))
			return ZoneCheckResult.IGNORED;
		return ZoneCheckResult.DENY;
	}
	
}
