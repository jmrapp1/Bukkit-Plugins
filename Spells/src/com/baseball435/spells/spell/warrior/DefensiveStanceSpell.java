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

public class DefensiveStanceSpell extends AbstractNoTargetSpell {
	
	public DefensiveStanceSpell() {
		super("Defense_Stance", 250, null, "Warrior", 288, 45, 15000);
	}

	@Override
	public void onUse(Player player, Entity target) {
		player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 2, true));
		player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 200, 2, true));
		Effects.createFirework(player, player.getLocation(), true, false, Color.ORANGE, Color.YELLOW, Type.BALL);
		removeMana(player);
		PlayerManager.getPlayerInfo(player).usedSpell(this);
	}

}
