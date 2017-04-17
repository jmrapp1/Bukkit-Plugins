package com.jmr.region;

import org.bukkit.Location;
import org.bukkit.Material;

public class SavedBlock {
	
	public Location loc;
	public Material material;
	public byte data;

	public SavedBlock(Location loc, Material material, byte data) {
		this.loc = loc;
		this.material = material;
		this.data = data;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public byte getData() {
		return data;
	}
	
}
