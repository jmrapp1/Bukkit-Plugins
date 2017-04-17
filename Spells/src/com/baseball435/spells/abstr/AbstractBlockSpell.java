package com.baseball435.spells.abstr;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public abstract class AbstractBlockSpell extends AbstractSpell {

	protected final int maxDistance;
	
	public AbstractBlockSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime, int maxDistance) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime);
		this.maxDistance = maxDistance;
	}

	@Override
	public void onUse(Player player, Entity target) {
	}
	
	public abstract void onUse(Player player, Block block);

}
