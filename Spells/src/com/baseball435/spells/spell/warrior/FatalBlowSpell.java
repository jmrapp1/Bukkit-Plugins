package com.baseball435.spells.spell.warrior;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractHitSpell;
import com.baseball435.spells.effects.Effects;

public class FatalBlowSpell extends AbstractHitSpell {

	public FatalBlowSpell() {
		super("Fatal_Blow", 600, null, "Warrior", 353, 20, 5000, 6);
	}

	@Override
	public void onUse(Player player, Entity target) {
		if (target instanceof Damageable ) {
			((Damageable) target).damage(damage, (Entity) player);
			Effects.createFirework(player, target.getLocation(), true, false, Color.RED, Color.YELLOW, Type.BALL);
			Effects.createFirework(player, target.getLocation(), true, false, Color.ORANGE, Color.SILVER, Type.BURST);
			removeMana(player);
			PlayerManager.getPlayerInfo(player).usedSpell(this);
		}
	}

}
