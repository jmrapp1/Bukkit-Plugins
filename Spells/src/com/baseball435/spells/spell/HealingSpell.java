package com.baseball435.spells.spell;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractHealingSpell;
import com.baseball435.spells.effects.Effects;

public class HealingSpell extends AbstractHealingSpell {

	public HealingSpell() {
		super("Heal", 0, null, "Healer", 280, 5, 2000, 6);
	}

	@Override
	public void onUse(Player player, Entity target) {
		Damageable d = (Damageable) player;
		if (d.getHealth() < d.getMaxHealth()) {
			double amount = d.getMaxHealth() + healAmount;
			if (amount > d.getMaxHealth())
				amount = d.getMaxHealth();
			d.setHealth(amount);
			Effects.createFirework(player, player.getLocation(), true, true, Color.LIME, Color.GREEN, Type.BURST);
			removeMana(player);
			PlayerManager.getPlayerInfo(player).usedSpell(this);
		}
	}

}
