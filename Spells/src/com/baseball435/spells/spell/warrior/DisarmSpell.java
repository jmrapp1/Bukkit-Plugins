package com.baseball435.spells.spell.warrior;

import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractHitSpell;
import com.baseball435.spells.effects.Effects;

public class DisarmSpell extends AbstractHitSpell {

	private static final Random random = new Random();
	
	public DisarmSpell() {
		super("Disarm", 200, null, "Warrior", 369, 35, 10000, 0);
	}

	@Override
	public void onUse(Player player, Entity target) {
		if (target instanceof Player) {
			if (random.nextInt(100) + 1 <= 40) {
				Effects.createFirework(player, player.getLocation(), true, false, Color.SILVER, Color.WHITE, Type.BURST);
				((Player) target).getWorld().dropItem(((Player) target).getLocation(), ((Player) target).getInventory().getItemInHand());
				removeMana(player);
				PlayerManager.getPlayerInfo(player).usedSpell(this);
			}
		}
	}

}
