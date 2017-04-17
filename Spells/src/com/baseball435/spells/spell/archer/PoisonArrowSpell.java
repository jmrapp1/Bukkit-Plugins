package com.baseball435.spells.spell.archer;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.baseball435.minigames.Main;
import com.baseball435.spells.abstr.AbstractArrowSpell;

public class PoisonArrowSpell extends AbstractArrowSpell {

	public PoisonArrowSpell() {
		super("Poison_Arrow", 450, null, "Archer", 262, 30, 5000, 0);
	}

	@Override
	public void afterUse(Player player, Arrow arrow) {
		arrow.setVelocity(arrow.getVelocity().multiply(1.5));
	}
	
	@Override
	public void onHit(Player shooter, Entity target, Arrow arrow) {
		if (target instanceof Damageable) {
			((Player)target).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, true));
			((Player)target).setMetadata("spell_damage",new FixedMetadataValue(Main.getBukkitPlugin(), "potion"));
			((Player)target).setMetadata("spell_killer",new FixedMetadataValue(Main.getBukkitPlugin(), shooter));
			shooter.getWorld().createExplosion(target.getLocation().getBlockX(), target.getLocation().getBlockY(), target.getLocation().getBlockZ(), 0, false, false);
		}
	}
	
}
