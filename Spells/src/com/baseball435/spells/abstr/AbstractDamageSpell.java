package com.baseball435.spells.abstr;

public abstract class AbstractDamageSpell extends AbstractSpell {

	protected final double damage;
	
	public AbstractDamageSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime, double damage) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime);
		this.damage = damage;
	}
	
	public double getDamage() {
		return damage;
	}

}
