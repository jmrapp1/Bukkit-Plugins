package com.jmr.region.regions;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.jmr.cmanager.CoinManager;
import com.jmr.pmanager.PlayerInformation;
import com.jmr.pmanager.PlayerManager;
import com.jmr.region.AbstractRegion;

public class MagicKitRegion extends AbstractRegion {

	private int killPay = 3;
	
	public MagicKitRegion(String name, String displayName, int killPay, double startX, double startY, double startZ, double endX, double endY, double endZ) {
		super(name, displayName, "magickitregion", startX, startY, startZ, endX, endY, endZ, true, false, false, true, false, false);
		this.killPay = killPay;
	}

	@Override
	public void onEnter(Player player) {
		PlayerManager.getInstance().backupInventory(player);
		player.getInventory().clear();
	}

	@Override
	public void onLeave(Player player) {
		PlayerManager.getInstance().reloadBackupInventory(player);
	}

	@Override
	public void onPlayerDeath(PlayerDeathEvent event) {
		if (event.getEntity().getKiller() instanceof Player) {
			event.getDrops().clear();
			event.setDeathMessage("");
			event.getEntity().sendMessage(ChatColor.DARK_RED + "You have been killed by " + event.getEntity().getKiller().getDisplayName());
			event.getEntity().getKiller().sendMessage(ChatColor.DARK_RED + "You have killed " + event.getEntity().getDisplayName() + " and got " + killPay + " coins!");
			CoinManager.getInstance().addCoins(event.getEntity().getKiller(), killPay);
		}
	}

	@Override
	public void onPlayerDamage(EntityDamageEvent event) {
		
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		
	}

}
