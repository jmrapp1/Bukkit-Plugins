package com.baseball435.spells.spell.archer;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.baseball435.spells.abstr.AbstractArrowSpell;
import com.baseball435.spells.effects.Effects;

public class KnockbackArrowSpell extends AbstractArrowSpell {

	public KnockbackArrowSpell() {
		super("Knockback", 350, null, "Archer", 280, 15, 3000, 2);
	}

	@Override
	public void afterUse(Player player, Arrow arrow) {
		arrow.setVelocity(arrow.getVelocity().multiply(1.5));
		arrow.setBounce(false);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onHit(Player shooter, Entity target, Arrow arrow) {
		if (target instanceof Damageable) {
			((Damageable) target).damage(damage, shooter);
			Vector vel = target.getVelocity();
			vel.setX(-.75);
			vel.setY(.75);
			vel.setZ(-.75);
			target.setVelocity(vel);
			Effects.createFirework(shooter, target.getLocation(), true, false, Color.GREEN, Color.LIME, Type.BURST);
		}
	}

}
