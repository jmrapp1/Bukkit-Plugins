package com.baseball435.spells.abstr;

public abstract class AbstractHealingSpell extends AbstractNoTargetSpell {

	protected final int healAmount;
	
	public AbstractHealingSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime, int healAmount) {
		super(name, price, parentSpell, className, itemId, manaCost, coolDownTime);
		this.healAmount = healAmount;
	}
	
	public int getHealAmount() {
		return healAmount;
	}

}
