package com.jmr.region;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class AbstractRegion {

	protected String name, displayName;
	protected String type;
	protected final double startX, startY, startZ, endX, endY, endZ;
	protected HashMap<String, Player> playersInside = new HashMap<String, Player>();
	protected boolean allowPvp, allowBlockBreaking, allowBlockPlacing, allowLeaving, saveBlocks, ignoreY;
	protected ArrayList<SavedBlock> savedBlocks = new ArrayList<SavedBlock>();
	
	public AbstractRegion(String name, String displayName, String type, double startX, double startY, double startZ, double endX, double endY, double endZ, boolean allowPvp, boolean allowBlockBreaking, boolean allowBlockPlacing, boolean allowLeaving, boolean saveBlocks, boolean ignoreY) {
		this.name = name;
		this.displayName = displayName;
		this.type = type;
		this.startX = Math.min(startX, endX);
		this.startY = Math.min(startY, endY);
		this.startZ = Math.min(startZ, endZ);
		this.endX = Math.max(endX, startX);
		this.endY = Math.max(endY, startY);
		this.endZ = Math.max(endZ, startZ);
		this.allowPvp = allowPvp;
		this.allowBlockBreaking = allowBlockBreaking;
		this.allowBlockPlacing = allowBlockPlacing;
		this.allowLeaving = allowLeaving;
		this.saveBlocks = saveBlocks;
		this.ignoreY = ignoreY;
	}
	
	public abstract void onEnter(Player player);
	
	public abstract void onLeave(Player player);
	
	public abstract void onPlayerDeath(PlayerDeathEvent event);
	
	public abstract void onBlockBreak(BlockBreakEvent event);
	
	public abstract void onBlockPlace(BlockPlaceEvent event);
	
	public abstract void onPlayerDamage(EntityDamageEvent event);
	
	public void addBlock(Location loc, Material mat, byte data) {
		if (!blockIsSaved(loc)) {
			savedBlocks.add(new SavedBlock(loc, mat, data));
		}
	}
	
	public void replaceSavedBlocks() {
		for (int i = 0; i < savedBlocks.size(); i++) {
			SavedBlock b = savedBlocks.get(i);
			b.getLocation().getWorld().getBlockAt(b.getLocation()).setType(b.getMaterial());
			if (b.getData() != -1)
				b.getLocation().getWorld().getBlockAt(b.getLocation()).setData(b.getData());
		}
		savedBlocks.clear();
	}
	
	private boolean blockIsSaved(Location loc) {
		for (SavedBlock b : savedBlocks)
			if (b.getLocation().getBlockX() == loc.getBlockX() && b.getLocation().getBlockY() == loc.getBlockY() && b.getLocation().getBlockZ() == loc.getBlockZ())
				return true;
		return false;
	}
	
	public boolean allowsPvp() {
		return allowPvp;
	}
	
	public void setAllowPvp(boolean b) {
		allowPvp = b;
	}
	
	public boolean ignoresY() {
		return ignoreY;
	}
	
	public void setIgnoresY(boolean b) {
		ignoreY = b;
	}
	
	public boolean savesBlocks() {
		return saveBlocks;
	}
	
	public void setSaveBlocks(boolean b) {
		saveBlocks = b;
	}
	
	public boolean allowsLeaving() {
		return allowLeaving;
	}
	
	public void setAllowLeaving(boolean b) {
		allowLeaving = b;
	}
	
	public boolean allowsBlockBreaking() {
		return allowBlockBreaking;
	}
	
	public void setAllowBlockBreaking(boolean b) {
		allowBlockBreaking = b;
	}
	
	public boolean allowsBlockPlacing() {
		return allowBlockPlacing;
	}
	
	public void setAllowBlockPlacing(boolean b) {
		allowBlockPlacing = b;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public String getType() {
		return type;
	}
	
	public double getStartX() {
		return startX;
	}

	public double getStartY() {
		return startY;
	}
	
	public double getStartZ() {
		return startZ;
	}
	
	public double getEndX() {
		return endX;
	}
	
	public double getEndY() {
		return endY;
	}
	
	public double getEndZ() {
		return endZ;
	}
	
	public boolean insideRegion(Location loc) {
		if (!ignoreY)
			return loc.getBlockX() <= endX && loc.getBlockX() >= startX && loc.getBlockY() <= endY && loc.getBlockY() >= startY && loc.getBlockZ() <= endZ && loc.getBlockZ() >= startZ;
		else
			return loc.getBlockX() <= endX && loc.getBlockX() >= startX && loc.getBlockZ() <= endZ && loc.getBlockZ() >= startZ;	
	}
	
	public boolean isPlayerInside(Player player) {
		return playersInside.containsKey(player.getName());
	}
	
	public void addPlayer(Player player) {
		playersInside.put(player.getName(), player);
	}
	
	public void removePlayer(Player player) {
		playersInside.remove(player.getName());
	}
	
}
