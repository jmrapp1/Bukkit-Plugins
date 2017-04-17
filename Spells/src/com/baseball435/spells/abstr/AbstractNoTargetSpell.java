package com.baseball435.spells.abstr;


public abstract class AbstractNoTargetSpell extends AbstractSpell {

	public AbstractNoTargetSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime);
	}

}
