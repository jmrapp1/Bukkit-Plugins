package com.baseball435.spells.abstr;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.PlayerManager;

public abstract class AbstractArrowSpell extends AbstractDamageSpell {

	public AbstractArrowSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime, int damage) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime, damage);
	}
	
	@Override
	public void onUse(Player player, Entity target) {
		player.launchProjectile(Arrow.class);
		removeMana(player);
		PlayerManager.getPlayerInfo(player).usedSpell(this);
	}
	
	public abstract void onHit(Player shooter, Entity target, Arrow arrow);

	public abstract void afterUse(Player player, Arrow arrow);
	
}
