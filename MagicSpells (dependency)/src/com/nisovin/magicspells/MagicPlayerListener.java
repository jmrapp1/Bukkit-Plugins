package com.nisovin.magicspells;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.jmr.menu.MenuManager;

class MagicPlayerListener implements Listener {
	
	private MagicSpells plugin;
	
	public MagicPlayerListener(MagicSpells plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		// set up spell book
		Spellbook spellbook = new Spellbook(player, plugin);
		plugin.spellbooks.put(player.getName(), spellbook);
		
		// set up mana bar
		if (plugin.mana != null) {
			plugin.mana.createManaBar(player);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent event) {
		Spellbook spellbook = plugin.spellbooks.remove(event.getPlayer().getName());
		if (spellbook != null) {
			spellbook.destroy();
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void playerTakesDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			//if (player is in minigame) {
			if (MagicSpells.getMagicZoneManager() != null) {
				if (MagicSpells.getMagicZoneManager().inNoDamageZone(player.getLocation())) {
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled()) 
			return;
		if (MagicSpells.getMagicZoneManager() != null && MagicSpells.getMagicZoneManager().inNoDamageZone(event.getBlock().getLocation()) && (!event.getPlayer().hasPermission("magicspells.modifynomagiczone") /* && !event.getPlayer().isInMinigame()*/)) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	public void onExplosion(EntityExplodeEvent event) {
		if (event.isCancelled()) 
			return;
		if (MagicSpells.getMagicZoneManager() != null && MagicSpells.getMagicZoneManager().inNoDamageZone(event.blockList())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOW)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled()) 
			return;
		if (MagicSpells.getMagicZoneManager() != null && MagicSpells.getMagicZoneManager().inNoDamageZone(event.getBlock().getLocation()) && (!event.getPlayer().hasPermission("magicspells.modifynomagiczone") /* && !event.getPlayer().isInMinigame()*/)) {
			event.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
		if (plugin.separatePlayerSpellsPerWorld) {
			MagicSpells.debug(2, "Player '" + event.getPlayer().getName() + "' changed from world '" + event.getFrom().getName() + "' to '" + event.getPlayer().getWorld().getName() + "', reloading spells");
			MagicSpells.getSpellbook(event.getPlayer()).reload();
		}
	}
	
}
