package com.baseball435.spells.spell.warrior;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractNoTargetSpell;
import com.baseball435.spells.effects.Effects;

public class ShoutSpell extends AbstractNoTargetSpell {

	public ShoutSpell() {
		super("Shout", 250, null, "Warrior", 371, 20, 30000);
	}

	@Override
	public void onUse(Player player, Entity target) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 2, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 2, true));
		Effects.createFirework(player, player.getLocation(), true, false, Color.ORANGE, Color.YELLOW, Type.BALL);
		removeMana(player);
		PlayerManager.getPlayerInfo(player).usedSpell(this);
	}

}
