package com.jmr.region;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class RegionEventManager implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();

		for (AbstractRegion r : RegionManager.getInstance().getRegions()) {
			if (r.isPlayerInside(player)) {
				if (!r.insideRegion(event.getTo())) {
					if (!r.allowsLeaving()) {
						event.setCancelled(true);
					} else {
						r.removePlayer(player);
						r.onLeave(player);
					}
				}
			} else {
				if (r.insideRegion(event.getTo())) {
					r.addPlayer(player);
					r.onEnter(player);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		for (AbstractRegion r : RegionManager.getInstance().getRegions()) {
			if (r.isPlayerInside(player)) {
				if (!r.insideRegion(event.getTo())) {
					r.removePlayer(player);
					r.onLeave(player);
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void playerTakesDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getCause() == DamageCause.FALL) {
				event.setCancelled(true);
			} else if (event.getCause() == DamageCause.ENTITY_ATTACK) {
				for (AbstractRegion r : RegionManager.getInstance().getRegionsInside((Player) event.getEntity())) {
					if (!r.allowsPvp()) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onEntityExplosion(EntityExplodeEvent event) {
		for (AbstractRegion r : RegionManager.getInstance().getRegions()) {
			if (r.insideRegion(event.getEntity().getLocation())) {
				if (!r.allowsBlockBreaking())
					event.setCancelled(true);
				else if (r.savesBlocks()) {
					for (Block b : event.blockList()) {
						r.addBlock(b.getLocation(), b.getType(), b.getData());
					}
				}
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event) {
		for (AbstractRegion r : RegionManager.getInstance().getRegions()) {
			if (r.insideRegion(event.getBlock().getLocation())) {
				if (!r.allowsBlockBreaking())
					event.setCancelled(true);
				else if (r.savesBlocks())
					r.addBlock(event.getBlock().getLocation(), event.getBlock().getType(), event.getBlock().getData());
				r.onBlockBreak(event);
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event) {
		for (AbstractRegion r : RegionManager.getInstance().getRegions()) {
			if (r.insideRegion(event.getBlock().getLocation())) {
				if (!r.allowsBlockPlacing())
					event.setCancelled(true);
				else if (r.savesBlocks())
					r.addBlock(event.getBlock().getLocation(), Material.AIR, (byte)-1);
				r.onBlockPlace(event);
				return;
			}
		}
	}

	/*@EventHandler(priority = EventPriority.LOW)
	public void onBlockHit(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		if (player.hasPermission("regions.modify") && event.hasBlock() && event.hasItem()) {
			if (event.getItem().getType() == Material.STICK) {
				Location loc = event.getClickedBlock().getLocation();
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					player.setMetadata("regionLoc1", new FixedMetadataValue(Plugin.pluginInstance, loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));
					player.sendMessage(ChatColor.AQUA + "First region location set to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ").");
				} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
					player.setMetadata("regionLoc2", new FixedMetadataValue(Plugin.pluginInstance, loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ()));
					player.sendMessage(ChatColor.AQUA + "Second region location set to (" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ").");
				}
				event.setCancelled(true);
			}
		}
	}*/
	 
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerDeath(PlayerDeathEvent event) {
		for (AbstractRegion r : RegionManager.getInstance().getRegions()) {
			if (r.insideRegion(event.getEntity().getLocation())) {
				r.onPlayerDeath(event);
				return;
			}
		}
	}
	
}
