package com.baseball435.spells.spell.wizard;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.minigames.player.PlayerInfo;
import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractDistanceSpell;

public class TombSpell extends AbstractDistanceSpell {

	public TombSpell() {
		super("Tomb", 350, null, "Wizard", 353, 50, 10000, 0, 10);
	}

	@Override
	public void onUse(Player player, Entity target) {
		if (checkPlayerOnTeam(player, target))
			return;
		if (player.getLocation().distance(target.getLocation()) <= maxDistance) {
			if (target instanceof Player) {
				Block b1 = player.getWorld().getBlockAt(target.getLocation().getBlockX(), target.getLocation().getBlockY() - 1, target.getLocation().getBlockZ());
				Block b2 = player.getWorld().getBlockAt(target.getLocation().getBlockX(), target.getLocation().getBlockY() - 2, target.getLocation().getBlockZ());
				Block b3 = player.getWorld().getBlockAt(target.getLocation().getBlockX(), target.getLocation().getBlockY() - 3, target.getLocation().getBlockZ());
				if (b1.getType() != Material.BEDROCK && b2.getType() != Material.BEDROCK && b3.getType() != Material.BEDROCK) {
					player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
					PlayerInfo pi = com.baseball435.minigames.PlayerManager.getPlayerInfo(player);
					pi.getTeam().getMinigame().addDestroyedBlock(b1.getLocation(), b1.getType());
					pi.getTeam().getMinigame().addDestroyedBlock(b2.getLocation(), b2.getType());
					pi.getTeam().getMinigame().addDestroyedBlock(b3.getLocation(), b3.getType());
					b1.setType(Material.AIR);
					b2.setType(Material.AIR);
					b3.setType(Material.AIR);
					target.teleport(b2.getLocation());
					removeMana(player);
					PlayerManager.getPlayerInfo(player).usedSpell(this);
				}
			}
		}
	}

}
