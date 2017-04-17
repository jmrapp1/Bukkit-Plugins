package com.jmr.classes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.server.v1_8_R3.Tuple;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.jmr.pmanager.PlayerInformation;
import com.jmr.pmanager.PlayerManager;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.Spell;

public class MagicClass {

	private String name, desc;
	private ItemStack icon;
	private ArrayList<Spell> spells = new ArrayList<Spell>();
	private ArrayList<ItemStack> beginningItems = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> endingItems = new ArrayList<ItemStack>();
	
	private ItemStack chestplate, legs, boots;
	
	public MagicClass(String name, String desc, int iconId, List<String> spellsStrList, ArrayList<ItemStack> beginningItems, ArrayList<ItemStack> endingItems, ItemStack chestplate, ItemStack legs, ItemStack boots) {
		this.name = name;
		this.desc = desc;
		this.icon = new ItemStack(Material.getMaterial(iconId), 1);
		this.beginningItems = beginningItems;
		this.endingItems = endingItems;
		this.chestplate = chestplate;
		this.legs = legs;
		this.boots = boots;
		for (String s : spellsStrList) {
			Spell spell = MagicSpells.getSpellByInGameName(s);
			if (spell != null)
				spells.add(spell);
		}
	}
	
	public ItemStack getChestplate() {
		return chestplate;
	}
	
	public ItemStack getLegs() {
		return legs;
	}
	
	public ItemStack getBoots() {
		return boots;
	}
	
	public ItemStack getIcon() {
		return icon;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public String getName() {
		return name;
	}
	
	public List<ItemStack> getBeginningItems() {
		return beginningItems;
	}
	
	public List<ItemStack> getEndingItems() {
		return endingItems;
	}
	
	public ArrayList<Spell> getSpells() {
		return spells;
	}
	
	public ArrayList<ItemStack> getSpellItems(boolean extraLore) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (Spell spell : spells) {
			ItemStack item = MagicSpells.getSpellItemWithLore(spell, true, extraLore);
			if (item != null)
				items.add(item);
		}
		return items;
	}
	
	public ArrayList<ItemStack> getSpellItems(Player player, boolean extraLore) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		PlayerInformation info = PlayerManager.getInstance().getInfo(player);
		for (Spell spell : spells) {
			if (info.ownsSpell(spell)) {
				if (MagicSpells.itemIsReplaced(player, spell)) {
					System.out.println("Skipping " + spell.getInternalName());
					continue;
				}
				ItemStack item = MagicSpells.getSpellItemWithLore(spell, true, extraLore);
				if (item != null)
					items.add(item);
			}
		}
		return items;
	}
	
	public ArrayList<Tuple<ItemStack, Spell>> getSpellItemsWithSpell(Player player, boolean extraLore) {
		ArrayList<Tuple<ItemStack, Spell>> items = new ArrayList<Tuple<ItemStack, Spell>>();
		PlayerInformation info = PlayerManager.getInstance().getInfo(player);
		for (Spell spell : spells) {
			if (info.ownsSpell(spell)) {
				if (!MagicSpells.itemIsReplaced(player, spell)) {
					ItemStack item = MagicSpells.getSpellItemWithLore(spell, true, extraLore);
					if (item != null)
						items.add(new Tuple<ItemStack, Spell>(item, spell));
				}
			}
		}
		return items;
	}
	
	public boolean hasSpell(Spell spell) {
		for (Spell s : spells)
			if (spell == s)
				return true;
		return false;
	}
	
}
