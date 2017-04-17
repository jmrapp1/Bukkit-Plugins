package com.jmr.pmanager;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.nisovin.magicspells.Spell;

public class PlayerInformation {

	private String uuid, name;
	private int coins;
	private CopyOnWriteArrayList<String> ownedSpells = new CopyOnWriteArrayList<String>();
	
	public PlayerInformation(String uuid, String name, int coins, String[] ownedSpellItems) {
		this.uuid = uuid;
		this.name = name;
		this.coins = coins;
		this.ownedSpells = new CopyOnWriteArrayList<String>(Arrays.asList(ownedSpellItems));
	}
	
	public String getUuid() {
		return uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public List<String> getOwnedSpells() {
		return ownedSpells;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean ownsSpell(Spell spell) {
		for (int i = 0; i < ownedSpells.size(); i++) {
			String spellStr = ownedSpells.get(i);
			if (spellStr.equalsIgnoreCase(spell.getName()))
				return true;
		}
		return false;
	}
	
	public void addSpell(Spell spell) {
		if (!ownsSpell(spell)) {
			ownedSpells.add(spell.getInternalName());
		}
	}
	
	public void removeSpell(Spell spell) {
		synchronized(ownedSpells) {
			ownedSpells.remove(spell.getInternalName());
		}
	}
	
	public void setCoins(int c) {
		this.coins = c;
	}
	
}
