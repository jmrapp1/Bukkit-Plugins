package com.baseball435.spells;

import java.util.ArrayList;

import com.baseball435.spells.abstr.AbstractSpell;

public class PlayerInfo {

	private static final int GENERAL_SPELL_DELAY = 1000;
	
	private final ArrayList<String> ownedSpells = new ArrayList<String>();
	private String className;
	private int mana;
	private long lastSpellTime;
	private AbstractSpell lastSpellUsed;
	
	public PlayerInfo(String className, int mana) {
		this.className = className;
		this.mana = mana;
	}
	
	public String getPlayerClass() {
		return className;
	}
	
	public void setPlayerClass(String className) {
		this.className = className;
	}
	
	public int getMana() {
		return mana;
	}
	
	public void addMana(int amount) {
		mana += amount;
	}
	
	public void subtractMana(int amount) {
		addMana(-amount);
	}
	
	public void setMana(int mana) {
		this.mana = mana;
	}

	public boolean canUseSpell(AbstractSpell spell) {
		if (spell == lastSpellUsed && System.currentTimeMillis() - lastSpellTime >= spell.getCoolDownTime())
			return true;
		else if (spell != lastSpellUsed && System.currentTimeMillis() - lastSpellTime >= GENERAL_SPELL_DELAY)
			return true;
		return false;
	}
	
	public void usedSpell(AbstractSpell spell) {
		lastSpellTime = System.currentTimeMillis();
		lastSpellUsed = spell;
	}
	
	public ArrayList<String> getOwnedSpells() {
		return ownedSpells;
	}
	
	public boolean ownsSpell(String name) {
		for (String s : ownedSpells)
			if (s.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public void addSpell(String name) {
		ownedSpells.add(name);
	}
	
}
