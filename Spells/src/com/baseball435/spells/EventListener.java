package com.baseball435.spells;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.BlockIterator;

public class EventListener implements Listener {

	@EventHandler
    public void onLogin(PlayerLoginEvent event) {
		PlayerManager.addPlayerInfo(event.getPlayer());
		DatabaseManager.loadPlayerSpells(event.getPlayer());
    }
	
	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
		PlayerManager.resetScoreboard(event.getPlayer());
	}
	
	@EventHandler
    public void onQuit(PlayerQuitEvent event) {
		PlayerManager.removePlayerInfo(event.getPlayer());
    }
	
	@EventHandler
    public void onItemUse(PlayerInteractEntityEvent event) {
		ClassManager.handleEntityInteraction(event.getPlayer(), event.getRightClicked());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileShot(ProjectileLaunchEvent event) {
		if (event.getEntity() != null && event.getEntity().getShooter() != null) {
			if (event.getEntity().getShooter() instanceof Player) {
				Player player = (Player) event.getEntity().getShooter();
				if (com.baseball435.minigames.PlayerManager.getPlayerInfo(player).inMinigame() && event.getEntity() instanceof Arrow) {
					ClassManager.handleArrowFired(player, ((Player)event.getEntity().getShooter()).getItemInHand().getTypeId(), (Arrow) event.getEntity());
				}
			}
		}
	}
	
	@EventHandler
	public void onEntityHitEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() != null && event.getDamager() instanceof Arrow && ((Arrow)event.getDamager()).getShooter() instanceof Player && !(event.getEntity() instanceof Block)) {
			Arrow arrow = (Arrow) event.getDamager();
			Player player = (Player) ((Arrow)event.getDamager()).getShooter();
			Entity target = event.getEntity();
			if (com.baseball435.minigames.PlayerManager.getPlayerInfo(player).inMinigame()) {
				ClassManager.handleArrowHit(player, target, arrow);
				event.setCancelled(true);
			}
		} else if (event.getEntity() != null && event.getDamager() instanceof Player) {
			Player player = (Player) event.getDamager();
			if (com.baseball435.minigames.PlayerManager.getPlayerInfo(player).inMinigame()) {
				ClassManager.handleEntityHitEntity(player, event.getEntity(), player.getItemInHand().getTypeId());
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onProjectileHitBlock(ProjectileHitEvent event) {
		if (event.getEntity() != null && event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {
			Player player = (Player) event.getEntity().getShooter();
			Arrow arrow = (Arrow) event.getEntity();
			BlockIterator bi = new BlockIterator(player.getWorld(), arrow.getLocation().toVector(), arrow.getVelocity().normalize(), 0, 4);
		    Block hit = null;

		    while(bi.hasNext()) {
		        hit = bi.next();
		        if(hit.getTypeId()!=0)
		            break;
		    }
		    
			if (com.baseball435.minigames.PlayerManager.getPlayerInfo(player).inMinigame()) {
				ClassManager.handleArrowBlockHit(player, hit, arrow);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onItemUse(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN){ 
	            Block b = event.getClickedBlock();
	            Sign sign = (Sign) b.getState();
	            String[] lines = sign.getLines();
	            if (lines[0].equalsIgnoreCase("[spell]")) {
	            	String spellName = lines[1].replace(" ", "_");
	            	ClassManager.purchaseSpell(event.getPlayer(), spellName);
            		event.setCancelled(true);
	            }
			}
        } else if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			boolean foundSpell = ClassManager.handleInteraction(event.getPlayer());			
			if (!foundSpell) {
				int range = 25;
				Block[] bs = event.getPlayer().getLineOfSight(null, range).toArray(new Block[0]);
				List<Entity> near = event.getPlayer().getNearbyEntities(range, range, range);
				for (Block b : bs) {
					for (Entity e : near) {
						if (e.getLocation().distance(b.getLocation()) < 2) {
							ClassManager.handleEntityInteraction(event.getPlayer(), e);
							break;
						}
					}
				}
				ClassManager.handleBlockInteraction(event.getPlayer());
			}
		}
	}
	
}
