package com.baseball435.spells.spell.wizard;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractDistanceSpell;
import com.baseball435.spells.effects.Effects;

public class LightningBoltSpell extends AbstractDistanceSpell {

	public LightningBoltSpell() {
		super("Lightning_Bolt", 500, null, "Wizard", 262, 40, 5000, 5, 15);
	}

	@Override
	public void onUse(Player player, Entity target) {
		if (checkPlayerOnTeam(player, target))
			return;
		if (player.getLocation().distance(target.getLocation()) <= maxDistance) {
			if (target instanceof Damageable) {
				Effects.createFirework(player, target.getLocation(), true, false, Color.SILVER, Color.WHITE, Type.BURST);
				player.getWorld().strikeLightning(target.getLocation());
				((Damageable) target).damage(damage, (Entity) player);
				removeMana(player);
				PlayerManager.getPlayerInfo(player).usedSpell(this);
			}
		}
	}
	
}
