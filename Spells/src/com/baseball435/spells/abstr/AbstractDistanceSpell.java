package com.baseball435.spells.abstr;

public abstract class AbstractDistanceSpell extends AbstractDamageSpell {

	protected final int maxDistance;
	
	public AbstractDistanceSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime, double damage, int maxDistance) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime, damage);
		this.maxDistance = maxDistance;
	}
	
	public int getMaxDistance() {
		return maxDistance;
	}

}
