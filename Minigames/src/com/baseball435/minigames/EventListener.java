package com.baseball435.minigames;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;

import com.baseball435.minigames.player.PlayerInfo;


public class EventListener implements Listener {
	
	@EventHandler
    public void onLogin(PlayerLoginEvent event) {
		PlayerManager.addPlayerInfo(event.getPlayer());
		Main.LOG.info("New player");
    }
	
	@EventHandler
    public void onQuit(PlayerQuitEvent event) {
		PlayerManager.removePlayerInfo(event.getPlayer());
    }
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		PlayerInfo pi = PlayerManager.getPlayerInfo(event.getPlayer());
		if (pi.inMinigame()) {
			if (!pi.getTeam().getMinigame().blockInside(event.getPlayer().getLocation())) {
				if (event.getFrom().getBlockX() == event.getTo().getBlockX() && event.getFrom().getBlockY() == event.getTo().getBlockY() && event.getFrom().getBlockZ() == event.getTo().getBlockZ())
                   return;
				event.setCancelled(true);
				event.getPlayer().teleport(pi.getTeam().getStartLocation());
			}
		}
	}
	
	@EventHandler
	public void onEntityDeath(PlayerDeathEvent event) {
		if (event.getEntity() instanceof Player && event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player){
			PlayerInfo killed = PlayerManager.getPlayerInfo(event.getEntity());
			PlayerInfo killer = PlayerManager.getPlayerInfo(event.getEntity().getKiller());
			if (killed.inMinigame() && killer.inMinigame()) {
				if (killed.getTeam().getMinigame() == killer.getTeam().getMinigame()) {
					event.getDrops().clear();
					((Player) event.getEntity().getKiller()).sendMessage(ChatColor.GREEN + "You killed " + ((Player) event.getEntity()).getDisplayName());
					((Player) event.getEntity()).sendMessage(ChatColor.RED + "You were killed by " + ((Player) event.getEntity().getKiller()).getDisplayName());
					killer.getTeam().addScore(1); //minigame team score
					killer.addScore(1); //personal score
					com.baseball435.spells.PlayerManager.getPlayerInfo(event.getEntity().getKiller()).setMana(100);
					event.setDeathMessage("");
					event.setKeepLevel(true);
				}
			}
		}/* else if (event.getEntity() instanceof Player && event.getEntity().getKiller() == null) {
			Main.LOG.info("Death by potion");
			Player player = (Player) event.getEntity();
			if (player.hasMetadata("spell_damage")) {
				String info = player.getMetadata("spell_damage").get(0).asString();
				if (info.equalsIgnoreCase("potion") || info.equalsIgnoreCase("attack")) {
					if (player.hasMetadata("spell_killer")) {
						Player killer = (Player) player.getMetadata("spell_killer");
						PlayerInfo killerInfo = PlayerManager.getPlayerInfo(killer);
						
						event.getDrops().clear();
						killer.sendMessage(ChatColor.GREEN + "You killed " + player.getDisplayName());
						player.sendMessage(ChatColor.RED + "You were killed by " + ((Player) event.getEntity().getKiller()).getDisplayName());
						killerInfo.getTeam().addScore(1); //minigame team score
						killerInfo.addScore(1); //personal score
						com.baseball435.spells.PlayerManager.getPlayerInfo(killer).setMana(100);
						event.setDeathMessage("");
						event.setKeepLevel(true);
					}
				}
			}
		}*/
	}
	
	@EventHandler
	public void onCommandUsed(PlayerCommandPreprocessEvent event) {
		String[] args = event.getMessage().split(" ");
		String command = args[0];
		if (PlayerManager.getPlayerInfo(event.getPlayer()).inMinigame()) {
			if (!event.getPlayer().isOp() && (command.contains("spawn") || command.contains("tp") || command.contains("home") || command.contains("sethome"))) {
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "To leave the minigame type /leave!");
			}
		}
	}
	
	@EventHandler
	public void onHelmetRemoved(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player)
			if (PlayerManager.getPlayerInfo((Player) event.getWhoClicked()).inMinigame())
				if (event.getSlot() == 5 || event.getSlot() == 39)
					event.setCancelled(true);
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		MinigameManager.handleBlockInside(event.getBlock().getLocation(), event.getBlock().getType());
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		BlockState bs = event.getBlockReplacedState();
		Material replace = Material.AIR;
		if (bs.getType() == Material.LAVA || bs.getType() == Material.STATIONARY_LAVA)
			replace = Material.LAVA;
		else if (bs.getType() == Material.WATER || bs.getType() == Material.STATIONARY_WATER)
			replace = Material.WATER;
		else if (bs.getType() == Material.FIRE)
			replace = Material.FIRE;
		MinigameManager.handleBlockInside(event.getBlock().getLocation(), replace);
	}
	
	@EventHandler
	public void onExplosion(EntityExplodeEvent event) {
		for (Block b : event.blockList())
			MinigameManager.handleBlockInside(b.getLocation(), b.getType());
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player p = event.getPlayer();
		final PlayerInfo pc = PlayerManager.getPlayerInfo(p);
		if (pc.inMinigame()) {
			event.setRespawnLocation(pc.getTeam().getStartLocation());
			Main.getBukkitServer().getScheduler().scheduleSyncDelayedTask(Main.getBukkitPlugin(), new Runnable() {
				public void run() {
					p.teleport(pc.getTeam().getStartLocation());
					pc.getTeam().getMinigame().onPlayerRespawn(p);
				}
			}, 2L);
		}
	}
	
	@EventHandler
	public void onPlayerHitPlayer(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
			Player p1 = (Player) event.getEntity();
			Player p2 = (Player) event.getDamager();
			if (PlayerManager.getPlayerInfo(p1).inMinigame() && PlayerManager.getPlayerInfo(p2).inMinigame()) {
				if (PlayerManager.getPlayerInfo(p1).getTeam().getMinigame() == PlayerManager.getPlayerInfo(p2).getTeam().getMinigame()) {
					if (PlayerManager.getPlayerInfo(p1).getTeam() == PlayerManager.getPlayerInfo(p2).getTeam()) {
						event.setCancelled(true);
					}
				}
			}
		}
	}
	
	@EventHandler
    public void onInteract(PlayerInteractEvent event){
        final Player player = event.getPlayer();
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(event.getClickedBlock().getType() == Material.SIGN_POST || event.getClickedBlock().getType() == Material.SIGN || event.getClickedBlock().getType() == Material.WALL_SIGN){ 
                Block b = event.getClickedBlock();
                Sign sign = (Sign) b.getState();
                String[] lines = sign.getLines();
                if (lines[0].equalsIgnoreCase("[Minigame]")) {
                	String minigameName = lines[1];
                	if (MinigameManager.minigameExists(minigameName)) {
                		MinigameManager.handleJoinMinigame(player, minigameName);
                		event.setCancelled(true);
                	} else {
                		Main.LOG.info("Error loading minigame named: \"" + lines[1] + "\".");
                	}
                }
            }
        } else if (PlayerManager.getPlayerInfo(player).inMinigame() && event.getAction() == Action.LEFT_CLICK_BLOCK) {
        	Block block = event.getClickedBlock();
        	BlockFace blockFace = event.getBlockFace();
        	Block relativeBlock = block.getRelative(blockFace);
        	if (relativeBlock.getType() == Material.FIRE) {
        		MinigameManager.handleBlockInside(relativeBlock.getLocation(), Material.FIRE);
        	}
        }
    }
	
}
