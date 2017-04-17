package com.baseball435.spells.spell.wizard;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractDistanceSpell;

public class IgniteSpell extends AbstractDistanceSpell {

	public IgniteSpell() {
		super("Ignite", 450, null, "Wizard", 371, 40, 7500, 0, 15);
	}

	@Override
	public void onUse(Player player, Entity target) {
		if (checkPlayerOnTeam(player, target))
			return;
		if (player.getLocation().distance(target.getLocation()) <= maxDistance) {
			target.setFireTicks(120);
			removeMana(player);
			PlayerManager.getPlayerInfo(player).usedSpell(this);
		}
	}
	
}
