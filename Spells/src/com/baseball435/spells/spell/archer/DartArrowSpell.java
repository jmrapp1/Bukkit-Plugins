package com.baseball435.spells.spell.archer;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.abstr.AbstractArrowSpell;

public class DartArrowSpell extends AbstractArrowSpell {

	public DartArrowSpell() {
		super("Dart", 0, null, "Archer", 288, 10, 1500, 3);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onHit(Player shooter, Entity target, Arrow arrow) {
		if (target instanceof Damageable) {
			((Damageable) target).damage(damage, shooter);
		}
	}

	@Override
	public void afterUse(Player player, Arrow arrow) {
		arrow.setVelocity(arrow.getVelocity().multiply(4));
		arrow.setBounce(false);
	}

}
