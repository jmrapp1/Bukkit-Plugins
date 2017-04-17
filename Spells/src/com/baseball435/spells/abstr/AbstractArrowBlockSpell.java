package com.baseball435.spells.abstr;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class AbstractArrowBlockSpell extends AbstractArrowSpell {

	public AbstractArrowBlockSpell(String name, int price,
			AbstractSpell parentSpell, String className, int itemId,
			int manaCost, int coolDownTime, int damage) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime,
				damage);
	}

	@Override
	public void onHit(Player shooter, Entity target, Arrow arrow) {
	}


	public abstract void onHit(Player shooter, Block block, Arrow arrow);
	
}
