package com.baseball435.spells.abstr;

public abstract class AbstractHitSpell extends AbstractDamageSpell {

	public AbstractHitSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime, double damage) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime, damage);
	}

}
