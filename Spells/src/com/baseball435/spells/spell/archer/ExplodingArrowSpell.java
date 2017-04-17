package com.baseball435.spells.spell.archer;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.abstr.AbstractArrowSpell;

public class ExplodingArrowSpell extends AbstractArrowSpell {

	public ExplodingArrowSpell() {
		super("Exploding_Arrow", 400, null, "Archer", 318, 20, 2500, 5);
	}

	@Override
	public void afterUse(Player player, Arrow arrow) {
		arrow.setVelocity(arrow.getVelocity().multiply(1.5));
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onHit(Player shooter, Entity target, Arrow arrow) {
		if (target instanceof Damageable) {
			((Damageable) target).damage(damage, shooter);
			shooter.getWorld().createExplosion(target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ(), 0, false, false);
		}
	}

}
