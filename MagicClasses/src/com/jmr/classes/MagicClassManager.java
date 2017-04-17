package com.jmr.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class MagicClassManager {

	private static final MagicClassManager instance = new MagicClassManager();
	
	private final ArrayList<MagicClass> magicClasses = new ArrayList<MagicClass>();
	
	private MagicClassManager() {
		//magicClasses.add(new MagicClass("Bowman", "Given a bow and 20 arrows\nto attack from a distance.", new ItemStack[] { new ItemStack(Material.BOW), new ItemStack(Material.ARROW, 20) }));
		//magicClasses.add(new MagicClass("Sword Swinger", "Use your little amount of\nstrength to eliminate the enemy.", new ItemStack[] { new ItemStack(Material.WOOD_SWORD) }));
		//magicClasses.add(new MagicClass("Healer", "Heal yourself and keep yourself\nalive while attacking enemies.", new ItemStack[] { new ItemStack(Material.COOKED_BEEF, 3), new ItemStack(Material.GOLD_PICKAXE) }));
		try {
			magicClasses.addAll(new MagicClassLoader().loadClasses("plugins\\MagicClasses\\magicClasses.yml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		//ItemStack infiniteBow = new ItemStack(Material.BOW);
		//infiniteBow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		//magicClasses.add(new MagicClass("Archer", "Given a bow with infinite\narrows to never run out.", new ItemStack[] { infiniteBow, new ItemStack(Material.ARROW) }));
		//magicClasses.add(new MagicClass("Fighter", "Use your strength and\nsword to eliminate everyone.", new ItemStack[] { new ItemStack(Material.IRON_SWORD) }));
		//magicClasses.add(new MagicClass("War Doctor", "Stay alive longer than anyone else\nwhile eliminating everyone.", new ItemStack[] { new ItemStack(Material.COOKED_BEEF), new ItemStack(Material.GOLD_PICKAXE) }));
	}
	
	public ArrayList<MagicClass> getMagicClasses() {
		return magicClasses;
	}
	
	public MagicClass getMagicClass(String name) {
		for (MagicClass k : magicClasses)
			if (k.getName().equalsIgnoreCase(name))
				return k;
		return null;
	}

	public static MagicClassManager getInstance() {
		return instance;
	}
	
}
