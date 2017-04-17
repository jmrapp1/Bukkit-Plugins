package com.jmr.region.regions;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.jmr.region.AbstractRegion;
import com.jmr.region.Plugin;

public class WelcomeRegion extends AbstractRegion {

	private String enter, leave;
	private boolean broadcast;
	
	public WelcomeRegion(String name, String displayName, String enter, String leave, boolean broadcast, double startX, double startY, double startZ, double endX, double endY, double endZ, boolean allowPvp, boolean allowBlockBreaking, boolean allowBlockPlacing, boolean allowLeaving, boolean saveBlocks, boolean ignoreY) {
		super(name, displayName, "welcome", startX, startY, startZ, endX, endY, endZ, allowPvp, allowBlockBreaking, allowBlockPlacing, allowLeaving, saveBlocks, ignoreY);
		this.enter = enter;
		this.leave = leave;
		this.broadcast = broadcast;
	}

	@Override
	public void onEnter(Player player) {
		if (broadcast) 
			Plugin.pluginInstance.getServer().broadcastMessage(enter);
		else
			player.sendMessage(enter);
	}

	@Override
	public void onLeave(Player player) {
		if (broadcast) 
			Plugin.pluginInstance.getServer().broadcastMessage(leave);
		else
			player.sendMessage(leave);
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
