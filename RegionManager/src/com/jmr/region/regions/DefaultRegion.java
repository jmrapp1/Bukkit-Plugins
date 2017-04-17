package com.jmr.region.regions;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.jmr.region.AbstractRegion;

public class DefaultRegion extends AbstractRegion {

	public DefaultRegion(String name, String displayName, double startX, double startY, double startZ, double endX, double endY, double endZ, boolean allowPvp, boolean allowBlockBreaking, boolean allowBlockPlacing, boolean allowLeaving, boolean saveBlocks, boolean ignoreY) {
		super(name, displayName, "default", startX, startY, startZ, endX, endY, endZ, allowPvp, allowBlockBreaking, allowBlockPlacing, allowLeaving, saveBlocks, ignoreY);
	}

	@Override
	public void onEnter(Player player) {
		
	}

	@Override
	public void onLeave(Player player) {
		
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event) {
		
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		
	}

	@Override
	public void onPlayerDamage(EntityDamageEvent event) {
		
	}

}
