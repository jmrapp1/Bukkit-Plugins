package com.baseball435.spells.spell.wizard;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractBlockSpell;

public class TeleportSpell extends AbstractBlockSpell {

	public TeleportSpell() {
		super("Teleport", 200, null, "Wizard", 399, 25, 5000, 15);
	}

	@Override
	public void onUse(Player player, Block block) {
		if (player.getLocation().distance(block.getLocation()) <= maxDistance) {
			if (player.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ()).getType() == Material.AIR && player.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 2, block.getLocation().getBlockZ()).getType() == Material.AIR) {
				player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
				player.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
				float yaw = player.getLocation().getYaw();
				Vector direction = player.getLocation().getDirection();
				player.teleport(new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
				player.getLocation().setYaw(yaw);
				player.getLocation().setDirection(direction);
				removeMana(player);
				PlayerManager.getPlayerInfo(player).usedSpell(this);
			}
		}
	}

}
