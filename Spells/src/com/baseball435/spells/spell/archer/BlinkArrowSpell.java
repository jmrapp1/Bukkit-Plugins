package com.baseball435.spells.spell.archer;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

import com.baseball435.spells.abstr.AbstractArrowBlockSpell;

public class BlinkArrowSpell extends AbstractArrowBlockSpell {

	public BlinkArrowSpell() {
		super("Blink", 750, null, "Archer", 399, 25, 5000, 0);
	}

	@Override
	public void onHit(Player shooter, Block block, Arrow arrow) {
		if (shooter.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ()).getType() == Material.AIR && shooter.getWorld().getBlockAt(block.getLocation().getBlockX(), block.getLocation().getBlockY() + 2, block.getLocation().getBlockZ()).getType() == Material.AIR) {	
			shooter.teleport(new Location(block.getWorld(), block.getLocation().getBlockX(), block.getLocation().getBlockY() + 1, block.getLocation().getBlockZ(), shooter.getLocation().getYaw(), shooter.getLocation().getPitch()));
			shooter.getWorld().playEffect(shooter.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
			shooter.getWorld().playEffect(block.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
		}
	}

	@Override
	public void afterUse(Player player, Arrow arrow) {
		
	}

}
